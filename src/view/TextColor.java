package view;

import model.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class text color contains the cases for different text colours
 */
public class TextColor implements ActionListener {
    private final MainFrame frame;

    /**
     * This constructor initializes the text colour
     * @param frame is the instance of the window
     */
    public TextColor(MainFrame frame) {
        this.frame = frame;
    }

    /**
     * This method contains the action to change text color based on selection
     * @param e is action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (((JMenuItem) e.getSource()).getText()) {
            case "White" -> {
                Constants.TEXTCOLOR = Color.WHITE;
                frame.getContainer().updateGrid();
            }
            case "Red" -> {
                Constants.TEXTCOLOR = Color.RED;
                frame.getContainer().updateGrid();
            }
            case "Blue" -> {
                Constants.TEXTCOLOR = Color.BLUE;
                frame.getContainer().updateGrid();
            }
            case "Yellow" -> {
                Constants.TEXTCOLOR = Color.YELLOW;
                frame.getContainer().updateGrid();
            }
        }
    }
}
