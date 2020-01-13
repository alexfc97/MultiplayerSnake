import javax.swing.*;

public class SnakeGame extends JFrame {
    Board board;

    public SnakeGame() {
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    public static void main(String[] args) {
        SnakeGame game = new SnakeGame();
        game.start();
    }

    public void start() {
        board = new Board();
        add(board);
        board.start();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}