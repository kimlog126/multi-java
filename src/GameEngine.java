import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.io.*;


public class GameEngine implements KeyListener {
    private PlayerObject player;
    private List<EnemyObject> enemies;
    private List<BulletObject> bullets;
    private List<EnemyBulletObject> enemyBullets;
    private List<BonusObject> bonusObjects;

    private boolean movingRight = true;
    private int speedX = 2;
    private int speedY = 10;

    private int playerSpeed = 10;

    private int score;
    private int topScore;
    private final String topScoreFile = "topscore.txt";

    private boolean gameOver = false;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;


    public GameEngine() {
        player = new PlayerObject(300, 500, 50, 50, "images/Player.png");
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        bonusObjects = new ArrayList<>();
        score = 0;
        topScore = loadTopScore();
        initializeEnemies();
    }

    public PlayerObject getPlayer() {
        return player;
    }

    public List<EnemyObject> getEnemies() {
        return enemies;
    }

    public List<BulletObject> getBullets() {
        return bullets;
    }

    public int getScore() {
        return score;
    }

    public int getTopScore() {
        return topScore;
    }

    private void initializeEnemies() {
        int firstRowXStart = 40;
        int firstRowY = 0;
        int secondRowXStart = 100;
        int secondRowY = 50;
        int distance = 130;
        int width = 45;
        int height = 45;

        for (int i = 0; i < 4; i++) {
            int x = firstRowXStart + i * distance;
            int y = firstRowY;
            enemies.add(new EnemyObject(x, y, width, height, "images/Enemy_1.png"));
        }

        for (int i = 0; i < 4; i++) {
            int x = secondRowXStart + i * distance;
            int y = secondRowY;
            enemies.add(new EnemyObject(x, y, width, height, "images/Enemy_1.png"));
        }
    }

    public void moveEnemies() {
        int boundaryLeft = Integer.MAX_VALUE;
        int boundaryRight = Integer.MIN_VALUE;

        for (EnemyObject enemy : enemies) {
            boundaryLeft = Math.min(boundaryLeft, enemy.getPosX());
            boundaryRight = Math.max(boundaryRight, enemy.getPosX() + enemy.getWidth());
        }

        for (EnemyObject enemy : enemies) {
            if (movingRight) {
                enemy.setPosX(enemy.getPosX() + speedX);
            } else {
                enemy.setPosX(enemy.getPosX() - speedX);
            }
        }

        if (movingRight && boundaryRight >= 600) {
            movingRight = false;
            for (EnemyObject enemy : enemies) {
                enemy.setPosY(enemy.getPosY() + speedY);
            }
        } else if (!movingRight && boundaryLeft <= 0) {
            movingRight = true;
            for (EnemyObject enemy : enemies) {
                enemy.setPosY(enemy.getPosY() + speedY);
            }
        }


    }


    public void fireBullet() {
        int bulletX = player.getPosX() + player.getWidth() / 2 - 5;
        int bulletY = player.getPosY() - 20;

        BulletObject bullet = new BulletObject(bulletX, bulletY, 10, 20, "images/PlayerBullet.png");
        bullets.add(bullet);
    }


    public void moveBullets() {
        List<BulletObject> bulletsToRemove = new ArrayList<>();
        for (BulletObject bullet : bullets) {
            bullet.updatePosition();
            if (bullet.isOutOfScreen()) {
                bulletsToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsToRemove);
    }

    public void fireEnemyBullets(List<EnemyObject> enemies) {
        for (EnemyObject enemy : enemies) {
            int bulletX = enemy.getPosX() + enemy.getWidth() / 2 - 5;
            int bulletY = enemy.getPosY() + enemy.getHeight();

            if (Math.random() < 0.003) {
                enemyBullets.add(new EnemyBulletObject(bulletX, bulletY, 20, 20, "images/EnemyBullet.png"));
            }
        }
    }

    public List<EnemyBulletObject> getEnemyBullets() {
        return enemyBullets;
    }

    public void moveEnemyBullets() {
        enemyBullets.removeIf(bullet -> {
            bullet.updatePosition();
            return bullet.isOutOfScreen(500);
        });
    }

    public void spawnBonusObject() {
        if (Math.random() < 0.003) {
            BonusObject bonus = new BonusObject(0, 50, 50, 30, "images/Bonus.png");
            bonusObjects.add(bonus);
        }
    }

    public void moveBonusObjects() {
        bonusObjects.removeIf(bonus -> {
            bonus.updatePosition();
            return bonus.isOutOfScreen(600);
        });
    }

    public List<BonusObject> getBonusObjects() {
        return bonusObjects;
    }

    public void checkCollisions() {
        List<EnemyObject> enemiesToRemove = new ArrayList<>();
        List<BulletObject> bulletsToRemove = new ArrayList<>();
        List<BonusObject> bonusToRemove = new ArrayList<>();

        for (BulletObject bullet : bullets) {
            for (EnemyObject enemy : enemies) {
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    enemiesToRemove.add(enemy);
                    bulletsToRemove.add(bullet);
                    score += 10;
                }

            }

            for (BonusObject bonus : bonusObjects) {
                if (bullet.getBounds().intersects(bonus.getBounds())) {
                    bonusToRemove.add(bonus);
                    bulletsToRemove.add(bullet);
                    score += 100;
                }
            }
        }

        enemies.removeAll(enemiesToRemove);
        bullets.removeAll(bulletsToRemove);
        bonusObjects.removeAll(bonusToRemove);

    }

    public void checkPlayerHit() {
        if (gameOver) {
            return;
        }

        for (EnemyBulletObject bullet : enemyBullets) {
            if (bullet.getBounds().intersects(player.getBounds())) {
                gameOver();
                break;
            }
        }

        for (EnemyObject enemy : enemies) {
            if (enemy.getBounds().intersects(player.getBounds())) {
                gameOver();
                break;
            }
        }
    }

    public void checkEnemyPosition() {
        if (gameOver) {
            return;
        }

        for (EnemyObject enemy : enemies) {
            if (enemy.getPosY() + enemy.getHeight() >= 500) {
                gameOver();
                return;
            }
        }
    }


    private void gameOver() {
        gameOver = true;

        if (score > topScore) {
            topScore = score;
        }

        int option = JOptionPane.showConfirmDialog(
                null,
                "Play Again?",
                "You LOSE!",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    public void checkWinCondition() {
        if (enemies.isEmpty() && !gameOver) {
            gameWin();
        }
    }

    private void gameWin() {
        gameOver = true;

        if (score > topScore) {
            topScore = score;
        }

        int option = JOptionPane.showConfirmDialog(
                null,
                "Play Again?",
                "You WIN!",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    public void restartGame() {
        score = 0;

        player = new PlayerObject(300, 500, 50, 50, "images/Player.png");

        enemies.clear();
        bullets.clear();
        enemyBullets.clear();
        bonusObjects.clear();

        initializeEnemies();

        gameOver = false;
    }


    private int loadTopScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(topScoreFile))) {
            return Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    public void updatePlayerMovement() {
        int dx = 0;
        int dy = 0;

        if (leftPressed) dx -= playerSpeed;
        if (rightPressed) dx += playerSpeed;
        if (upPressed) dy -= playerSpeed;
        if (downPressed) dy += playerSpeed;

        player.setPosX(Math.max(0, Math.min(600, player.getPosX() + dx)));
        player.setPosY(Math.max(0, Math.min(500, player.getPosY() + dy)));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;
            case KeyEvent.VK_UP:
                upPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = true;
                break;
            case KeyEvent.VK_SPACE:
                fireBullet();
                break;
        }
        updatePlayerMovement();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;
            case KeyEvent.VK_UP:
                upPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
