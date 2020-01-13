import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import javax.swing.*;
import java.awt.Graphics;

import java.awt.event.KeyEvent;

public class Snake extends JFrame {
    private ArrayList<SnakeBodyPart> snakeBody;
    private int playerID, xCorHead, yCorHead, length;
    private boolean up, down, left, right = false;
    private RemoteSpace gameState, playerOneInput, playerOneOutput;

    public Snake(int playerID, int initXCorHead, int initYCorHead, int initLength){
        this.playerID = playerID;
        this.snakeBody = new ArrayList<SnakeBodyPart>();
        this.xCorHead = initXCorHead;
        this.yCorHead = initYCorHead;
        this.length = initLength;
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Who is running the program? (alex, ali, or marius)");
            String uri = input.readLine();
            if (uri.equals("alex")) {
                gameState = new RemoteSpace("tcp://10.16.189.126:9001/gameState?keep");
                playerOneInput = new RemoteSpace("tcp://10.16.189.126:9001/playerOneInput?keep");
                playerOneOutput = new RemoteSpace("tcp://10.16.189.126:9001/playerOneOutput?keep");
            } else if (uri.equals("ali")) {
                gameState = new RemoteSpace("tcp://127.0.0.1:9001/gameState?keep");
                playerOneInput = new RemoteSpace("tcp://127.0.0.1:9001/playerOneInput?keep");
                playerOneOutput = new RemoteSpace("tcp://127.0.0.1:9001/playerOneOutput?keep");
            } else if (uri.equals("marius")) {
                gameState = new RemoteSpace("tcp://10.16.181.120:9001/gameState?keep");
                playerOneInput = new RemoteSpace("tcp://10.16.181.120:9001/playerOneInput?keep");
                playerOneOutput = new RemoteSpace("tcp://10.16.181.120:9001/playerOneOutput?keep");
            } else {
                System.out.println("Not a valid IP configuration");
            }
        }catch (Exception e) {
            System.out.println(e);
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
                Object[] newCor = playerOneOutput.getp(new FormalField(Integer.class), new FormalField(Integer.class), new FormalField(Boolean.class));
                if (newCor != null) {
                    System.out.println((boolean) newCor[2]);
                    if ((boolean) newCor[2]) {
                        endGame();
                    } else {
                         int newXCor = (int) newCor[0];
                         int newYCor = (int) newCor[1];
                         System.out.println("Client received the new coordinates: " + newXCor + ", " + newYCor);
                         xCorHead = newXCor;
                         yCorHead = newYCor;
                         // Adding a new snake body part
                         snakeBody.add(new SnakeBodyPart(xCorHead, yCorHead, Board.TILESIZE));
                         // Adjusting the length
                         if (snakeBody.size() > length) {
                             SnakeBodyPart extraPart = snakeBody.get(0);
                             gameState.get(new ActualField(extraPart.xCor), new ActualField(extraPart.yCor),
                                     new FormalField(Integer.class));
                             gameState.put(extraPart.xCor, extraPart.yCor, -1);

                             snakeBody.remove(0);
                         }
                    }
                 }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void draw(Graphics g) {
        for (SnakeBodyPart snakeBodyPart : snakeBody) {
            snakeBodyPart.draw(g, playerID);
        }
    }
    public static void endGame() {
        JOptionPane.showMessageDialog(null, "Game Over","GAME OVER", JOptionPane.ERROR_MESSAGE);
        System.exit(ABORT);
    }
}