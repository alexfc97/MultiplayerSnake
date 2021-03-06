package Client;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import Shared.Board;
import Shared.Food;
import Shared.SnakeBodyPart;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics;

public class Player {
    private int playerID;
    RemoteSpace gameState, playerInput, lobby, IDs, isAlive;
    private boolean up, down, left, right = false;
    private String gateIP;

    public Player() {
        connect();
    }

    private void connect() {
        getIP();

        try {
            lobby = new RemoteSpace("tcp://" + gateIP + "/lobby?keep");
            IDs = new RemoteSpace("tcp://" + gateIP + "/IDs?keep");
            gameState = new RemoteSpace("tcp://" + gateIP + "/gameState?keep");
            isAlive = new RemoteSpace("tcp://" + gateIP + "/isAlive?keep");
            System.out.println("Client has created remote repos");
            lobby.put("connected");
            Object[] t = IDs.get(new FormalField(Integer.class));
            System.out.println("Player ID:  " + t.toString());
            playerID = (int) t[0];
            String spaceName = "player_" + playerID + "_input";
            playerInput = new RemoteSpace("tcp://" + gateIP + "/" + spaceName + "?keep");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getIP() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the IP address of the server. Press enter for localhost:9001: ");
            String ip = input.readLine();
            if (ip.isEmpty()) {
                ip = "localhost:9001";

                this.gateIP = ip;

            }
            this.gateIP = ip;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void update() {

        try {
            Object[] status = isAlive.query(new ActualField(playerID), new FormalField(Boolean.class),
                    new FormalField(Boolean.class));
            if ((boolean) status[1] == false) {
                GameOver popup = new GameOver("lost");
                System.out.println("Im dead lol");
            } else if ((boolean) status[2] == true) {
                GameOver popup = new GameOver("WON");
                System.out.println("I WONN!");
            }

            if (right) {
                playerInput.put(playerID, "right");
            }

            else if (left) {
                playerInput.put(playerID, "left");
            }

            else if (up) {
                playerInput.put(playerID, "up");
            }

            else if (down) {
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

    public ArrayList<Food> getAllFood() {
        try {
            ArrayList<Food> allfoods = new ArrayList<Food>(0);
            List<Object[]> response = gameState.queryAll(new FormalField(Integer.class), new FormalField(Integer.class),
                    new FormalField(Boolean.class), new ActualField(true));
            for (Object[] obj : response) {
                int foodXCor = (int) obj[0];
                int foodYCor = (int) obj[1];

                allfoods.add(new Food(foodXCor, foodYCor));
            }
            return allfoods;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public ArrayList<SnakeBodyPart> getAllBodyParts() {
        try {
            ArrayList<SnakeBodyPart> allBodyParts = new ArrayList<SnakeBodyPart>(0);

            List<Object[]> response = gameState.queryAll(new FormalField(Integer.class), new FormalField(Integer.class),
                    new FormalField(Integer.class), new FormalField(Boolean.class));
            for (Object[] obj : response) {
                int xCor = (int) obj[0];
                int yCor = (int) obj[1];
                int ID = (int) obj[2];
                if (ID == playerID) {
                    // System.out.println("Response from server: X: " + xCor + " Y: " + yCor + " ID:
                    // " + ID);
                }
                allBodyParts.add(new SnakeBodyPart(xCor, yCor, ID, Board.TILESIZE));
            }
            return allBodyParts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void draw(Graphics g) {
        ArrayList<SnakeBodyPart> allBodyParts = getAllBodyParts();
        ArrayList<Food> allfoods = getAllFood();
        for (SnakeBodyPart bodyPart : allBodyParts) {
            bodyPart.draw(g, playerID);
        }

        for (Food food : allfoods) {
            food.draw(g);
        }

    }

}