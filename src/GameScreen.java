import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {
    private GameEngine engine;
    private JPanel gameArea;
    private JPanel infoPanel;
    private JLabel scoreLabel;
    private JLabel topScoreLabel;

    private Thread gameThread;
    private boolean running;

    public GameScreen() {
        setLayout(new BorderLayout());
        engine = new GameEngine();

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(engine);

        gameArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.BLACK);

                engine.getPlayer().draw(g);

                for (EnemyObject enemy : engine.getEnemies()) {
                    enemy.draw(g);
                }

                for (BulletObject bullet : engine.getBullets()) {
                    bullet.draw(g);
                }

                for (EnemyBulletObject bullet : engine.getEnemyBullets()) {
                    bullet.draw(g);
                }

                for (BonusObject bonus : engine.getBonusObjects()) {
                    bonus.draw(g);
                }

            }
        };
        gameArea.setLayout(null);
        add(gameArea, BorderLayout.CENTER);

        createInfoPanel();
        add(infoPanel, BorderLayout.EAST);

        startGameThread();
    }

    private void createInfoPanel() {
        infoPanel = new JPanel();
        infoPanel.setBackground(Color.BLUE);
        infoPanel.setPreferredSize(new Dimension(200, 600));
        infoPanel.setLayout(new BorderLayout());

        scoreLabel = new JLabel("SCORE: " + engine.getScore());
        topScoreLabel = new JLabel("TOP SCORE: " + engine.getTopScore());

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setForeground(Color.WHITE);
        topScoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topScoreLabel.setForeground(Color.WHITE);

        JPanel labelsPanel = new JPanel(new GridLayout(2, 1));
        labelsPanel.setOpaque(false);
        labelsPanel.add(scoreLabel);
        labelsPanel.add(topScoreLabel);

        infoPanel.add(labelsPanel, BorderLayout.NORTH);


        ImageIcon originalIcon = new ImageIcon("images/Background.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(300, 150, Image.SCALE_SMOOTH);

        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JLabel gameImage = new JLabel(resizedIcon);
        gameImage.setHorizontalAlignment(SwingConstants.CENTER);
        infoPanel.add(gameImage, BorderLayout.CENTER);
    }

    private void startGameThread() {
        running = true;
        gameThread = new Thread(() -> run());
        gameThread.start();
    }


    public void run() {
        while (running) {
            try {
                engine.moveBullets();

                engine.moveEnemies();
                engine.fireEnemyBullets(engine.getEnemies());
                engine.moveEnemyBullets();

                engine.spawnBonusObject();
                engine.moveBonusObjects();

                engine.checkCollisions();
                engine.checkPlayerHit();
                engine.checkEnemyPosition();

                engine.checkWinCondition();

                scoreLabel.setText("SCORE: " + engine.getScore());
                topScoreLabel.setText("TOP SCORE: " + engine.getTopScore());

                repaint();

                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
