import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import org.jspace.RemoteSpace;

import java.awt.Dimension;

public class Board extends JPanel implements ActionListener, KeyListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final int TILESIZE = 10;

    Grid grid;
    Timer timer;
    Snake playerOneSnake;
    OtherSnake playerTwoSnake;


    public Board() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        timer = new Timer(50, this);
        addKeyListener(this);
        setFocusable(true);
        grid = new Grid(this);
        playerOneSnake = new Snake(1,10, 10, TILESIZE);
        playerTwoSnake = new OtherSnake(2, 60, 60, TILESIZE);
    }

    public void start() {
        timer.start();
    }

    // Runs every timer tick
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            update();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        repaint();
    }

    public void update() throws InterruptedException {
        playerOneSnake.update();
        //playerTwoSnake.update();

    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        grid.draw(g);
        playerOneSnake.draw(g);
        //playerTwoSnake.draw(g);



    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        playerOneSnake.pressed(e);
        //playerTwoSnake.pressed(e);

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

}