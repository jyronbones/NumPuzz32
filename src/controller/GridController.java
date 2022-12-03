package controller;

import clientserver.GameClient;
import model.Constants;
import model.Path;
import model.ScoreReader;
import model.SoundPlayer;
import view.Container;
import view.WinSplash;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A class used to track the player moves so that they can be undone. Stores their
 * x and y coordinates in the game grid.
 */
class Move {
    /**
     * X and Y coordinates of the game button locations in our 2D array
     */
    int x, y;

    /**
     * Constructor that allows a move object to be created and coordinates to be set
     *
     * @param x x coordinate of game button
     * @param y y coordinate of game button
     */
    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

/**
 * A class that handles moves made on the game grid (2D jbutton array)
 */
public class GridController implements ActionListener {

    private Container container;
    private int move = 0;
    private ArrayList<Move> lastMoves;
    private ArrayList<String> movesDone;
    GameClient client;

    /**
     * Creates an object of GridController and takes in a container object, which contains the panels
     * of current NumPuzz game and their respective data as well as the game frame itself
     *
     * @param container an object of class container that contains all panels of NumPuzz
     */
    public GridController(Container container, GameClient client) {
        this.client = client;
        lastMoves = new ArrayList<>();
        this.container = container;
        this.container.getBottomPanel().setHighScore("" + ScoreReader.readHigh());
        movesDone = new ArrayList<>();
    }

    /**
     * Listener function for action performed, in this class a mouse click on a Jbutton
     *
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton[][] but = container.getGrid().getButtons();
        int empty_x = 0, empty_y = 0, click_x = 0, click_y = 0;
        for (int i = 0; i < but.length; i++) {
            for (int j = 0; j < but[i].length; j++) {
                if (but[i][j].getText().equals("")) {
                    empty_x = i;
                    empty_y = j;
                    break;
                }
            }
        }
        for (int i = 0; i < but.length; i++) {
            for (int j = 0; j < but[i].length; j++) {
                if (but[i][j].equals(e.getSource())) {
                    click_x = i;
                    click_y = j;
                    break;
                }
            }
        }
        if (empty_x == click_x && empty_y == click_y) {
            return;
        } else if (Math.abs(empty_x - click_x) >= 2 || Math.abs(empty_y - click_y) >= 2) {
            return;
        }
        if (empty_x == click_x || empty_y == click_y) {
            but[empty_x][empty_y].setText(but[click_x][click_y].getText());
            but[click_x][click_y].setText("");
            but[click_x][click_y].setBackground(Color.GRAY);
            but[empty_x][empty_y].setBackground(Constants.GRIDCOLOR);
            move++;
            String mv = String.format("%s to row %d:col %d", but[empty_x][empty_y].getText(), empty_x + 1, empty_y + 1);
            movesDone.add(mv);
            String s = "";
            for (String m : movesDone)
                s += m + "\n";
            container.getSettings().getTextArea().setText(s);
            container.getBottomPanel().setPlayerScore("" + move);
            client.setScore(move);
            lastMoves.add(new Move(empty_x, empty_y));
        }
        /**
         * An ArrayList of tiles for NumPuzz game
         */
        ArrayList<String> getTiles = new ArrayList<>();
        /**
         * A sorted ArrayList of tiles for NumPuzz game
         */
        ArrayList<String> sort = new ArrayList<>();
        for (int i = 0; i < but.length; i++) {
            for (int j = 0; j < but[i].length; j++) {
                if ((i + 1) * (j + 1) != but.length * but[0].length) {
                    getTiles.add(but[i][j].getText());
                    sort.add(but[i][j].getText());
                }
            }
        }
        Collections.sort(sort);
        if (sort.equals(getTiles)) {
            if (container.getSettings().getDifficulty().getSelectedIndex() == 0) {
                int score = ScoreReader.readEasy();
                if (move < score) {
                    ScoreReader.write(Path.EASY_SCORE_, move);
                }
            } else if (container.getSettings().getDifficulty().getSelectedIndex() == 0) {
                int score = ScoreReader.readMedium();
                if (move < score) {
                    ScoreReader.write(Path.MEDIUM_SCORE_, move);
                }
            } else {
                int score = ScoreReader.readHigh();
                if (move < score) {
                    ScoreReader.write(Path.HIGH_SCORE_, move);
                }
            }
            SoundPlayer.pauseSong();
            SoundPlayer.dispose();
            WinSplash splash = new WinSplash(client);
            container.dispose();
        }
    }

    /**
     * A function that undoes the previous player move when clicked
     */
    public void undoLastMove() {
        if (lastMoves.size() == 0) {
            JOptionPane.showMessageDialog(null, "No Last move");
            container.getSettings().getTextArea().setText("");
            movesDone = new ArrayList<>();
        } else {
            int empty_x = 0, empty_y = 0;
            JButton[][] but = container.getGrid().getButtons();
            for (int i = 0; i < but.length; i++) {
                for (int j = 0; j < but[i].length; j++) {
                    if (but[i][j].getText().equals("")) {
                        empty_x = i;
                        empty_y = j;
                        break;
                    }
                }
            }
            Move mv = lastMoves.remove(lastMoves.size() - 1);
            String mvs = movesDone.remove(movesDone.size() - 1);
            but[empty_x][empty_y].setText(but[mv.x][mv.y].getText());
            but[mv.x][mv.y].setText("");
            but[mv.x][mv.y].setBackground(Color.GRAY);
            but[empty_x][empty_y].setBackground(Constants.GRIDCOLOR);
            move--;
            StringBuilder s = new StringBuilder();
            for (String m : movesDone)
                s.append(m).append("\n");
            System.out.println(s);
            container.getSettings().getTextArea().setText(s.toString());
            container.getBottomPanel().setPlayerScore("" + move);
        }
    }
}
