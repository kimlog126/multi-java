import javax.swing.*;
import java.awt.*;

public class SpaceInvadersGame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public SpaceInvadersGame() {
        setTitle("Let's play Space Invaders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        GameScreen gameScreen = new GameScreen();

        mainPanel.add(new StartScreen(this), "StartScreen");
        mainPanel.add(gameScreen, "GameScreen");

        add(mainPanel);
        setVisible(true);
    }

    public void switchToScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);

        if (screenName.equals("GameScreen")) {
            SwingUtilities.invokeLater(() -> mainPanel.getComponent(1).requestFocusInWindow());
        }
    }

    public static void main(String[] args) {
        new SpaceInvadersGame();
    }
}
