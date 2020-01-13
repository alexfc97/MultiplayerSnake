import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import org.jspace.RemoteSpace;

import java.awt.Dimension;

public class OtherBoard extends JPanel {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final int TILESIZE = 10;

    OtherGrid grid;
    Timer timer;
    Snake playerOneSnake;
    OtherSnake playerTwoSnake;


    public OtherBoard() throws InterruptedException {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        //timer = new Timer(50, this);
        //addKeyListener(this);
        setFocusable(true);
        grid = new OtherGrid(this);
        playerTwoSnake = new OtherSnake(1,10, 10, TILESIZE);
        update();
        repaint();
    }

    public void start() {
        timer.start();
    }



    public void update() throws InterruptedException {
        playerTwoSnake.update();
        //playerTwoSnake.update();

    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        grid.draw(g);
        playerTwoSnake.draw(g);
        //playerTwoSnake.draw(g);



    }


}