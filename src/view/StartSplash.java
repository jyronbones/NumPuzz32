package view;

import model.Path;

import javax.swing.*;
import java.io.Serial;

/**
 * This class contains the splash window for the start of the game
 */
public class StartSplash extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    JLabel image = new JLabel(new ImageIcon("resources/start.jpg"));

    /**
     * Creates an instance of splash with start game image
     */
    public StartSplash() {
        getContentPane().setLayout(null);
        setUndecorated(true);

        setSize(800, 800);
        setLocationRelativeTo(null);
        add(image);
        image.setSize(800, 800);
        setVisible(true);


        SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(2000); // change this to button
                setVisible(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }
}