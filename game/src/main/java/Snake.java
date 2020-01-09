import java.util.ArrayList;

import java.awt.Graphics;

import java.awt.event.KeyEvent;

public class Snake {
    public ArrayList<SnakeBodyPart> snakeBody;
    public int xCorHead, yCorHead, length;
    public boolean up, down, left, right = false;

    public Snake(int initXCorHead, int initYCorHead, int initLength) {
        this.snakeBody = new ArrayList<SnakeBodyPart>();
        this.xCorHead = initXCorHead;
        this.yCorHead = initYCorHead;
        this.length = initLength;
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

        if (right) {
            xCorHead = (xCorHead + 1) % (Board.WIDTH/10);
        }

        if (left) {
            xCorHead--;
            if (xCorHead < 0) {
                xCorHead = (xCorHead + Board.WIDTH/10);
            }
        }

        if (up) {
            yCorHead--;
            if (yCorHead < 0) {
                yCorHead = (yCorHead + (Board.HEIGHT/10));
            }

        }

        if (down) {
            yCorHead = (yCorHead + 1) % (Board.HEIGHT/10);
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

}