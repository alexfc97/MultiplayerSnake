import java.util.ArrayList;

import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.awt.Graphics;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.UnknownHostException;

public class SnakeOpp {
    public ArrayList<SnakeBodyPart> snakeBody;
    public int xCorHead, yCorHead, length;
    RemoteSpace space;

    public SnakeOpp(int initXCorHead, int initYCorHead, int initLength) {
        this.snakeBody = new ArrayList<SnakeBodyPart>();
        this.xCorHead = initXCorHead;
        this.yCorHead = initYCorHead;
        this.length = initLength;
        try {
            space = new RemoteSpace("tcp://10.16.189.126:9001/chat?keep");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void update() {
        if (snakeBody.size() == 0) {
            snakeBody.add(new SnakeBodyPart(xCorHead, yCorHead, 10));
        }

        try {
            Object[] oppCor = space.getp(new FormalField(Integer.class), new FormalField(Integer.class));
            if(oppCor != null){
            snakeBody.add(new SnakeBodyPart((int) oppCor[0], (int) oppCor[1], 10));
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (snakeBody.size() > length) {
            snakeBody.remove(0);
        }

    }

    public void draw(Graphics g) {
        for (int i = 0; i < snakeBody.size(); i++) {
            snakeBody.get(i).draw(g);
        }
    }

}