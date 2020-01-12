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
    Snake snake;
    Timer timer;


    RemoteSpace space;

    public Board() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        timer = new Timer(25, this);
        addKeyListener(this);
        setFocusable(true);
        grid = new Grid(this);
        snake = new Snake(10, 10, TILESIZE);

    }

    public void start() {
        timer.start();
    }

    // Runs every timer tick
    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }

    public void update() {
        snake.update();

    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        grid.draw(g);
        snake.draw(g);



    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        snake.pressed(e);

    }

    @Override
    public void keyReleased(KeyEvent e) {


    }

}