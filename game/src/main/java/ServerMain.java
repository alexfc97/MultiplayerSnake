import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;
import org.jspace.Tuple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class ServerMain {
    private static SequentialSpace game;
    private static SequentialSpace Commands;
    private int Players;
    private Object[] command;

    public static void main(String[] args) {
        ServerMain game = new ServerMain();
        game.Start();
        game.game();
    }

    public ServerMain(){
    }

    private void Start() {
        Connection();
        matrixInit();
    }

    private void game() {
        try {
            command = Commands.get(new FormalField(String.class), new FormalField(Integer.class), new FormalField(Integer.class));
        }catch (Exception e){
            System.out.println(e);
        }
        move((String) command[0], (Integer) command[1], (Integer) command[2]);
    }

    private Tuple move(String direction, int x, int y){
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


        return new Tuple(xCorHead,yCorHead);
    }
    private void matrixInit() {
        for (int i = 0; i <= 79 ;i++){
            for (int j = 0; j <=79;j++){
                try {
                    game.put(i, j, false);
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
            Commands = new SequentialSpace();

            // Add the space to the repository
            repository.add("game", game);

            // Set the URI of the game
            System.out.print("Enter URI of the chat server or press enter for default: ");
            String uri = input.readLine();
            // Default value
            if (uri.isEmpty()) {
                uri = "tcp://10.16.189.126:9001/?keep";
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
