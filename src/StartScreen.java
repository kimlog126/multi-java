import javax.swing.*;
import java.awt.*;

public class StartScreen extends JPanel {
    public StartScreen(SpaceInvadersGame gameFrame) {
        setLayout(new BorderLayout());

        ImageIcon background = new ImageIcon("images/Background.png");
        JLabel imageLabel = new JLabel(background);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(imageLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setPreferredSize(new Dimension(200, 50));
        startButton.addActionListener(e -> gameFrame.switchToScreen("GameScreen"));
        add(startButton, BorderLayout.SOUTH);
    }
}
