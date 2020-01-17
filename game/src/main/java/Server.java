import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;

import org.jspace.*;

public class Server {
    private static int foodSpawnCounter = 0;
    private static int foodSpawnTimer = 300; // Every 30 secs

    private static int numberOfPlayers;
    private int startID;
    private Random rand;
    private int initSnakeLength;
    private static HashMap<Integer, SequentialSpace> idMap = new HashMap<Integer, SequentialSpace>();
    protected static HashMap<Integer, Snake> snakeMap = new HashMap<Integer, Snake>();
    protected static HashMap<Integer, Thread> threadMap = new HashMap<Integer, Thread>();
    private static int maxfood = 3;
    private static ArrayList<Food> foodlist = new ArrayList<>();

    // protected static HashMap<Integer, Boolean> isAlive = new HashMap<Integer,
    // Boolean>();

    private SpaceRepository repository;
    private static SequentialSpace lobby, IDs, isAlive;
    private static SequentialSpace gameState;

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
        initRepo();
        createSpaceMatrix();
        initFood();
        addGate(repository);
        waitingForPlayers();
        initThreads();
    }

    private void initFood() {
        for (int i = 0; i < maxfood; i++) {
            int randx = rand.nextInt(79);
            int randy = rand.nextInt(79);
            Food f = new Food(randx, randy);
            foodlist.add(f);
            try {
                gameState.get(new ActualField(f.xCor), new ActualField(f.yCor), new ActualField(true),
                        new ActualField(false));
                gameState.put(f.xCor, f.yCor, true, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

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

    private void initRepo() {
        SpaceRepository repository = new SpaceRepository();
        gameState = new SequentialSpace();
        lobby = new SequentialSpace();
        IDs = new SequentialSpace();
        isAlive = new SequentialSpace();
        repository.add("gameState", gameState);
        repository.add("lobby", lobby);
        repository.add("IDs", IDs);
        repository.add("isAlive", isAlive);
        this.repository = repository;
    }

    private void createSpaceMatrix() {
        for (int row = 0; row < Board.HEIGHT / 10; row++) {
            for (int col = 0; col < Board.WIDTH / 10; col++) {
                try {
                    gameState.put(row, col, true, false);
                } catch (InterruptedException e) {
                    System.out.println("Failed to create the space matrix. Error stack");
                    e.printStackTrace();
                }
            }
        }
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

    private void waitingForPlayers() {
        int id = startID;
        for (int i = 0; i < numberOfPlayers; i++) {
            try {
                // Creating IDs and Input spaces
                lobby.get(new ActualField("connected"));
                String name = "player_" + id + "_input";
                SequentialSpace space = new SequentialSpace();
                idMap.put(id, space);
                repository.add(name, space);
                IDs.put(id);

                // Creating Snakes
                int randX = rand.nextInt(Board.WIDTH / 10 - 1);
                int randY = rand.nextInt(Board.HEIGHT / 10 - 1);
                gameState.get(new ActualField(randX), new ActualField(randY), new ActualField(true),
                        new ActualField(false));
                Snake snake = new Snake(id, randX, randY, initSnakeLength);
                snake.snakeBody.add(new SnakeBodyPart(randX, randY, id, Board.TILESIZE));
                snakeMap.put(id, snake);
                gameState.put(randX, randY, id, false);

                // Setting the snakes to be alive
                isAlive.put(id, true, false);

                id++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(numberOfPlayers + " Have joined !");

    }

    private void initThreads() {
        idMap.forEach((id, space) -> {
            Thread t = new PlayerHandler(space);
            threadMap.put(id, t);
            t.start();
        });
    }

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
            Object[] cell = gameState.getp(new ActualField(newXCor), new ActualField(newYCor), new ActualField(true),
                    new FormalField(Boolean.class));
            // If theres a snake body
            if (cell == null) {
                removeSnake(playerID);
                isAlive.get(new ActualField(playerID), new FormalField(Boolean.class), new FormalField(Boolean.class));
                isAlive.put(playerID, false, false);
                numberOfPlayers--;
                checkWinner();
                threadMap.get(playerID).interrupt();
                // If theres food
            } else if ((boolean) cell[3]) {
                snakeMap.get(playerID).length++;
                foodlist.remove(0);
            }
            foodSpawnCounter++;
            // Adding new food every 5 secs if theres food missing
            if (foodSpawnCounter >= foodSpawnTimer) {
                if (foodlist.size() < maxfood) {
                    Object[] newFoodCell = gameState.get(new FormalField(Integer.class), new FormalField(Integer.class),
                            new ActualField(true), new ActualField(false));
                    int newfoodx = (int) newFoodCell[0];
                    int newfoody = (int) newFoodCell[1];
                    foodlist.add(new Food(newfoodx, newfoody));
                    gameState.put(newfoodx, newfoody, true, true);
                    foodSpawnCounter = 0;
                }
            }
            System.out.println("After getting the empty cell");
            // Adding a bodypart to the snake object
            snakeMap.get(playerID).snakeBody.add(new SnakeBodyPart(newXCor, newYCor, playerID, Board.TILESIZE));
            // Controlling for length
            if (snakeMap.get(playerID).snakeBody.size() > snakeMap.get(playerID).length) {
                int oldX = snakeMap.get(playerID).snakeBody.get(0).xCor;
                int oldY = snakeMap.get(playerID).snakeBody.get(0).yCor;
                snakeMap.get(playerID).snakeBody.remove(0);
                gameState.get(new ActualField(oldX), new ActualField(oldY), new FormalField(Integer.class),
                        new FormalField(Boolean.class));
                System.out.println("After getting old cell");
                gameState.put(oldX, oldY, true, false);
            }
            // Updating the snake
            snakeMap.get(playerID).xCorHead = newXCor;
            snakeMap.get(playerID).yCorHead = newYCor;
            // Updating the game state
            gameState.put(newXCor, newYCor, playerID, false);
            System.out.println("Sending new coordinates to client..");
            System.out.println("---------");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void checkWinner() {
        int aliveCount = 0;
        int winnerID = 0;
        List<Object[]> status = isAlive.queryAll(new FormalField(Integer.class), new FormalField(Boolean.class),
                new FormalField(Boolean.class));
        for (Object[] obj : status) {
            if ((boolean) obj[1] == true) {
                aliveCount++;
                winnerID = (int) obj[0];
            }

        }

        if (aliveCount == 1) {
            System.out.println("Player " + winnerID + " won!!");
            try {
                isAlive.get(new ActualField(winnerID), new FormalField(Boolean.class), new FormalField(Boolean.class));
                isAlive.put(winnerID, true, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            threadMap.get(winnerID).interrupt();

        }
    }

    private static void removeSnake(int playerID) {
        try {
            List<Object[]> allCells = gameState.queryAll(new FormalField(Integer.class), new FormalField(Integer.class),
                    new ActualField(playerID), new FormalField(Boolean.class));
            for (Object[] obj : allCells) {
                gameState.get(new ActualField(obj[0]), new ActualField(obj[1]), new ActualField(obj[2]),
                        new FormalField(Boolean.class));
                gameState.put(obj[0], obj[1], true, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}