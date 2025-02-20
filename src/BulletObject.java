import java.awt.*;
import javax.swing.*;

public class BulletObject extends GameObject {
    private int speed;
    private int width;
    private int height;

    public BulletObject(int posX, int posY, int width, int height, String imagePath) {
        super(posX, posY, new ImageIcon(imagePath).getImage());
        this.width = width;
        this.height = height;
        this.speed = 10;
    }

    public void move() {
        posY -= speed;
    }

    public boolean isOutOfScreen() {
        return posY < 0;
    }

    @Override
    public void updatePosition() {
        move();
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(posX, posY, width, height);
    }

    public void draw(Graphics g) {
        g.drawImage(image, posX, posY, width, height, null);
    }
}
