import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

public class Server {
    private SpaceRepository repository;
    private SequentialSpace gameState, playerOneCommands;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    private void start() {
        repository = createRepo();
        addGate(repository);
        createSpaceMatrix();
        handlePlayerCommands(playerOneCommands);
    }

    private void handlePlayerCommands(SequentialSpace playerCommands) {
        while (true) {
            try {
                Object[] command = playerCommands.get(new ActualField(1), new FormalField(String.class),
                        new FormalField(Integer.class), new FormalField(Integer.class));
                String playerID = (String) command[0];
                String direction = (String) command[1];
                int xCor = (int) command[2];
                int yCor = (int) command[3];
                move(playerID, direction, xCor, yCor);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void move(String playerID, String direction, int xCor, int yCor) {
        int newXCor = xCor;
        int newYCor = yCor;

        if (direction.equals("right")) {
            newXCor = (xCor + 1) % (Board.WIDTH / 10);
        }

        if (direction.equals("left")) {
            newXCor = xCor--;
            if (newXCor < 0) {
                newXCor = (newXCor + Board.WIDTH / 10);
            }
        }

        if (direction.equals("up")) {
            newYCor = yCor--;
            if (newYCor < 0) {
                newYCor = (newYCor + (Board.HEIGHT / 10));
            }

        }

        if (direction.equals("down")) {
            newYCor = (yCor + 1) % (Board.HEIGHT / 10);
        }

        try{
        gameState.get(new ActualField(newXCor), new ActualField(newYCor), new ActualField(-1));
        gameState.put(new ActualField(newXCor), new ActualField(newYCor), new ActualField(playerID));
        } catch(InterruptedException e){
            e.printStackTrace();
        }


    }

    private SpaceRepository createRepo() {
        SpaceRepository repository = new SpaceRepository();
        gameState = new SequentialSpace();
        playerOneCommands = new SequentialSpace();
        repository.add("gameState", gameState);
        repository.add("playerOneCommands", playerOneCommands);
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