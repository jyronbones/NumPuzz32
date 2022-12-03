package view;

import clientserver.GameClient;
import controller.GridController;
import model.Constants;
import model.GenerateGrid;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;

/**
 * This Class contains the grid components of the game
 */
public class Grid extends JPanel {
    @Serial
    private static final long serialVersionUID = 1L;
    private final JButton[][] buttons;
    private final Container container;
    private final GridController con;
    GameClient client;

    /**
     * This constructor initializes the grid components
     *
     * @param container contains all the panels and frame used for the game
     */
    public Grid(Container container, GameClient client) {
        this.container = container;
        con = new GridController(container, client);
        setPreferredSize(new Dimension(Constants.GRID_WIDTH, Constants.GRID_HEIGHT));
        setBackground(Color.GREEN);
        setForeground(Color.GREEN);
        setBorder(BorderFactory.createLineBorder(Color.black, 2));
        setLayout(new GridLayout(Constants.ITEM_ROWS, Constants.ITEM_COL, 3, 3));
        buttons = new JButton[Constants.ITEM_ROWS][Constants.ITEM_COL];
        addButtons();
    }

    /**
     * This method adds buttons to the game grid
     */
    public void addButtons() {
        int counter = 0;
        ArrayList<String> numbers = GenerateGrid.getArrayList();
        Constants.CUSTOM = 0;
        for (int r = 0; r < Constants.ITEM_ROWS; r++) {
            for (int c = 0; c < Constants.ITEM_COL; c++) {
                buttons[r][c] = new JButton(numbers.get(counter));
                buttons[r][c].setFont(new Font(Constants.FONT_NAME, Font.BOLD, Constants.FONT_SIZE));
                buttons[r][c].setBackground(Constants.GRIDCOLOR);
                buttons[r][c].setOpaque(true);
                buttons[r][c].setForeground(Constants.TEXTCOLOR);
                buttons[r][c].setFont(new Font("Serif", Font.BOLD, 42));
                buttons[r][c].addActionListener(con);
                if (buttons[r][c].getText().equals(""))
                    buttons[Constants.ITEM_ROWS - 1][Constants.ITEM_ROWS - 1].setBackground(Color.gray);
                add(buttons[r][c]);

                counter++;
            }
        }
    }

    /**
     * This method accesses the grid controller
     */
    public GridController getCon() {
        return con;
    }

    /**
     * This method accesses the container
     */
    public Container getContainer() {
        return container;
    }

    /**
     * This method accesses the grids buttons
     *
     * @return returns the array of buttons
     */
    public JButton[][] getButtons() {
        return buttons;
    }
}
