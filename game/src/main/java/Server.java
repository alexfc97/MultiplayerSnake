import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

public class Server {
    private SpaceRepository repository;
    private SequentialSpace gameState, playerOneInput, playerOneOutput;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    private void start() {
        repository = createRepo();
        addGate(repository);
        createSpaceMatrix();
        handlePlayerCommands(playerOneInput);
    }

    private void handlePlayerCommands(SequentialSpace playerCommands) {
        while (true) {
            try {
                Object[] command = playerCommands.get(new FormalField(Integer.class), new FormalField(String.class),
                        new FormalField(Integer.class), new FormalField(Integer.class));
                int playerID = (int) command[0];
                String direction = (String) command[1];
                int xCor = (int) command[2];
                int yCor = (int) command[3];
                System.out.println("New command from player" + playerID + ":" + "\n " + "    Direction: " + direction
                        + "\n" + "    Coordinates: " + xCor + ", " + yCor);
                move(playerID, direction, xCor, yCor);
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
            gameState.get(new ActualField(newXCor), new ActualField(newYCor), new ActualField(-1));
            gameState.put(newXCor, newYCor, playerID);
            System.out.println("Sending new coordinates to client..");
            System.out.println("---------");
            playerOneOutput.put(newXCor, newYCor);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private SpaceRepository createRepo() {
        SpaceRepository repository = new SpaceRepository();
        gameState = new SequentialSpace();
        playerOneInput = new SequentialSpace();
        playerOneOutput = new SequentialSpace();
        repository.add("gameState", gameState);
        repository.add("playerOneInput", playerOneInput);
        repository.add("playerOneOutput", playerOneOutput);
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
                    gameState.put(row, col, -1);
                } catch (InterruptedException e) {
                    System.out.println("Failed to create the space matrix. Error stack");
                    e.printStackTrace();
                }
            }
        }
    }

}