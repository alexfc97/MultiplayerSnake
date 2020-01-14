import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

public class Server {
    private int numberOfPlayers;
    private SpaceRepository repository;
    private SequentialSpace gameState;
    private SequentialSpace playersInputSpaces[];
    private Snake playerOne;

    public Server(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public static void main(String[] args) {
        // TODO input number of players from terminal
        Server server = new Server(2);
        server.start();
    }

    private void start() {
        repository = createRepo();
        addGate(repository);
        createSpaceMatrix();
        intiPlayerOne();
        handlePlayerCommands(playerOneInput);
    }

    private void intiPlayerOne() {
        try {
            gameState.get(new ActualField(10), new ActualField(10), new ActualField(true));
            this.playerOne = new Snake(1, 10, 10, Board.TILESIZE);
            playerOne.snakeBody.add(new SnakeBodyPart(playerOne.xCorHead, playerOne.yCorHead, 1, Board.TILESIZE));
            gameState.put(10, 10, 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void createInputSpaces() {
        for (int id : IDlist) {
            String name = "player_" + id + "_input";
            SequentialSpace space = new SequentialSpace();
            repository.add(name, space);
        }
    }

    private SpaceRepository createRepo() {
        SpaceRepository repository = new SpaceRepository();
        gameState = new SequentialSpace();
        playerOneInput = new SequentialSpace();
        repository.add("gameState", gameState);
        repository.add("playerOneInput", playerOneInput);
        return repository;
    }

    private void addGate(SpaceRepository repo) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter URI of the chat server or press enter for default: ");
            String uri = input.readLine();
            if (uri.isEmpty()) {
                uri = "tcp://127.0.0.1:9001/?keep";
            }
            URI myUri = new URI(uri);
            String gateUri = "tcp://" + myUri.getHost() + ":" + myUri.getPort() + "?keep";
            System.out.println("Opening repository gate at " + gateUri + "...");
            repo.addGate(gateUri);
        } catch (Exception e) {
            System.out.println("Failed to add gate to repository. Error stack: ");
            e.printStackTrace();
        }
    }

    private void createSpaceMatrix() {
        for (int row = 0; row < Board.HEIGHT / 10; row++) {
            for (int col = 0; col < Board.WIDTH / 10; col++) {
                try {
                    gameState.put(row, col, true);
                } catch (InterruptedException e) {
                    System.out.println("Failed to create the space matrix. Error stack");
                    e.printStackTrace();
                }
            }
        }
    }

    private void handlePlayerCommands(SequentialSpace playerCommands) {
        while (true) {
            try {
                // Getting the command from the player
                Object[] command = playerCommands.get(new FormalField(Integer.class), new FormalField(String.class));
                // Parsing it
                int playerID = (int) command[0];
                String direction = (String) command[1];
                System.out.println(
                        "New command from player" + playerID + ":" + "\n " + "    Direction: " + direction + "\n");
                // Running move

                move(playerID, direction, playerOne.xCorHead, playerOne.yCorHead);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void move(int playerID, String direction, int xCor, int yCor) {
        int newXCor = xCor;
        int newYCor = yCor;

        if (direction.equals("right")) {
            newXCor = (xCor + 1) % (Board.WIDTH / 10);
        }

        if (direction.equals("left")) {
            newXCor = xCor - 1;
            if (newXCor < 0) {
                newXCor = (newXCor + Board.WIDTH / 10);
            }
        }

        if (direction.equals("up")) {
            newYCor = yCor - 1;
            if (newYCor < 0) {
                newYCor = (newYCor + (Board.HEIGHT / 10));
            }

        }

        if (direction.equals("down")) {
            newYCor = (yCor + 1) % (Board.HEIGHT / 10);
        }

        System.out.println("New coordinates: " + newXCor + ", " + newYCor);

        try {
            System.out.println("Updating the game state..");
            gameState.get(new ActualField(newXCor), new ActualField(newYCor), new ActualField(true));
            System.out.println("After getting the empty cell");
            playerOne.snakeBody.add(new SnakeBodyPart(newXCor, newYCor, 1, Board.TILESIZE));
            if (playerOne.snakeBody.size() > playerOne.length) {
                int oldX = playerOne.snakeBody.get(0).xCor;
                int oldY = playerOne.snakeBody.get(0).yCor;
                playerOne.snakeBody.remove(0);
                gameState.get(new ActualField(oldX), new ActualField(oldY), new FormalField(Integer.class));
                System.out.println("After getting old cell");
                gameState.put(oldX, oldY, true);
            }
            playerOne.xCorHead = newXCor;
            playerOne.yCorHead = newYCor;
            gameState.put(newXCor, newYCor, playerID);
            System.out.println("Sending new coordinates to client..");
            System.out.println("---------");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}