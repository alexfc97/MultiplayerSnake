import java.util.ArrayList;
import java.util.List;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.awt.Graphics;

import java.awt.event.KeyEvent;

public class Snake {
    private ArrayList<SnakeBodyPart> snakeBody;
    private int playerID, xCorHead, yCorHead, length;
    private boolean up, down, left, right = false;
    private RemoteSpace gameState, playerOneInput, playerOneOutput;

    public Snake(int playerID, int initXCorHead, int initYCorHead, int initLength) {
        this.playerID = playerID;
        this.snakeBody = new ArrayList<SnakeBodyPart>();
        this.xCorHead = initXCorHead;
        this.yCorHead = initYCorHead;
        this.length = initLength;
        try {
            gameState = new RemoteSpace("tcp://127.0.0.1:9001/gameState?keep");
            playerOneInput = new RemoteSpace("tcp://127.0.0.1:9001/playerOneInput?keep");
            playerOneOutput = new RemoteSpace("tcp://127.0.0.1:9001/playerOneOutput?keep");
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

            Object[] newCor = playerOneOutput.getp(new FormalField(Integer.class), new FormalField(Integer.class));
            if (newCor != null) {
                int newXCor = (int) newCor[0];
                int newYCor = (int) newCor[1];
                System.out.println("Client recevied the new coordinates: " + newXCor + ", " + newYCor);
                xCorHead = newXCor;
                yCorHead = newYCor;
                snakeBody.add(new SnakeBodyPart(xCorHead, yCorHead, Board.TILESIZE));
                if (snakeBody.size() > length) {
                    SnakeBodyPart extraPart = snakeBody.get(0);
                    gameState.get(new ActualField(extraPart.xCor), new ActualField(extraPart.yCor), new FormalField(Integer.class));
                    gameState.put(extraPart.xCor, extraPart.yCor, -1);

                    snakeBody.remove(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // private ArrayList<SnakeBodyPart> gameStateToSnake(int i) {
    // // Get all parts
    // // add them to the list
    // // Make length checks
    // ArrayList<SnakeBodyPart> newSnakeBody = new ArrayList<SnakeBodyPart>();

    // try {
    // List<Object[]> allBodyParts = new ArrayList<>();
    // allBodyParts = gameState.queryAll(new FormalField(Integer.class), new
    // FormalField(Integer.class),
    // new ActualField(i));
    // for (Object[] bodyPart : allBodyParts) {
    // newSnakeBody.add(new SnakeBodyPart((int) bodyPart[0], (int) bodyPart[1],
    // Board.TILESIZE));
    // }
    // if (snakeBody.size() == 0){
    // newSnakeBody.add(new SnakeBodyPart(10,10,10));
    // }
    // if (snakeBody.size() > length) {
    // newSnakeBody.remove(0);
    // }

    // return newSnakeBody;
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return newSnakeBody;

    // }

    public void draw(Graphics g) {
        for (int i = 0; i < snakeBody.size(); i++) {
            snakeBody.get(i).draw(g);
        }
    }

}