package Shared;

import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import org.jspace.RemoteSpace;

import Client.Grid;
import Client.Player;

public class Board extends JPanel implements ActionListener, KeyListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final int TILESIZE = 10;

    Grid grid;
    Timer timer;
    Player player;

    public Board() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        timer = new Timer(50, this);
        addKeyListener(this);
        setFocusable(true);
        grid = new Grid(this);
        player = new Player();
    }

    public void start() {
        timer.start();
    }

    // Runs every timer tick
    @Override
    public void actionPerformed(final ActionEvent e) {
        update();
        Toolkit.getDefaultToolkit().sync();
        repaint();
    }

    public void update() {
        player.update();

    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        grid.draw(g);
        player.draw(g);

    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        player.pressed(e);

    }

    @Override
    public void keyReleased(final KeyEvent e) {
        // TODO Auto-generated method stub

    }

}