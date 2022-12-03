package view;

import clientserver.GameClient;
import model.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

/**
 * This class contains all the panels and frame used for the game
 */
public class Container extends JPanel {
    @Serial
    private static final long serialVersionUID = 1L;
    private Grid grid;
    private Settings settings;
    private BottomPanel bottomPanel;
    private final MainFrame frame;
    GameClient client;

    /**
     * This constructor initializes the panels
     *
     * @param frame is the game window
     */
    public Container(MainFrame frame, GameClient client) {
        this.client = client;
        this.frame = frame;
        setLayout(new BorderLayout());
        bottomPanel = new BottomPanel(this);
        settings = new Settings(this);
        grid = new Grid(this, client);
        add(grid, BorderLayout.CENTER);
        add(settings, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * This method accesses the grid for numbers or letters
     *
     * @return
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * This method accesses the settings panel for the game
     *
     * @return
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * This method accesses the bottom panel for the game
     *
     * @return
     */
    public BottomPanel getBottomPanel() {
        return bottomPanel;
    }

    /**
     * This method resets the game settings
     */
    public void reset() {
        Constants.ITEM_ROWS = 3;
        Constants.ITEM_COL = 3;
        Constants.GAME_MODE = 0;
        Constants.SELECTION = 0;
        remove(grid);
        remove(settings);
        remove(bottomPanel);
        bottomPanel = new BottomPanel(this);
        settings = new Settings(this);
        grid = new Grid(this, client);
        add(grid, BorderLayout.CENTER);
        add(settings, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * This method updates the grid
     */
    public void updateGrid() {
        System.out.println("WORKING");
        remove(grid);
        grid = new Grid(this, client);
        add(grid, BorderLayout.CENTER);
    }

    /**
     * This method disposes the frame
     */
    public void dispose() {
        this.frame.dispose();
    }
}
