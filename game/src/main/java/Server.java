import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.jspace.*;

public class Server {
    private SequentialSpace gameState, playerOneInput, playerOneOutput;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    private void start() {
        SpaceRepository repository = createRepo();
        addGate(repository);
        createSpaceMatrix();
        handlePlayerCommands(playerOneInput);
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
            if (uri.equals("ali")) {
                uri = "tcp://127.0.0.1:9001/?keep";
            } else if (uri.equals("alex")){
                uri = "tcp://10.16.189.126:9001/?keep";
            } else if (uri.equals("marius")){
                uri = "tcp://10.16.181.120:9001/?keep";
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

    private void handlePlayerCommands(SequentialSpace playerCommands) {
        while (true) {
            try {
                // Getting the command from the player
                Object[] command = playerCommands.get(new FormalField(Integer.class), new FormalField(String.class),
                        new FormalField(Integer.class), new FormalField(Integer.class));
                // Parsing it
                int playerID = (int) command[0];
                String direction = (String) command[1];
                int xCor = (int) command[2];
                int yCor = (int) command[3];
                System.out.println("New command from player" + playerID + ":" + "\n " + "    Direction: " + direction
                        + "\n" + "    Coordinates: " + xCor + ", " + yCor);
                // Running move
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


        //fixme here:
        // - Functionality for collisions
        // - get method should not just check actualfield (-1)
        // - should check as formalfield and perform the correct action
        try {
            System.out.println("Updating the game state..");
            /*
            Object[] NewState = gameState.get(new ActualField(newXCor), new ActualField(newYCor), new FormalField(Integer.class));
            if (isCollision()){
                if (foodCollision){
                }
                if (snakeCollision){
                endGame();
                //todo end game for player, end game if only one player left:
                }

            }
            gameState.put(newXCor, newYCor, playerID);
            System.out.println("Sending new coordinates to client..");
            System.out.println("---------");
            playerOneOutput.put(newXCor, newYCor,isAlive);
             */
            System.out.println("before getting new state");
            Object[] NewState = gameState.get(new ActualField(newXCor), new ActualField(newYCor), new FormalField(Integer.class));
            //Object[] NewState = gameState.get(new ActualField(newXCor), new ActualField(newYCor), new ActualField(-1));
            gameState.put(newXCor, newYCor, playerID);
            System.out.println("Sending new coordinates to client..");
            playerOneOutput.put(newXCor, newYCor,isCollision((Integer) NewState[2]));
        } catch (InterruptedException e) {
            System.out.println(e);
        }

    }

    private boolean isCollision(int x){
        return x != -1;
    }
}







