import java.awt.*;
import javax.swing.*;

public class EnemyObject extends GameObject {
    private int width;
    private int height;

    public EnemyObject(int posX, int posY, int width, int height, String imagePath) {
        super(posX, posY, new ImageIcon(imagePath).getImage());
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.drawImage(image, posX, posY, width, height, null);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


}
