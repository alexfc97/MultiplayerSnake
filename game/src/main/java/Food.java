import java.awt.*;

public class Food {
    public int xCor, yCor;
    public int width, height;
    public int max = 80,min=0;

    public Food(int tileSize){
            this.xCor = random();
            this.yCor = random();
            this.width = tileSize;
            this.height = tileSize;
    }

    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(xCor*width, yCor*height, width, height);
        g.setColor(Color.GREEN);
        g.fillRect(xCor*width + 2, yCor*height + 2, width - 4, height - 4);
    }

    public int random() {
        double randomDouble = Math.random();
        randomDouble = randomDouble * 80;
        int randomInt = (int) randomDouble;
        return randomInt;
    }
}
