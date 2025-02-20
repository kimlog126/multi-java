import java.awt.*;

public abstract class GameObject {
    protected int posX;
    protected int posY;
    protected Image image;

    public GameObject(int posX, int posY, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.image = image;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Rectangle getBounds() {
        return new Rectangle(posX, posY, 20, 20);
    }

    public void updatePosition() {
    }
}
