import java.awt.Color;
import java.awt.Graphics;

public class SnakeBodyPart {
    public int xCor, yCor, width, height;

    public SnakeBodyPart(int xCor, int yCor, int tileSize) {
        this.xCor = xCor;
        this.yCor = yCor;
        this.width = tileSize;
        this.height = tileSize;
    }

    public void update() {
    }

    public void draw(Graphics g, int playerID) {
        g.setColor(Color.BLACK);
        if (playerID == 1){
            g.fillRect(xCor*width, yCor*height, width, height);
            g.setColor(Color.GREEN);
            g.fillRect(xCor*width + 2, yCor*height + 2, width - 4, height - 4);
        } else if (playerID == 2){
            g.fillRect(xCor*width, yCor*height, width, height);
            g.setColor(Color.BLUE);
            g.fillRect(xCor*width + 2, yCor*height + 2, width - 4, height - 4);
        } else if (playerID == 3){
            g.fillRect(xCor*width, yCor*height, width, height);
            g.setColor(Color.RED);
            g.fillRect(xCor*width + 2, yCor*height + 2, width - 4, height - 4);
        } else if (playerID == 4){
            g.fillRect(xCor*width, yCor*height, width, height);
            g.setColor(Color.MAGENTA);
            g.fillRect(xCor*width + 2, yCor*height + 2, width - 4, height - 4);
        }

    }
}