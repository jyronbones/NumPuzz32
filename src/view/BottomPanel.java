package view;

import model.Path;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This panel will contain the high score and current score for the NumPuzz game
 */
public class BottomPanel extends JPanel {
    @Serial
    private static final long serialVersionUID = 1L;
    private final JLabel playerScore;
    private final JLabel highScore;
    private final JLabel timer;
    private final Container container;

    /**
     * This constructor initializes the components of the bottom panel such as player score and high score
     * @param container is the class that contains all the panels for numpuzz
     */
    public BottomPanel(Container container) {
        this.container = container;
        setPreferredSize(new Dimension(100, 75));
        setBackground(Color.BLACK);
        setLayout(new GridLayout(2, 6, 5, 5));
        setForeground(Color.YELLOW);
        setFont(new Font("Cooper Black", Font.BOLD, 21));


        playerScore = new JLabel("0", SwingConstants.CENTER);
        playerScore.setForeground(Color.YELLOW);
        playerScore.setFont(new Font("Cooper Black", Font.BOLD, 21));

        highScore = new JLabel("0", SwingConstants.CENTER);
        highScore.setForeground(Color.YELLOW);
        highScore.setFont(new Font("Cooper Black", Font.BOLD, 21));

        timer = new JLabel("0", SwingConstants.CENTER);
        timer.setForeground(Color.WHITE);
        timer.setFont(new Font("Rockwell", Font.BOLD, 20));

        AtomicInteger seconds = new AtomicInteger();
        Timer t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.setText(Integer.toString(seconds.get()));
                seconds.addAndGet(1);
                container.client.setTime(seconds.get());
                container.client.setScore(Integer.parseInt(playerScore.getText()));
                container.client.sendScore();
            }
        });
        t.start();
        add(new JLabel(new ImageIcon("resources/pscore.jpg"), SwingConstants.CENTER));
        add(new JLabel(new ImageIcon("resources/bscore.jpg"), SwingConstants.CENTER));
        add(new JLabel(new ImageIcon("resources/timer.gif"), SwingConstants.CENTER));
        add(playerScore);
        add(highScore);
        add(timer);


    }

    /**
     * This method sets the player score in the bottom panel
     * @param s is the score of the player
     */
    public void setPlayerScore(String s) {
        playerScore.setText(s);
    }

    /**
     * This method sets the high score in the bottom panel
     * @param s is the score of the high score
     */
    public void setHighScore(String s) {
        highScore.setText(s);
    }

    /**
     * This method sets the time for the timer
     * @param s is the time
     */
    public void setTimer(String s) {
        timer.setText(s);
    }

    /**
     * This method accesses the container
     * @return is the container for all panels
     */
    public Container getContainer() {
        return container;
    }
}
