import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 15;
    static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE;
    static final int DELAY = 60;

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];

    int bodyParts = 6;
    int appleEaten;
    int appleX;
    int appleY;

    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel () {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame () {
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void newApple () {
        appleX = random.nextInt((SCREEN_WIDTH/UNIT_SIZE)) *UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE ) *UNIT_SIZE;
    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw (Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
//                    g.setColor(new Color(45, 180, 0));
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        }
        else {
            gameOver(g);
        }
        score(g);
    }

    public void score (Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score : " + appleEaten, (SCREEN_WIDTH - metrics.stringWidth("Score : " + appleEaten)) /2, g.getFont().getSize());
    }
    public void move () {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }

    }
    public void checkApple () {
        if ((x[0] == appleX) && y[0] == appleY) {
            bodyParts++;
            appleEaten++;
            newApple();
        }
    }
    public void checkCollisions () {
//        check head collision with body parts
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) running = false;
        }

//        checking the snake collision with borders
        if (x[0] < 0) running = false;
        if (y[0] < 0) running = false;
        if (x[0] > SCREEN_WIDTH) running = false;
        if (y[0] > SCREEN_HEIGHT) running = false;

        if (!running) timer.stop();

    }
    public void gameOver (Graphics g) {
//        showing score at gameover
        score(g);
        // Game Over Text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) /2, (SCREEN_HEIGHT/2));
    }

    public class MyKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case 65 -> {
                    if (direction != 'R') direction = 'L';
                }
                case 68 -> {
                    if (direction != 'L') direction = 'R';
                }
                case 87 -> {
                    if (direction != 'D') direction = 'U';
                }
                case 83-> {
                    if (direction != 'U') direction = 'D';
                }
            }
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

}
