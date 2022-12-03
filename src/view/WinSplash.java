package view;

import clientserver.GameClient;
import model.Path;
import model.SoundPlayer;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.Serial;

/**
 * This class contains the splash window for winning the game
 */
public class WinSplash extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    JLabel image = new JLabel(new ImageIcon("resources/winner.jpg"));
    GameClient client;

    /**
     * Creates an instance of splash with win game image and sound effects
     */
    public WinSplash(GameClient client) {
        this.client = client;
        getContentPane().setLayout(null);
        setUndecorated(true);

        setSize(800, 800);
        setLocationRelativeTo(null);
        add(image);
        image.setSize(800, 800);
        setVisible(true);


        SwingUtilities.invokeLater(() -> {
            try {
                SoundPlayer.pauseSong();
                SoundPlayer.dispose();
                Clip clip = SoundPlayer.playGameSound("resources/applause.wav");
                Thread.sleep(2000); // change this to button
                setVisible(false);
                clip.stop();
                SoundPlayer.dispose();
                SoundPlayer.playGameSound("resources/jeopardy.wav");
                SoundPlayer.start();
                MainFrame frame = new MainFrame(client);
                frame.pack();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }
}