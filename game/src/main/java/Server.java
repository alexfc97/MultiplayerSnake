import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;
import java.util.Random;

public class Server {
    private int numberOfPlayers;
    private int startID;
    private Random rand;
    private int initSnakeLength;
    private HashMap<Integer, SequentialSpace> idMap = new HashMap<Integer, SequentialSpace>();
    protected static HashMap<Integer, Snake> snakeMap = new HashMap<Integer, Snake>();
    private SpaceRepository repository;
    private static SequentialSpace gameState, lobby, IDs;

    public Server() {
        rand = new Random();
        startID = rand.nextInt(100);

    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    private void start() {
        initNumberOfPlayers();
        initSnakeLength();
        repository = initRepo();
        createSpaceMatrix();
        addGate(repository);
        waitingForPlayers();
        initThreads();
    }

    private void initSnakeLength() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Choose the initial length of the snakes: ");
            String n = input.readLine();
            if (n.isEmpty()) {
                n = "5";
            } else if (Integer.valueOf(n) > 20) {
                System.out.println("Too long. Setting the length to the maximum 20");
                n = "20";

            }
            this.initSnakeLength = Integer.valueOf(n);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initNumberOfPlayers() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the desired number of players: ");
            String n = input.readLine();
            if (n.isEmpty()) {
                n = "2";
            }
            int players = Integer.valueOf(n);
            this.numberOfPlayers = players;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initThreads() {
        idMap.forEach((id, space) -> {
            Thread t = new PlayerHandler(space);
            t.start();
        });
    }

    private void waitingForPlayers() {
        int id = startID;
        for (int i = 0; i < numberOfPlayers; i++) {
            try {
                // Creating IDs
                lobby.get(new ActualField("connected"));
                String name = "player_" + id + "_input";
                SequentialSpace space = new SequentialSpace();
                idMap.put(id, space);
                repository.add(name, space);
                System.out.println(id);
                IDs.put(id);

                // Creating Snakes
                int randX = rand.nextInt(Board.WIDTH / 10 - 1);
                int randY = rand.nextInt(Board.HEIGHT / 10 - 1);
                gameState.get(new ActualField(randX), new ActualField(randY), new ActualField(true));
                Snake snake = new Snake(id, randX, randY, initSnakeLength);
                snake.snakeBody.add(new SnakeBodyPart(randX, randY, id, Board.TILESIZE));
                snakeMap.put(id, snake);
                gameState.put(randX, randY, id);

                id++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // private void initPlayers() {
    // int id = startID;
    // try {
    // for (int i = 0; i < numberOfPlayers; i++) {
    // int randX = rand.nextInt(Board.WIDTH / 10 - 1);
    // int randY = rand.nextInt(Board.HEIGHT / 10 - 1);
    // gameState.get(new ActualField(randX), new ActualField(randY), new
    // ActualField(true));
    // Snake snake = new Snake(id, randX, randY, initSnakeLength);
    // snake.snakeBody.add(new SnakeBodyPart(randX, randY, id, Board.TILESIZE));
    // snakeMap.put(id, snake);
    // gameState.put(randX, randY, id);
    // id++;
    // }

    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }

    // }

    private SpaceRepository initRepo() {
        SpaceRepository repository = new SpaceRepository();
        gameState = new SequentialSpace();
        lobby = new SequentialSpace();
        IDs = new SequentialSpace();
        repository.add("gameState", gameState);
        repository.add("lobby", lobby);
        repository.add("IDs", IDs);
        return repository;
    }

    private void addGate(SpaceRepository repo) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter an IP address for the  server or press enter for localhost:9001:  ");
            String ip = input.readLine();
            if (ip.isEmpty()) {
                ip = "localhost:9001";
            }
            String uri = "tcp://" + ip + "/?keep";
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

    // private void handlePlayerCommands(SequentialSpace playerCommands) {
    // while (true) {
    // try {
    // // Getting the command from the player
    // Object[] command = playerCommands.get(new FormalField(Integer.class), new
    // FormalField(String.class));
    // // Parsing it
    // int playerID = (int) command[0];
    // String direction = (String) command[1];
    // System.out.println(
    // "New command from player" + playerID + ":" + "\n " + " Direction: " +
    // direction + "\n");
    // // Running move

    // move(playerID, direction, snakeMap.get(playerID).xCorHead,
    // snakeMap.get(playerID).yCorHead);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // }
    // }

    protected static void move(int playerID, String direction, int xCor, int yCor) {
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
            snakeMap.get(playerID).snakeBody.add(new SnakeBodyPart(newXCor, newYCor, 1, Board.TILESIZE));
            if (snakeMap.get(playerID).snakeBody.size() > snakeMap.get(playerID).length) {
                int oldX = snakeMap.get(playerID).snakeBody.get(0).xCor;
                int oldY = snakeMap.get(playerID).snakeBody.get(0).yCor;
                snakeMap.get(playerID).snakeBody.remove(0);
                gameState.get(new ActualField(oldX), new ActualField(oldY), new FormalField(Integer.class));
                System.out.println("After getting old cell");
                gameState.put(oldX, oldY, true);
            }
            snakeMap.get(playerID).xCorHead = newXCor;
            snakeMap.get(playerID).yCorHead = newYCor;
            gameState.put(newXCor, newYCor, playerID);
            System.out.println("Sending new coordinates to client..");
            System.out.println("---------");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}