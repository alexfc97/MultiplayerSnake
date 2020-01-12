import java.util.ArrayList;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.awt.Graphics;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

public class Snake {
    public ArrayList<SnakeBodyPart> snakeBody;
    public int xCorHead, yCorHead, length;
    public boolean up, down, left, right = false;
    private RemoteSpace commands,game;

    public Snake(int initXCorHead, int initYCorHead, int initLength) {
        this.snakeBody = new ArrayList<SnakeBodyPart>();
        this.xCorHead = initXCorHead;
        this.yCorHead = initYCorHead;
        this.length = initLength;
        try {
            commands = new RemoteSpace("tcp://10.16.189.126:9001/commands?keep");
            game = new RemoteSpace("tcp://10.16.189.126:9001/game?keep");
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


        if (right) {
            try {
                commands.put("right", xCorHead, yCorHead);
            } catch (InterruptedException l) {
                l.printStackTrace();
            }
        }

        if (left) {
            try {
                commands.put("left", xCorHead, yCorHead);
            } catch (InterruptedException l) {
                l.printStackTrace();
            }
        }

        if (up) {
            try {
                commands.put("up", xCorHead, yCorHead);
            } catch (InterruptedException l) {
                l.printStackTrace();
            }
        }

        if (down) {
            try {
                commands.put("down", xCorHead, yCorHead);
            } catch (InterruptedException l) {
                l.printStackTrace();
            }
        }

        snakeBody.add(new SnakeBodyPart(xCorHead, yCorHead, 10));
        if (snakeBody.size() > length) {
            snakeBody.remove(0);
        }

    }

    public void draw(Graphics g) {
        for (int i = 0; i < snakeBody.size(); i++) {
            snakeBody.get(i).draw(g);
        }
    }

    public void ClientDraw(Graphics g) {
        try {
            List<Object[]> t = game.queryAll(new FormalField(Integer.class), new FormalField(Integer.class), new ActualField(1));
            for (Object[] query : t) {
                System.out.println(query[0] + " " + query[1] + " " + query[2]);
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }



}