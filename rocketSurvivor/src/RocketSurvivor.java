import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class RocketSurvivor extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 1200;
    int boardHeight = 736;

    // Images
    Image backgroundImg;
    Image rocketImg;
    Image meteoriteImg;

    // Rocket
    class Rocket {
        int x = rocketX;
        int y = rocketY;
        int width = rocketWidth;
        int height = rocketHeight;
        Image img;

        Rocket(Image img) {
            this.img = img;
        }
    }

    int rocketX = boardWidth / 8;
    int rocketY = boardHeight / 2;
    int rocketWidth = 160;
    int rocketHeight = 80;

    // Meteorites
    class Meteorite {
        int x;
        int y;
        int width;
        int height;

        Meteorite(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    ArrayList<Meteorite> meteorites = new ArrayList<>();
    int meteoriteWidth = 180;
    int meteoriteHeight = 180;
    int meteoriteSpawnRate = 85;
    int gameTick = 0;

    //Game logic
    Rocket rocket;
    int velocityY = 0;
    int gravity = 1;
    boolean isGameOver = false;

    Timer gameLoop;
    Random random = new Random();

    //Score
    int score = 0;

    RocketSurvivor() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        //Load images
        backgroundImg = new ImageIcon(getClass().getResource("darkSkyBackground.jpg")).getImage();
        rocketImg = new ImageIcon(getClass().getResource("rocket.png")).getImage();
        meteoriteImg = new ImageIcon(getClass().getResource("meteorite.png")).getImage();

        //Rocket initialization
        rocket = new Rocket(rocketImg);

        //Game timer
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //Background
        if (backgroundImg != null) {
            g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, boardWidth, boardHeight);
        }

        //Rocket
        g.drawImage(rocket.img, rocket.x, rocket.y, rocket.width, rocket.height, null);

        //Meteorites
        for (Meteorite meteorite : meteorites) {
            g.drawImage(meteoriteImg, meteorite.x, meteorite.y, meteorite.width, meteorite.height, null);
        }

        //Score display
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Score: " + score, 20, 40);

        //Game over Text
        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER !", boardWidth / 2 - 150, boardHeight / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 30));
            g.drawString("Final Score: " + score, boardWidth / 2 - 100, boardHeight / 2 + 50);
        }
    }

    public void move() {
        //Rocket
        velocityY += gravity;
        rocket.y += velocityY;
        rocket.y = Math.max(rocket.y, 0);
        rocket.y = Math.min(rocket.y, boardHeight - rocketHeight);

        //Meteorites
        for (int i = 0; i < meteorites.size(); i++) {
            Meteorite meteorite = meteorites.get(i);
            meteorite.x -= 5;

            // Remove meteorites that go out of bounds and increment score
            if (meteorite.x + meteorite.width < 0) {
                meteorites.remove(i);
                score++;
                i--;
            }
        }

        // Check collisions
        checkCollisions();
    }

    public void spawnMeteorite() {
        int y;
        do {
            y = random.nextInt(boardHeight - meteoriteHeight);
        } while (Math.abs(y - rocket.y) < rocketHeight);

        meteorites.add(new Meteorite(boardWidth, y, meteoriteWidth, meteoriteHeight));
    }

    public void checkCollisions() {
        for (Meteorite meteorite : meteorites) {
            if (rocket.x < meteorite.x + meteorite.width &&
                    rocket.x + rocket.width > meteorite.x &&
                    rocket.y < meteorite.y + meteorite.height &&
                    rocket.y + rocket.height > meteorite.y) {
                isGameOver = true;
                gameLoop.stop();

                //print the final score in console
                System.out.println("Game Over! Final Score: " + score);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            gameTick++;

            if (gameTick % meteoriteSpawnRate == 0) {
                spawnMeteorite();
            }

            move();
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !isGameOver) {
            velocityY = -9;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
