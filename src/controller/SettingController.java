package controller;

import model.Constants;
import model.GenerateGrid;
import model.SoundPlayer;
import view.Container;

import javax.sound.sampled.FloatControl;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Controller class that handles settings options from settings panel of NumPuzz game
 */
public class SettingController implements ActionListener, ChangeListener {
    private final Container con;

    /**
     * Creates an object of GridController and takes in a container object, which contains the panels
     * of current NumPuzz game and their respective data as well as the game frame itself
     * @param container an object of class container that contains all panels of NumPuzz
     */
    public SettingController(Container container) {
        this.con = container;
    }

    /**
     * Action listener that listents for user mouseclicks on various settings buttons
     * @param e ActionEvent, mouse click on settings items
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(con.getSettings().getPause())) {
            SoundPlayer.pauseSong();
        } else if (e.getSource().equals(con.getSettings().getPlay())) {
            SoundPlayer.start();
        } else if (e.getSource().equals(con.getSettings().getDifficulty()) || e.getSource().equals(con.getSettings().getModeBox())) {
            Constants.SELECTION = con.getSettings().getDifficulty().getSelectedIndex();
            Constants.GAME_MODE = con.getSettings().getModeBox().getSelectedIndex();
            if (Constants.SELECTION == 0) {
                Constants.ITEM_ROWS = 3;
                Constants.ITEM_COL = 3;
            } else if (Constants.SELECTION == 1) {
                Constants.ITEM_ROWS = 4;
                Constants.ITEM_COL = 4;
            } else {
                Constants.ITEM_ROWS = 5;
                Constants.ITEM_COL = 5;
            }
            con.updateGrid();
        } else if (e.getSource().equals(con.getSettings().getUndoLast())) {
            con.getGrid().getCon().undoLastMove();
        } else if (e.getSource().equals(con.getSettings().getResetGame())) {
            if (con.getSettings().getPlayGame().isSelected()) {
                con.reset();
            } else {
                if (Constants.GAME_MODE == 0) {
                    try {
                        String inputString = "";
                        if (Constants.SELECTION == 0) {
                            inputString = JOptionPane.showInputDialog(null,
                                    """
                                            Enter series of numbers for easy difficulty
                                            example: 1,3,2,8,4,5,6,7,0
                                            NOTE: Easy is 9 numbers
                                            0 is used for blank tile and MUST be placed last""",
                                    "Custom Easy Number Design", JOptionPane.INFORMATION_MESSAGE);
                        } else if (Constants.SELECTION == 1) {
                            inputString = JOptionPane.showInputDialog(null,
                                    """
                                            Enter series of numbers for medium difficulty
                                            example: 15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0
                                            NOTE: Medium is 16 numbers
                                            0 is used for blank tile and MUST be placed last""",
                                    "Custom Medium Numbers Design", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            inputString = JOptionPane.showInputDialog(null,
                                    """
                                            Enter series of numbers for current hard difficulty
                                            example: 24,23,22,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0
                                            NOTE: Hard is 25 numbers
                                            0 is used for blank tile and MUST be placed last""",
                                    "Custom Hard Numbers Design", JOptionPane.INFORMATION_MESSAGE);
                        }
                        String[] str = inputString.split(",");
                        if (str.length != Constants.ITEM_ROWS * Constants.ITEM_COL)
                            throw new Exception();
                        ArrayList<String> list1 = new ArrayList<>();
                        list1.addAll(Arrays.asList(str));
                        if (!list1.contains("0"))
                            throw new Exception("Zero not present");
                        Collections.sort(list1);
                        for (int i = 0; i < list1.size() - 1; i++) {
                            if (list1.get(i + 1).charAt(0) - list1.get(i).charAt(0) != 1) {
                                throw new Exception();
                            }
                        }
                        GenerateGrid.custom = new ArrayList<>();
                        GenerateGrid.custom.addAll(Arrays.asList(str));
                        GenerateGrid.custom.remove("0");
                        GenerateGrid.custom.add("");
                        Constants.CUSTOM = 1;
                        con.updateGrid();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,
                                "Invalid number sequence, please try again",
                                "Invalid Numbers", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    try {
                        String inputString = "";
                        if (Constants.SELECTION == 0) {
                            inputString = JOptionPane.showInputDialog(null,
                                    """
                                            Enter series of characters for easy difficulty
                                            example: a,b,c,d,e,f,g,h,0
                                            NOTE: Easy is 9 characters
                                            0 is used for blank tile and MUST be placed last""",
                                    "Custom Easy characters Design", JOptionPane.INFORMATION_MESSAGE);
                        } else if (Constants.SELECTION == 1) {
                            inputString = JOptionPane.showInputDialog(null,
                                    """
                                            Enter series of characters for medium difficulty
                                            example: a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,0
                                            NOTE: Medium is 16 characters
                                            0 is used for blank tile and MUST be placed last""",
                                    "Custom Medium characters Design", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            inputString = JOptionPane.showInputDialog(null,
                                    """
                                            Enter series of characters for current hard difficulty
                                            example: a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,0
                                            NOTE: Hard is 25 characters
                                            0 is used for blank tile and MUST be placed last""",
                                    "Custom Hard characters Design", JOptionPane.INFORMATION_MESSAGE);
                        }
                        String[] str = inputString.split(",");
                        if (str.length != Constants.ITEM_ROWS * Constants.ITEM_COL)
                            throw new Exception();
                        ArrayList<String> list1 = new ArrayList<>();
                        list1.addAll(Arrays.asList(str));
                        if (!list1.contains("0"))
                            throw new Exception("Zero not present");
                        Collections.sort(list1);
                        for (int i = 0; i < list1.size() - 1; i++) {
                            if(list1.get(i).equals("0"))
                                continue;
                            System.out.println(list1.get(i + 1).charAt(0) - list1.get(i).charAt(0));
                            if (list1.get(i + 1).charAt(0) - list1.get(i).charAt(0) != 1) {
                                throw new Exception();
                            }
                        }
                        GenerateGrid.custom = new ArrayList<>();
                        GenerateGrid.custom.addAll(Arrays.asList(str));
                        GenerateGrid.custom.remove("0");
                        GenerateGrid.custom.add("");
                        Constants.CUSTOM = 1;
                        con.updateGrid();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,
                                "Invalid number sequence, please try again",
                                "Invalid Numbers", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        } else if (e.getSource().equals(con.getSettings().getRestart())) {
            con.reset();
        }
    }

    /**
     * Action listner that listens for a change of state in volume slider for ingame music and sound effects
     * @param e ActionListener for change of state in volume slider
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        FloatControl fc = (FloatControl) SoundPlayer.getClip().getControl(FloatControl.Type.MASTER_GAIN);
        float currentVolume = this.con.getSettings().getSlider().getValue();
        fc.setValue(currentVolume);
    }
}
