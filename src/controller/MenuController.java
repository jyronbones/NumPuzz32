package controller;

import clientserver.GameClient;
import model.Constants;
import model.GenerateGrid;
import model.Path;
import model.SoundPlayer;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Controller class that allows user to select various menu options and implements their effects on the game
 */
public class MenuController implements ActionListener {

    private MainFrame frame;

    GameClient client;

    /**
     * Constructor for MenuController
     *
     * @param frame takes in the primary NumPuzz game frame
     */
    public MenuController(MainFrame frame, GameClient client) {
        this.frame = frame;
        this.client = client;
    }

    /**
     * Action listener that listents for user mouseclicks on various menu items
     *
     * @param e ActionEvent, mouse click on menu items
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (((JMenuItem) e.getSource()).getText().equals("New Game")) {
            int choice = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to start a new game?",
                    "New Game", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                SoundPlayer.pauseSong();
                SoundPlayer.dispose();
                frame.dispose();
                new MainFrame(client);
            }
        } else if (((JMenuItem) e.getSource()).getText().equals("Save")) {
            try {
                saveGame();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (((JMenuItem) e.getSource()).getText().equals("Load")) {
            try {
                loadGame();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (((JMenuItem) e.getSource()).getText().equals("Exit")) {
            System.exit(0);
        } else if (((JMenuItem) e.getSource()).getText().equals("Undo")) {
            frame.getContainer().getGrid().getCon().undoLastMove();
        } else if (((JMenuItem) e.getSource()).getText().equals("White")) {
            Constants.GRIDCOLOR = Color.WHITE;
            frame.getContainer().updateGrid();
        } else if (((JMenuItem) e.getSource()).getText().equals("Red")) {
            Constants.GRIDCOLOR = Color.RED;
            frame.getContainer().updateGrid();
        } else if (((JMenuItem) e.getSource()).getText().equals("Blue")) {
            Constants.GRIDCOLOR = Color.BLUE;
            frame.getContainer().updateGrid();
        } else if (((JMenuItem) e.getSource()).getText().equals("Yellow")) {
            Constants.GRIDCOLOR = Color.YELLOW;
            frame.getContainer().updateGrid();
        } else if (((JMenuItem) e.getSource()).getText().equals("Yellow")) {
            Constants.GRIDCOLOR = Color.YELLOW;
            frame.getContainer().updateGrid();
        } else if (((JMenuItem) e.getSource()).getText().equals("Help")) {
            JOptionPane.showMessageDialog(
                    null, "Hello there! Welcome to NumPuzz",
                    "Instructions", JOptionPane.PLAIN_MESSAGE);
        } else if (((JMenuItem) e.getSource()).getText().equals("Credits")) {
            int copyrightSymbolCodePoint = 169;
            String s = Character.toString(copyrightSymbolCodePoint);
            JOptionPane.showMessageDialog(
                    null, "NumPuzz created by:\nByron Jones & Sean Wray\n" +
                            "Copyright " + s + "2022", "Credits", JOptionPane.PLAIN_MESSAGE);
        }
    }

    /**
     * loads a previously saved game of NumPuzz
     */
    private void loadGame() {
        int choice = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to load your previously saved game?\n" +
                        "NOTE: You will lose all progress in your current game",
                "Save Game", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                Scanner scanner = new Scanner(new File(Path.SAVE_PATH));
                Constants.ITEM_COL = scanner.nextInt();
                Constants.ITEM_ROWS = scanner.nextInt();
                GenerateGrid.custom = new ArrayList<>();
                for (int i = 0; i < Constants.ITEM_ROWS * Constants.ITEM_COL - 1; i++) {
                    GenerateGrid.custom.add("" + scanner.nextInt());
                }
                GenerateGrid.custom.add("");
                scanner.close();
                Constants.CUSTOM = 1;
                frame.getContainer().updateGrid();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves current iteration of a numpuzz game to external file which can be loaded later
     */
    private void saveGame() {
        int choice = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to save current game?\n" +
                        "NOTE: This will override any previously saved game",
                "Save Game", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            PrintWriter wr = null;
            try {
                wr = new PrintWriter(Path.SAVE_PATH);
                JButton[][] but = frame.getContainer().getGrid().getButtons();
                wr.write(Constants.ITEM_ROWS + "\n");
                wr.write(Constants.ITEM_ROWS + "\n");
                for (int i = 0; i < Constants.ITEM_ROWS; i++) {
                    for (int j = 0; j < Constants.ITEM_COL; j++) {
                        wr.write(but[i][j].getText() + "\n");
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            wr.close();
            JOptionPane.showMessageDialog(null,
                    "Your game has been saved",
                    "Game Saved", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
