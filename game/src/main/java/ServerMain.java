import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.sql.Connection;

public class ServerMain {
    private static SequentialSpace game;

    public static void main(String[] args){
        ServerMain game = new ServerMain();
        game.Start();
    }

    public ServerMain(){
    }

    private void Start() {
        Connection();
        matrixInit();
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
