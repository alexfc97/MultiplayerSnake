package Client;

import javax.swing.*;

public class GameOver extends JFrame {
    public GameOver(String message) {
        if (message == "WON") {
            JOptionPane.showMessageDialog(null, "You won, great job!", "WINNER WINNER", JOptionPane.ERROR_MESSAGE);
            System.exit(ABORT);
        } else {
            JOptionPane.showMessageDialog(null, "Game Over", "GAME OVER", JOptionPane.ERROR_MESSAGE);
            System.exit(ABORT);
        }
    }
}