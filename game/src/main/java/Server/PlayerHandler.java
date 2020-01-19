package Server;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;

public class PlayerHandler extends Thread {
    private SequentialSpace space;

    public PlayerHandler(SequentialSpace space) {
        this.space = space;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Getting the command from the player
                Object[] command = space.get(new FormalField(Integer.class), new FormalField(String.class));
                // Parsing it
                int playerID = (int) command[0];
                String direction = (String) command[1];
                System.out.println(
                        "New command from player" + playerID + ":" + "\n " + "    Direction: " + direction + "\n");
                // Running move
                Server.move(playerID, direction, Server.snakeMap.get(playerID).xCorHead,
                        Server.snakeMap.get(playerID).yCorHead);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
