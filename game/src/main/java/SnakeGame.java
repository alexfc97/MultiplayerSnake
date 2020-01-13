import javax.swing.JFrame;
import java.util.Scanner;

public class SnakeGame extends JFrame {
    Board board;
    OtherBoard oboard;
    Scanner input = new Scanner(System.in);

    public SnakeGame() {
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    public static void main(String[] args) throws InterruptedException {
        SnakeGame game = new SnakeGame();
        game.start();

    }

    public void start() throws InterruptedException {
        String myString = input.next();
        System.out.println("-1");
        if(myString.equals("One"))
        {
            board = new Board();
            add(board);
            board.start();
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }
        else if(myString.equals("Two")){
            System.out.println("0");
            oboard = new OtherBoard();
            add(oboard);
            System.out.println("1");
            oboard.start();
            System.out.println("2");
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }

    }

}