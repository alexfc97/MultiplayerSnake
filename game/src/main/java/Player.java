import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics;

public class Player {
    private int playerID;
    RemoteSpace gameState, playerInput, lobby, IDs;
    private boolean up, down, left, right = false;

    public Player() {
        connect();
    }

    private void connect() {

        try {
            lobby = new RemoteSpace("tcp://localhost:9001/lobby?keep");
            IDs = new RemoteSpace("tcp://localhost:9001/IDs?keep");
            gameState = new RemoteSpace("tcp://localhost:9001/gameState?keep");
            System.out.println("Client has created remote repos");
            lobby.put("connected");
            Object[] t = IDs.get(new FormalField(Integer.class));
            System.out.println("Player ID:  " + t.toString());
            playerID = (int) t[0];
            String spaceName = "player_" + playerID + "_input";
            playerInput = new RemoteSpace("tcp://localhost:9001/" + spaceName + "?keep");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try {
            if (right) {
                playerInput.put(playerID, "right");
            }

            if (left) {
                playerInput.put(playerID, "left");
            }

            if (up) {
                playerInput.put(playerID, "up");
            }

            if (down) {
                playerInput.put(playerID, "down");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void pressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_RIGHT && !left) {
            up = false;
            down = false;
            right = true;
        }

        if (key == KeyEvent.VK_LEFT && !right) {
            up = false;
            down = false;
            left = true;
        }

        if (key == KeyEvent.VK_UP && !down) {
            left = false;
            right = false;
            up = true;
        }

        if (key == KeyEvent.VK_DOWN && !up) {
            left = false;
            right = false;
            down = true;
        }

    }

    public void draw(Graphics g) {
        ArrayList<SnakeBodyPart> allBodyParts = getAllBodyParts();
        for (SnakeBodyPart bodyPart : allBodyParts) {
            bodyPart.draw(g);
        }

    }

    public ArrayList<SnakeBodyPart> getAllBodyParts() {
        try {
            ArrayList<SnakeBodyPart> allBodyParts = new ArrayList<SnakeBodyPart>(0);
            List<Object[]> response = gameState.queryAll(new FormalField(Integer.class), new FormalField(Integer.class),
                    new FormalField(Integer.class));

            System.out.println(response.size());
            for (Object[] obj : response) {
                System.out.println("In loop");
                int xCor = (int) obj[0];
                int yCor = (int) obj[1];
                int ID = (int) obj[2];
                System.out.println("Response from server: X: " + xCor + " Y: " + yCor + "ID: " + ID);
                allBodyParts.add(new SnakeBodyPart(xCor, yCor, ID, Board.TILESIZE));
            }
            return allBodyParts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}