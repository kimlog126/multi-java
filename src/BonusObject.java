import java.awt.*;
import javax.swing.*;

public class BonusObject extends GameObject {
    private int speed;
    private int width;
    private int height;

    public BonusObject(int posX, int posY, int width, int height, String imagePath) {
        super(posX, posY, new ImageIcon(imagePath).getImage());
        this.width = width;
        this.height = height;
        this.speed = 3;
    }

    @Override
    public void updatePosition() {
        posX += speed;
    }

    public boolean isOutOfScreen(int screenWidth) {
        return posX > screenWidth;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(posX, posY, width, height);
    }

    public void draw(Graphics g) {
        g.drawImage(image, posX, posY, width, height, null);
    }
}
