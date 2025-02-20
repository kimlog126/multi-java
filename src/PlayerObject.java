import java.awt.*;
import javax.swing.*;

public class PlayerObject extends GameObject {
    private int width;
    private int height;

    public PlayerObject(int posX, int posY, int width, int height, String imagePath) {
        super(posX, posY, new ImageIcon(imagePath).getImage());
        this.width = width;
        this.height = height;
    }


    @Override
    public void updatePosition() {
    }

    public void draw(Graphics g) {
        g.drawImage(image, posX, posY, width, height, null);
    }

    public int getWidth() {
        return width;
    }

}
