import java.awt.Color;
import java.awt.Graphics;

public class SnakeBodyPart {
    private int xCor, yCor, width, height;

    public SnakeBodyPart(int xCor, int yCor, int tileSize) {
        this.xCor = xCor;
        this.yCor = yCor;
        this.width = tileSize;
        this.height = tileSize;
    }

    public void update() {

    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK); 
        g.fillRect(xCor*width, yCor*height, width, height); 
        g.setColor(Color.GREEN);
        g.fillRect(xCor*width + 2, yCor*height + 2, width - 4, height - 4); 
    }
}