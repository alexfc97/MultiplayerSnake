import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;
import org.jspace.Tuple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class ServerMain {
    private static SequentialSpace game;
    private static SequentialSpace commands;
    private int Players;

    public static void main(String[] args) {
        ServerMain game = new ServerMain();
        game.Start();
        game.game();
    }


    private void Start() {
        Connection();
        matrixInit();
        game();
    }

    private void game() {
        while(true) {
            Object[] command = new Object[0];
            Object[] m = new Object[0];
            try {
                System.out.println("Before get commmand");
                command = commands.get(new FormalField(String.class), new FormalField(Integer.class), new FormalField(Integer.class));
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Before move");
            move((String) command[0], (Integer) command[1], (Integer) command[2]);
            try {
                System.out.println("Before query");
                m = game.query(new FormalField(Integer.class), new FormalField(Integer.class), new ActualField(1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("X: " + (int) m[0] + ", Y: " + (int) m[1] + "ID: " + (int) m[2]);


        }
    }

    private void move(String direction, int x, int y) {
        int xCorHead = 0;
        int yCorHead = 0;

        if (direction.equals("right")) {
            xCorHead = (xCorHead + 1) % (Board.WIDTH/10);
        }

        if (direction.equals("left")) {
            xCorHead--;
            if (xCorHead < 0) {
                xCorHead = (xCorHead + Board.WIDTH/10);
            }
        }

        if (direction.equals("up")) {
            yCorHead--;
            if (yCorHead < 0) {
                yCorHead = (yCorHead + (Board.HEIGHT/10));
            }

        }

        if (direction.equals("down")) {
            yCorHead = (yCorHead + 1) % (Board.HEIGHT/10);
        }

        try{
            System.out.println("before get in move");
            System.out.println("Server X: " + (int) xCorHead  + " Server Y: " + (int) yCorHead);
        game.get(new ActualField (xCorHead), new ActualField(yCorHead), new ActualField(0));
        game.put(xCorHead, yCorHead, 1);
        } catch(InterruptedException e){
            System.out.println(e);
        }

    }

    private void matrixInit() {
        for (int i = 0; i <= 79 ;i++){
            for (int j = 0; j <=79;j++){
                try {
                    game.put(i, j, 0);
                }catch (Exception e){
                    System.out.println(e);}
            }
        }
    }

    private static void Connection() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            // Create a repository
            SpaceRepository repository = new SpaceRepository();

            // Create a local space for the game
            game = new SequentialSpace();
            commands = new SequentialSpace();

            // Add the space to the repository
            repository.add("game", game);
            repository.add("commands", commands);



            // Set the URI of the game
            System.out.print("Enter URI of the chat server or press enter for default: ");
            String uri = input.readLine();
            // Default value
            if (uri.isEmpty()) {
                uri = "tcp://10.16.80.149:9001/?keep";
            }

            // Open a gate
            URI myUri = new URI(uri);
            String gateUri = "tcp://" + myUri.getHost() + ":" + myUri.getPort() + "?keep";
            System.out.println("Opening repository gate at " + gateUri + "...");
            repository.addGate(gateUri);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
