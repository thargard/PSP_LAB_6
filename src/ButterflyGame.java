import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ButterflyGame extends JFrame implements ActionListener{

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int BUTTERFLY_SIZE = 50;
    private static final int NET_SIZE = 100;

    private JButton startButton;
    private JButton catchButton;
    private JButton stopButton;

    private JLabel scoreLabel;
    private JLabel resultLabel;

    private JPanel drawPanel;

    private int butterflyX;
    private int butterflyY;
    private int netX;
    private int netY;

    private boolean isRunning;
    private boolean isCatching;

    private int hits;
    private int misses;

    private Random random;

    public ButterflyGame() {
        setTitle("Бабочка и сачок");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        startButton = new JButton("Старт");
        catchButton = new JButton("Ловить");
        stopButton = new JButton("Стоп");

        startButton.addActionListener(this);
        catchButton.addActionListener(this);
        stopButton.addActionListener(this);

        buttonPanel.add(startButton);
        buttonPanel.add(catchButton);
        buttonPanel.add(stopButton);

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout());

        scoreLabel = new JLabel("Попадания: 0, промахи: 0");
        resultLabel = new JLabel("");

        labelPanel.add(scoreLabel);
        labelPanel.add(resultLabel);

        drawPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.YELLOW);
                g.fillOval(butterflyX, butterflyY, BUTTERFLY_SIZE, BUTTERFLY_SIZE);
                g.setColor(Color.GREEN);
                g.fillRect(netX, netY, NET_SIZE, NET_SIZE);
            }
        };

        add(buttonPanel, BorderLayout.NORTH);
        add(labelPanel, BorderLayout.SOUTH);
        add(drawPanel, BorderLayout.CENTER);

        butterflyX = WIDTH / 2 - BUTTERFLY_SIZE / 2;
        butterflyY = HEIGHT / 2 - BUTTERFLY_SIZE / 2;
        netX = WIDTH/2;
        netY = 0;
        isRunning = false;
        isCatching = false;
        hits = 0;
        misses = 0;
        random = new Random();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            startGame();
        }
        else if (e.getSource() == catchButton) {
            catchButterfly();
        }
        else if (e.getSource() == stopButton) {
            stopGame();
        }
    }

    private void startGame() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        hits = 0;
        misses = 0;
        scoreLabel.setText("Попадания: 0, промахи: 0");
        resultLabel.setText("");
        Thread butterflyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    int dx = random.nextInt(21) - 10;
                    int dy = random.nextInt(21) - 10;
                    butterflyX += dx;
                    butterflyY += dy;
                    if (butterflyX < 0) {
                        butterflyX = 0;
                    }
                    if (butterflyX > WIDTH - BUTTERFLY_SIZE) {
                        butterflyX = WIDTH - BUTTERFLY_SIZE;
                    }
                    if (butterflyY < 0) {
                        butterflyY = 0;
                    }
                    if (butterflyY > HEIGHT - BUTTERFLY_SIZE) {
                        butterflyY = HEIGHT - BUTTERFLY_SIZE;
                    }
                    drawPanel.repaint();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        butterflyThread.start();
    }

    private void catchButterfly() {
        if (!isRunning) {
            return;
        }
        if (isCatching) {
            return;
        }
        isCatching = true;
        Thread netThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 300; i++) {
                    netY++;
                    drawPanel.repaint();
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                checkHit();
                for (int i = 0; i < 300; i++) {
                    netY--;
                    drawPanel.repaint();
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isCatching = false;
            }
        });
        netThread.start();
    }

    private void checkHit() {
        if (butterflyX + BUTTERFLY_SIZE / 2 >= netX &&
                butterflyX + BUTTERFLY_SIZE / 2 <= netX + NET_SIZE || // было &&
                butterflyY + BUTTERFLY_SIZE / 2 >= netY &&
                butterflyY + BUTTERFLY_SIZE / 2 <= netY + NET_SIZE) {
            hits++;
            scoreLabel.setText("Попадания: " + hits + ", промахи: " + misses);
        } else {
            misses++;
            scoreLabel.setText("Попадания: " + hits + ", промахи: " + misses);
        }
    }

    private void stopGame() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
        resultLabel.setText("Результат: " + hits + " попаданий и " + misses + " промахов");
    }

    public static void main(String[] args){
        ButterflyGame bg = new ButterflyGame();
        bg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bg.setVisible(true);
    }
}