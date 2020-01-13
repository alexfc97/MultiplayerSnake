import java.util.ArrayList;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import java.util.Scanner;


import java.awt.Graphics;

import java.awt.event.KeyEvent;

public class Snake {
    private ArrayList<SnakeBodyPart> snakeBody;
    private int playerID, xCorHead, yCorHead, length;
    private boolean up, down, left, right = false;
    private RemoteSpace gameState, playerOneInput, playerOneOutput, Chat;
    Scanner input = new Scanner(System.in);

    public Snake(int playerID, int initXCorHead, int initYCorHead, int initLength) {
        this.playerID = playerID;
        this.snakeBody = new ArrayList<SnakeBodyPart>();
        this.xCorHead = initXCorHead;
        this.yCorHead = initYCorHead;
        this.length = initLength;

        System.out.println("type in your player id in words");
        String myString = input.next();


        try {
            gameState = new RemoteSpace("tcp://10.16.81.120:9001/gameState?keep");
            playerOneInput = new RemoteSpace("tcp://10.16.81.120:9001/"+ myString + "Input?keep");
            playerOneOutput = new RemoteSpace("tcp://10.16.81.120:9001/" + myString + "Output?keep");
            Chat = new RemoteSpace("tcp://10.16.81.120:9001/Chat?keep");
            Chat.put(myString);
            System.out.println(myString);
        } catch (Exception e) {
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

    public void update() {
        if (snakeBody.size() == 0) {
            snakeBody.add(new SnakeBodyPart(xCorHead, yCorHead, 10));
        }

        try {
            if (right) {
                playerOneInput.put(playerID, "right", xCorHead, yCorHead);
            }

            if (left) {
                playerOneInput.put(playerID, "left", xCorHead, yCorHead);
            }

            if (up) {
                playerOneInput.put(playerID, "up", xCorHead, yCorHead);
            }

            if (down) {
                playerOneInput.put(playerID, "down", xCorHead, yCorHead);
            }
            // Getting the new coordinate
            Object[] newCor = playerOneOutput.getp(new FormalField(Integer.class), new FormalField(Integer.class));
            if (newCor != null) {
                int newXCor = (int) newCor[0];
                int newYCor = (int) newCor[1];
                //System.out.println("Client recevied the new coordinates: " + newXCor + ", " + newYCor);
                xCorHead = newXCor;
                yCorHead = newYCor;
                // Adding a new snake body part
                snakeBody.add(new SnakeBodyPart(xCorHead, yCorHead, Board.TILESIZE));
                // Adjusting the length
                if (snakeBody.size() > length) {
                    SnakeBodyPart extraPart = snakeBody.get(0);
                    //System.out.println("before gamestate get in snake");
                    gameState.get(new ActualField(extraPart.xCor), new ActualField(extraPart.yCor),
                            new FormalField(Integer.class));
                    gameState.put(extraPart.xCor, extraPart.yCor, -1);

                    snakeBody.remove(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void draw(Graphics g) {
        for (int i = 0; i < snakeBody.size(); i++) {
            snakeBody.get(i).draw(g);
        }
    }

}