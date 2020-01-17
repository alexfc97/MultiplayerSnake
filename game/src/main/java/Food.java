import java.awt.*;
import java.util.Random;

public class Food {
    public int xCor,yCor,height,width;

    public int getxCor() {
        return xCor;
    }

    public int getyCor() {
        return yCor;
    }

    public Food(int xCor , int yCor){
        this.xCor = xCor;
        this.yCor = yCor;
        this.height = Board.TILESIZE;
        this.width = Board.TILESIZE;
    }

    /*
    public boolean isFood(int x, int y) {
        return x == xCor && y == yCor;
    }
     */

    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(xCor * width, yCor * height, width, height);
        g.setColor(Color.WHITE);
        g.fillRect(xCor * width + 2, yCor * height + 2, width - 4, height - 4);
    }
}