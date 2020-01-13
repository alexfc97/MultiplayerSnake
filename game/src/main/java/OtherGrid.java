import java.awt.Graphics;
import java.awt.Color;


public class OtherGrid{
    private OtherBoard board;

    public OtherGrid(OtherBoard board){
        this.board = board;
    }

    public void draw(Graphics g){
        // g.clearRect(0, 0, board.WIDTH, board.HEIGHT);
        g.setColor(new Color(10, 50, 0));
        g.fillRect(0, 0, Board.WIDTH, Board.HEIGHT);
        g.setColor(Color.BLACK);
        // Drawing a grid with 10x10 pixel cells
        for (int i = 0; i < Board.WIDTH / Board.TILESIZE; i++) {
            g.drawLine(i * Board.TILESIZE, 0, i * Board.TILESIZE, Board.HEIGHT);
        }
        for (int i = 0; i < Board.WIDTH / Board.TILESIZE; i++) {
            g.drawLine(0, i * Board.TILESIZE, Board.WIDTH, i * Board.TILESIZE);
        }

    }

}