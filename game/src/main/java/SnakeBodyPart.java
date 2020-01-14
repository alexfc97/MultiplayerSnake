import java.awt.Color;
import java.awt.Graphics;

public class SnakeBodyPart {
    public int xCor, yCor, width, height, ID;

    public SnakeBodyPart(int xCor, int yCor, int ID, int tileSize) {
        this.xCor = xCor;
        this.yCor = yCor;
        this.ID = ID;
        this.width = tileSize;
        this.height = tileSize;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(xCor * width, yCor * height, width, height);
        g.setColor(Color.GREEN);
        g.fillRect(xCor * width + 2, yCor * height + 2, width - 4, height - 4);
    }
}