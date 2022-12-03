package view;

import controller.SettingController;
import model.Constants;
import model.Path;
import model.ScoreReader;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

/**
 * This class contains the components for the setting panel
 */
public class Settings extends JPanel {
    @Serial
    private static final long serialVersionUID = 1L;
    private final GridBagConstraints gbc;
    private final JLabel[] scoreLabel;
    private final JTextArea text;
    private final JRadioButton playGame;
    private final JRadioButton designGame;
    private final Container container;
    private final SettingController con;
    private final JButton pause;
    private final JButton play;
    private final JButton resetGame;
    private final JButton restart;
    private final JSlider slider;
    private final JComboBox<String> difficulty;
    private final JComboBox<String> modeBox;
    private final JButton undoLast;

    /**
     * This constructor initializes the components of the setting panel
     * @param container contains all the panels and frame used for the game
     */
    public Settings(Container container) {
        this.container = container;
        con = new SettingController(container);
        gbc = new GridBagConstraints();
        setPreferredSize(new Dimension(250, 500));
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        scoreLabel = new JLabel[3];

        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;

        setConstraints(2, 0, 0);
        add(new JLabel(new ImageIcon("resources/numpuzz.jpg")), gbc);

        setConstraints(1, 0, 1);
        pause = new JButton("Pause Music");
        pause.addActionListener(con);
        add(pause, gbc);

        setConstraints(1, 1, 1);
        play = new JButton("Play Music");
        play.addActionListener(con);
        add(play, gbc);

        setConstraints(2, 0, 2);
        add(getLabel("Volume"), gbc);

        setConstraints(2, 0, 3);
        slider = new JSlider(-67, 6);
        slider.setBackground(Color.BLACK);
        slider.addChangeListener(con);
        add(slider, gbc);

        setConstraints(1, 0, 4);
        difficulty = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        difficulty.setSelectedIndex(Constants.SELECTION);
        difficulty.addActionListener(con);
        add(difficulty, gbc);

        setConstraints(1, 1, 4);
        modeBox = new JComboBox<>(new String[]{"Numbers", "Letters"});
        modeBox.setSelectedIndex(Constants.GAME_MODE);
        modeBox.addActionListener(con);
        add(modeBox, gbc);

        setConstraints(1, 0, 5);
        add(getLabel("Move List"), gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.weightx = 2;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.gridy = 5;
        undoLast = new JButton("Undo Last Move");
        undoLast.setIcon(new ImageIcon("resources/undo.png"));
        undoLast.setHorizontalTextPosition(JButton.CENTER);
        undoLast.setVerticalTextPosition(SwingConstants.BOTTOM);
        undoLast.addActionListener(con);
        add(undoLast, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.gridy = 6;
        text = new JTextArea("", 4, 3);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);

        JScrollPane scrollMoves = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollMoves.setBounds(3, 3, 50, 50);
        add(scrollMoves, gbc);

        setConstraints(1, 0, 7);
        playGame = new JRadioButton();
        playGame.setSelected(true);
        playGame.setText("Play Game");
        playGame.setForeground(Color.BLACK);
        add(playGame, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        setConstraints(1, 1, 7);
        designGame = new JRadioButton();
        designGame.setText("Design Game");
        designGame.setForeground(Color.BLACK);
        add(designGame, gbc);

        ButtonGroup G = new ButtonGroup();
        G.add(playGame);
        G.add(designGame);

        setConstraints(2, 0, 8);
        resetGame = new JButton("Reset Game with Settings");
        resetGame.addActionListener(con);
        add(resetGame, gbc);

        setConstraints(2, 0, 9);
        JLabel bestScores = new JLabel(new ImageIcon("resources/highscores.gif"));
        add(bestScores, gbc);


        setConstraints(2, 0, 10);
        add(addTitle("resources/easy.png", String.valueOf(ScoreReader.readEasy()), 0), gbc);
        setConstraints(2, 0, 11);
        add(addTitle("resources/medium.png", String.valueOf(ScoreReader.readMedium()), 1), gbc);
        setConstraints(2, 0, 12);
        add(addTitle("resources/hard.png", String.valueOf(ScoreReader.readHigh()), 2), gbc);

        setConstraints(2, 0, 13);
        restart = new JButton("Restart Game");
        restart.setFont(new Font("Rockwell", Font.BOLD, 20));
        restart.setForeground(Color.WHITE);
        restart.setBackground(Color.BLACK);
        restart.setBorderPainted(false);
        restart.setIcon(new ImageIcon("resources/restart.png"));
        restart.setHorizontalTextPosition(JButton.CENTER);
        restart.setVerticalTextPosition(SwingConstants.BOTTOM);
        restart.addActionListener(con);
        add(restart, gbc);
    }

    /**
     * This method adds the high score panel
     * @param easy is the high score for easy mode
     * @param valueOf is the high score from file for easy mode
     * @param index is the index of the ease score
     * @return returns the panel for the title
     */
    private JPanel addTitle(String easy, String valueOf, int index) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.add(new JLabel(new ImageIcon(easy)));
        panel.add(scoreAdd(valueOf, index));
        return panel;
    }

    /**
     * This method adds the scores to the labels
     * @param value is the score
     * @param index is the position of score dependent of difficulty
     * @return
     */
    private JLabel scoreAdd(String value, int index) {
        if (value.equals("0"))
            value = "0000";
        scoreLabel[index] = new JLabel(value);
        scoreLabel[index].setFont(new Font("Cooper Black", Font.BOLD, 21));
        scoreLabel[index].setForeground(Color.RED);
        return scoreLabel[index];
    }

    /**
     * This method accesses the volume label
     * @param name is the volume text
     * @return returns the label for volume
     */
    private JLabel getLabel(String name) {
        JLabel vol = new JLabel(name, SwingConstants.CENTER);
        vol.setForeground(Color.WHITE);
        vol.setAlignmentX(CENTER_ALIGNMENT);
        vol.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        return vol;
    }

    /**
     * This method sets the constraints for the gridbag layout
     * @param width is the grids width
     * @param x is the x-axis location
     * @param y is the y-axis location
     */
    private void setConstraints(int width, int x, int y) {
        gbc.gridwidth = width;
        gbc.gridx = x;
        gbc.gridy = y;
    }

    /**
     * This method accesses the container
     * @return returns the container
     */
    public Container getContainer() {
        return container;
    }

    /**
     * This method accesses the slider component for volume
     * @return returns the slider component
     */
    public JSlider getSlider() {
        return slider;
    }

    /**
     * This method accesses GridBagConstraints
     * @return returns GridBagConstrains object
     */
    public GridBagConstraints getGbc() {
        return gbc;
    }


    /**
     * This class accesses the score label
     * @return returns the array for score label
     */
    public JLabel[] getScoreLabel() {
        return scoreLabel;
    }

    /**
     * This method acceses the JTextArea
     * @return returns the JTextArea text
     */
    public JTextArea getTextArea() {
        return text;
    }

    /**
     * This method accesses the JRadioButton
     * @return returns the JRadioButton object
     */
    public JRadioButton getPlayGame() {
        return playGame;
    }

    /**
     * This method accesses the JRadioButton
     * @return returns the JRadioButton object
     */
    public JRadioButton getDesignGame() {
        return designGame;
    }

    /**
     * This method accesses the SettingController
     * @return returns the SettingController object
     */
    public SettingController getCon() {
        return con;
    }

    /**
     * This class accesses the pause button
     * @return returns the pause
     */
    public JButton getPause() {
        return pause;
    }

    /**
     * This method accesses the play button
     * @return returns the play
     */
    public JButton getPlay() {
        return play;
    }

    /**
     * This method accesses the JcomboBox difficulty
     * @return returns the difficulty
     */
    public JComboBox<String> getDifficulty() {
        return difficulty;
    }

    /**
     * This method accesses the JComboBox modes
     * @return returns the modeBox
     */
    public JComboBox<String> getModeBox() {
        return modeBox;
    }

    /**
     * This method accesses the Jbutton to undo move
     * @return returns the last move
     */
    public JButton getUndoLast() {
        return undoLast;
    }

    /**
     * This method accesses the button to Reset game
     * @return returns reset game
     */
    public JButton getResetGame() {
        return resetGame;
    }

    /**
     * This method accesses the restart button
     * @return returns the restart
     */
    public JButton getRestart() {
        return restart;
    }

}

