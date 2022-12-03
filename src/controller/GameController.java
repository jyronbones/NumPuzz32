package controller;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * game.Main game class, contains majority of functions and variables required to play NumPuzz
 */
public class GameController extends JFrame implements ActionListener {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * variable that sets game mode to use letters
     */
    public static final int LETTERS = 1;
    /**
     * variable that sets game mode to use digits
     */
    public static final int DIGITS = 0;

    private int gameMode;
    /**
     * Holds the highscore for NumPuzz easy mode
     */
    private int easyScore;
    /**
     * Holds the highscore for Numpuzz medium mode
     */
    private int medScore;
    /**
     * Holds the highscore for Numpuzz hard mode
     */
    private int hardScore;
    //    JButton click = new JButton();
    /**
     * This icon is the NumPuzz jpg logo
     */
    private final Icon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("numpuzz.jpg")));
    /**
     * Affixes the NumPuzz icon to a JLabel to be displayed in settings panel
     */
    private final JLabel title_image = new JLabel(icon);
    /**
     * An empty placeholder JLabel for the highest score obtained in NumPuzz
     */
    private final JLabel highestScore = new JLabel((""), SwingConstants.CENTER);
    /**
     * A radio button that will allow "play game" mode to be selected
     */
    private JRadioButton playGame;
    /**
     * A radio button that will allow "design game" mode to be selected
     */
    private JRadioButton designGame;
    /**
     * A JSlider that will be used to control in game music volume
     */
    private final JSlider slider = new JSlider(-70, 6);
    /**
     * A GridLayout that is used for the NumPuzz game tiles
     */
    private final GridLayout game_grid;
    /**
     * A GridLayout that is used for the score grid (player score and high score) used in bottom panel
     */
    private GridLayout score_grid = new GridLayout(2,6,5,5);
    /**
     * A GridBayLayout used to display various settings and scores used in settings panel
     */
    private GridBagLayout settings_grid = new GridBagLayout();
    /**
     * Creates a GridBagConstrains object used for manipulating various buttons and cells in GridBagLayout
     */
    private final GridBagConstraints gbc = new GridBagConstraints();
    /**
     * A 2-D Array of JButtons that holds the game tiles
     */
    private JButton[][] b;
    /**
     * A counter for number of player moves, used to calculate player score
     */
    private int numMoves = 0;
    /**
     * JLables used to display player score and time elapsed in game
     */
    private JLabel playerScore, time;
    /**
     * A JTextArea used for displaying player moves
     */
    private JTextArea text;
    /**
     * numbers will hold an array of numbers that is used to generate a numpuzz game
     */
    private ArrayList<String> numbers = new ArrayList<>();
    private ArrayList<String> charSolution = new ArrayList<>();


    /**
     * Audio clip for in game music
     */
    private Clip clip;
    /**
     * Various int values used to hold game information such as game size, nunmber of rows and columns and user
     * selection for settings
     */
    private int lastR, lastC, size, rows, cols, selection;
    /**
     * emptyCellColor used for blank tile in NumPuzz game
     */
    private final Color emptyCellColor = Color.gray;
    /**
     * Tile color used for non-blank tile in NumPuzz game
     */
    private Color tileColour = Color.WHITE;
    /**
     * Tile text color used for non-blank tile in NumPuzz game
     */
    private Color textColour = Color.BLACK;
    /**
     * moveList will hold all player moves during a game, used for displaying in text box and undoing a move
     */
    private ArrayList<String> moveList = new ArrayList<>();
    /**
     * Writer that saves high scores to a file
     */
    private Writer wr;
    JPanel container;
    JPanel panel_01;


    /**
     * Default constructor for NumPuzz Game, sets game grid to 3x3 with randomly generated set of numbers
     */
    public GameController()
    {
        this(new JButton[3][3], new GridLayout(3, 3,3,3), 9, 3, 3, 0,DIGITS, false, null, null, Color.WHITE, Color.BLACK);

    }

    /**
     * Parameterized constructor for NumPuzz Game, allows custom settings to be used for different difficulties and modes
     *
     * @param b a two-dimensional array of JButtons that make up the game grid
     * @param game_grid an instance of GridLayout that allows the JButtons to be arranged in a grid
     * @param size an integer for game size, or how many squares will make up a game (9,16 or 25)
     * @param rows number of rows that will make up the game grid
     * @param cols number of columns that will make up the game grid
     * @param selection integer that tracks user selection for different settings
     * @param restart boolean that determines if a game uses different or not
     * @param sameNumbers if a game is restarted the same numbers will be used
     */
    public GameController(JButton[][] b, GridLayout game_grid, int size, int rows, int cols, int selection, int gameMode, boolean restart, ArrayList<String> sameNumbers, ArrayList<String> charSolution, Color tileColour, Color textColour)
    {
        this.selection = selection;
        this.b = b;
        this.size = size;
        this.rows = rows;
        this.cols = cols;
        this.game_grid = game_grid;
        this.gameMode =  gameMode;
        this.charSolution = charSolution;
        this.tileColour = tileColour;
        this.textColour = textColour;

        String[] filenames = new String[] {"easy_score.txt", "med_score.txt", "hard_score.txt"};
        for(int i = 0; i < filenames.length; i++) {
            try {
                BufferedReader reader =new BufferedReader(new FileReader(filenames[i]));

                String line;

                while ((line = reader.readLine()) != null) {
                    int In_Value = Integer.parseInt(line);

                    if(i == 0) {
                        easyScore = In_Value;
                        if(selection == 0) {
                            highestScore.setText(Integer.toString(In_Value));
                        }
                    }
                    else if(i == 1) {
                        medScore = In_Value;
                        if(selection == 1) {
                            highestScore.setText(Integer.toString(In_Value));
                        }
                    }
                    else {
                        hardScore = In_Value;
                        if(selection == 2) {
                            highestScore.setText(Integer.toString(In_Value));
                        }
                    }
                }
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
        if (!restart) {

            if (gameMode == DIGITS) {
                while (true) {
                    for (int i = 1; i < size; i++) {
                        numbers.add(Integer.toString(i));
                    }
                    Collections.shuffle(numbers);
                    numbers.add("0");
                    boolean _isSolvable = isSolvable(numbers, selection, cols);
                    if (_isSolvable) {
                        break;
                    } else {
                        numbers.clear();
                    }

                }
            }
            else {
                char asci = 'a';
                for (int i = 1; i < size; i++) {
                    numbers.add(Character.toString (asci));
                    asci++;
                }
                Collections.shuffle(numbers);
                numbers.add("0");
            }
        }
        else {
            numbers = sameNumbers;
        }


        setTitle("NumPuzz"); // window title
        setSize(800, 800); // window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // default close option

        URL iconURL = getClass().getResource("puzzle.png");
        // iconURL is null when not found
        assert iconURL != null;
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage()); // window icon

        // container panel
        /**
         * Primary JPanel which will act as a container for NumPuzz game
         */
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        // panel_01
        /**
         * This panel will contain the grid of squares that make up NumPuzz game
         */
        panel_01 = new JPanel();
        container.add(panel_01);
        panel_01.setPreferredSize(new Dimension(500,200));
        panel_01.setForeground(Color.green);
        panel_01.setBackground(Color.green);
        panel_01.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        panel_01.setLayout(game_grid);
        addButtons(panel_01); // add buttons to panel 1
        add(panel_01, BorderLayout.CENTER);

        // panel_02
        /**
         * This panel will contain the settings buttons and functions for the NumPuzz game
         */
        JPanel panel_02 = new JPanel();
        container.add(panel_02);
        panel_02.setPreferredSize(new Dimension(250, 500));
        panel_02.setLayout(settings_grid);
        panel_02.setBackground(Color.BLACK);
        add(panel_02, BorderLayout.EAST);
        addSettings(panel_02); // add settings to panel 2


        // bottom_panel
        /**
         * This panel will contain the high score and current score for the NumPuzz game
         */
        JPanel bottom_panel = new JPanel();
        container.add(bottom_panel);
        bottom_panel.setPreferredSize(new Dimension(100,75));
        bottom_panel.setBackground(Color.BLACK);
        add(bottom_panel, BorderLayout.SOUTH);
        bottom_panel.setLayout(score_grid);
        highestScore.setForeground(Color.YELLOW);
        highestScore.setFont(new Font("Cooper Black", Font.BOLD, 21));



        //text.setFont(new Font("Serif", Font.PLAIN, 21)); //text ref
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 3; x++) {
                if (y == 0 && x == 0) {
                    Icon player_score_icon = new ImageIcon(Objects.requireNonNull(getClass().
                            getResource("pscore.jpg")));
                    JLabel pscore_image = new JLabel(player_score_icon, SwingConstants.CENTER);
                    bottom_panel.add(pscore_image);
                }
                else if (y == 1 && x == 0) {
                    playerScore = new JLabel(Integer.toString(numMoves), SwingConstants.CENTER);
                    playerScore.setForeground(Color.YELLOW);
                    playerScore.setFont(new Font("Cooper Black", Font.BOLD, 21));
                    bottom_panel.add(playerScore);
                }
                else if (y == 0 && x == 1) {
                    Icon high_score = new ImageIcon(Objects.requireNonNull(getClass().
                            getResource("bscore.jpg")));
                    JLabel hscore_image = new JLabel(high_score, SwingConstants.CENTER);
                    bottom_panel.add(hscore_image);
                }
                else if (y == 1 && x == 1) {
                    bottom_panel.add(highestScore);
                }
                else if (y == 0) {
                    Icon timer = new ImageIcon(Objects.requireNonNull(getClass().
                            getResource("timer.gif")));
                    JLabel timer_image = new JLabel(timer, SwingConstants.CENTER);
                    bottom_panel.add(timer_image);
                }
                else {
                    time = new JLabel(); // timer
                    AtomicInteger seconds = new AtomicInteger();
                    Timer t = new Timer(1000, this);
                    t.addActionListener(e->{
                        time.setText(Integer.toString(seconds.get()));
                        seconds.addAndGet(1);
                    });
                    time.setFont(new Font("Rockwell", Font.BOLD, 20));
                    t.start();
                    time.setForeground(Color.WHITE);
                    time.setHorizontalAlignment(SwingConstants.CENTER);
                    bottom_panel.add(time);
                }
            }
        }

        // menu bar
        setJMenuBar(menu());
        SwingUtilities.invokeLater(StartSplash::new);
        setLocationRelativeTo(null); // center game on screen
        setVisible(true); //show frame

    }

    /**
     * Function that adds JButtons to panel 01, which make up the game grid for NumPuzz
     * @param panel_01  JPanel that holds game grid and NumPuzz JButtons
     */
    public void addButtons(JPanel panel_01) {
        int counter = 0;
        for(int r = 0; r < rows; r++)
        {
            for(int c = 0; c < cols; c++)
            {
                String button_index = numbers.get(counter);
                b[r][c] = new JButton(button_index);
                b[r][c].setFont(new Font("Serif", Font.BOLD, 42));
                int finalR = r;
                int finalC = c;

                b[rows - 1][cols - 1] = new JButton("");
                b[r][c].addActionListener(e -> {
                    System.out.println("test clicks" + e.getActionCommand());
                    System.out.printf("The row is %d and the col is %d\n", finalR, finalC);

                    MoveTile(b, finalR, finalC, emptyCellColor, false);

                    playerScore.setText(Integer.toString(numMoves));
                    text.setText(ArrayListToString());


                    try {
                        winCheck(b);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }


                });
                b[r][c].setBackground(getTileColour());
                b[r][c].setForeground(getTextColour());
                b[rows - 1][cols - 1].setBackground(emptyCellColor);
                b[r][c].setFont(new Font("Serif", Font.BOLD, 42));

                panel_01.add(b[r][c]);
                counter++;
            }
        }
    }

    /**
     * Function that adds NumPuzz game settings to JPanel 02
     * @param panel_02 JPanel that contains JButtons and Labels for NumPuzz settings
     */
    public void addSettings(JPanel panel_02) {
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel_02.add(title_image, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JButton pause = new JButton("Pause Music");
        panel_02.add(pause,gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        JButton play = new JButton("Play Music");
        panel_02.add(play,gbc);

        gbc.gridwidth = 2; // volume label
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel vol = new JLabel("Volume", SwingConstants.CENTER);
        vol.setForeground(Color.WHITE);
        vol.setAlignmentX(CENTER_ALIGNMENT);
        vol.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        panel_02.add(vol,gbc);

        gbc.gridx = 0; // volume slider
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel_02.add(slider, gbc);
        slider.setBackground(Color.BLACK);
        slider.addChangeListener(e -> {
            FloatControl fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            float currentVolume = slider.getValue();
            fc.setValue(currentVolume);
        });

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 4;
        String[] difficulties = { "Easy", "Medium", "Hard"};
        JComboBox<String> difficultyComboBox = new JComboBox<>(difficulties);
        if(selection == 0)
            difficultyComboBox.setSelectedIndex(0);
        else if(selection == 1)
            difficultyComboBox.setSelectedIndex(1);
        else
            difficultyComboBox.setSelectedIndex(2);
        difficultyComboBox.addActionListener(e->GameDifficulty(Objects.requireNonNull(difficultyComboBox.getSelectedItem()).toString()));
        panel_02.add(difficultyComboBox,gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.gridy = 4;
        String[] modes = { "Numbers", "Letters"};
        JComboBox<String> modeBox = new JComboBox<>(modes);
        modeBox.setSelectedIndex(0);
        modeBox.addActionListener(this);
        if(gameMode == 0)
            modeBox.setSelectedIndex(0);
        else
            modeBox.setSelectedIndex(1);
        modeBox.addActionListener(e->GameMode(Objects.requireNonNull(modeBox.getSelectedItem()).toString()));
        panel_02.add(modeBox,gbc);


        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel moveLabel = new JLabel("Move List", SwingConstants.CENTER);
        moveLabel.setForeground(Color.WHITE);
        moveLabel.setAlignmentX(CENTER_ALIGNMENT);
        moveLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
        panel_02.add(moveLabel,gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.weightx = 3;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton undoLast = new JButton("Undo Last Move");
        try {
            Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("undo.png")));
            undoLast.setIcon(new ImageIcon(img));
            undoLast.setHorizontalTextPosition(JButton.CENTER);
            undoLast.setVerticalTextPosition(SwingConstants.BOTTOM);
        } catch (Exception ex) {
//            System.out.println(ex);
        }
        undoLast.addActionListener(e-> {
            UndoLastMove();
            text.setText(ArrayListToString());
            MoveTile(b,lastR, lastC, emptyCellColor, true);
        });

        panel_02.add(undoLast,gbc);


        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.gridy = 6;
        text = new JTextArea(ArrayListToString(), 3,3);
        text.setLineWrap( true );
        text.setWrapStyleWord( true );

        JScrollPane scrollMoves = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollMoves.setBounds(3,3,50,50);
        panel_02.add(scrollMoves,gbc);


        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        playGame = new JRadioButton();
        playGame.setSelected(true);
        playGame.setText("Play Game");
        panel_02.add(playGame,gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        designGame = new JRadioButton();
        designGame.setText("Design Game");
        panel_02.add(designGame,gbc);

        ButtonGroup G = new ButtonGroup();
        G.add(playGame);
        G.add(designGame);



        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        JButton resetGame = new JButton("Reset Game with Settings");
        resetGame.addActionListener(e->RestartWithSettings());
        panel_02.add(resetGame,gbc);


        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        URL url = this.getClass().getResource("highscores.gif");
        assert url != null;
        Icon high_score_img = new ImageIcon(url);
        JLabel bestScores = new JLabel(high_score_img);
        panel_02.add(bestScores,gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        JLabel easyTitle = new JLabel();
        try {
            Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("easy.png")));
            easyTitle.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
//            System.out.println(ex);
        }
        panel_02.add(easyTitle,gbc);

        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        JLabel easyHigh;
        if(easyScore > 0) {
            easyHigh = new JLabel(String.valueOf(easyScore));
        }
        else {
            easyHigh = new JLabel("0000");
        }
        easyHigh.setFont(new Font("Cooper Black", Font.BOLD, 21));
        easyHigh.setForeground(Color.RED);
        panel_02.add(easyHigh,gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        JLabel medTitle = new JLabel();
        try {
            Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("medium.png")));
            medTitle.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
//            System.out.println(ex);
        }
        panel_02.add(medTitle,gbc);

        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        JLabel medHigh;
        if(medScore > 0) {
            medHigh = new JLabel(String.valueOf(medScore));
        }
        else {
            medHigh = new JLabel("0000");
        }
        medHigh.setFont(new Font("Cooper Black", Font.BOLD, 21));
        medHigh.setForeground(Color.RED);
        panel_02.add(medHigh,gbc);

        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        JLabel hardTitle = new JLabel();
        try {
            Image img = ImageIO.read(Objects.requireNonNull(getClass().getResource("hard.png")));
            hardTitle.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
//            System.out.println(ex);
        }
        panel_02.add(hardTitle,gbc);

        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        JLabel hardHigh;
        if(hardScore > 0) {
            hardHigh = new JLabel(String.valueOf(hardScore));
        }
        else {
            hardHigh = new JLabel("0000");
        }
        hardHigh.setFont(new Font("Cooper Black", Font.BOLD, 21));
        hardHigh.setForeground(Color.RED);
        panel_02.add(hardHigh,gbc);


        // restart button
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        JButton restart = new JButton("Restart Game");
        restart.setFont(new Font("Rockwell", Font.BOLD, 20));
        restart.setForeground(Color.WHITE);
        restart.setBackground(Color.BLACK);
        restart.setBorderPainted(false);
        try {
            Image restartImg = ImageIO.read(Objects.requireNonNull(getClass().getResource("restart.png")));
            restart.setIcon(new ImageIcon(restartImg));
            restart.setHorizontalTextPosition(JButton.CENTER);
            restart.setVerticalTextPosition(SwingConstants.BOTTOM);
        } catch (Exception ex) {
//            System.out.println(ex);
        }
        panel_02.add(restart,gbc);
        restart.addActionListener(e-> RestartGame());


        this.clip = PlayGameSound("jeopardy.wav");
        AtomicLong clipTimePosition = new AtomicLong();
        pause.addActionListener(e -> {
            clipTimePosition.set(clip.getMicrosecondPosition());
            clip.stop();
        });
        play.addActionListener(e-> {
                    clip.setMicrosecondPosition(clipTimePosition.get());
                    clip.start();
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
        );
    }

    /**
     * Function that creates a JMenuBar and adds in NumPuzz functionality like saving, loading, and creating
     * a new game
     * @return returns a JMenuBar that contains game functionality like saving and loading files
     */
    public JMenuBar menu() {
        JMenuBar menu = new JMenuBar();
        JMenu file,edit,help;

        // File menu
        JSeparator file_sep3 = new JSeparator();
        JSeparator file_sep2 = new JSeparator();
        file= new JMenu("File");
        menu.add(file);

        JMenuItem save, load, exit, newGame;
        newGame = new JMenuItem("New Game" );
        file.add(newGame);
        ImageIcon i = new ImageIcon((Objects.requireNonNull(getClass().getResource("new.png"))));
        Image image = i.getImage();
        Image newimg = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        i = new ImageIcon(newimg);
        newGame.setIcon(i);
        file.add(file_sep2);

        save = new JMenuItem("Save");
        file.add(save);
        ImageIcon j = new ImageIcon((Objects.requireNonNull(getClass().getResource("save.png"))));
        Image image2 = j.getImage();
        Image newimg2 = image2.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        j = new ImageIcon(newimg2);
        save.setIcon(j);


        load = new JMenuItem("Load");
        file.add(load);
        file.add(file_sep3);
        ImageIcon k = new ImageIcon((Objects.requireNonNull(getClass().getResource("load.png"))));
        Image image3 = k.getImage();
        Image newimg3 = image3.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        k = new ImageIcon(newimg3);
        load.setIcon(k);


        newGame.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to start a new game?",
                    "New Game",  JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION)
            {
                clip.stop();
                dispose();
                new GameController(b, game_grid, size, rows, cols, selection,gameMode, false, null, charSolution, tileColour, textColour);
            }
        });

        save.addActionListener(e -> {
            try {
                saveGame();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        load.addActionListener(e -> {
            try {
                loadGame();
            } catch ( FileNotFoundException fne) {
                JOptionPane.showMessageDialog(this, "There is no saved game",
                        "No saved game", JOptionPane.WARNING_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        exit = new JMenuItem("Exit");
        ImageIcon l = new ImageIcon((Objects.requireNonNull(getClass().getResource("exit.png"))));
        Image image4 = l.getImage(); // transform it
        Image newimg4 = image4.getScaledInstance(20, 20, Image.SCALE_SMOOTH); // scale it the smooth way
        l = new ImageIcon(newimg4);  // transform it back
        exit.setIcon(l);
        file.add(exit);

        exit.addActionListener(e -> System.exit(0));

        // Edit menu
        JSeparator editSep1 = new JSeparator();
        edit = new JMenu("Edit");
        menu.add(edit);

        JMenuItem undo;
        undo = new JMenuItem("Undo");
        edit.add(undo);

        undo.addActionListener(e-> {
            UndoLastMove();
            text.setText(ArrayListToString());
            MoveTile(b,lastR, lastC, emptyCellColor, true);
        });

        edit.add(editSep1);
        // tile colour
        JMenu tileColor = new JMenu("Tile Colour");
        edit.add(tileColor);
        JMenuItem whiteTile = new JMenuItem("White");
        tileColor.add(whiteTile);
        whiteTile.addActionListener(e-> {setTileColour(Color.WHITE);
            clip.stop();
            dispose();
            new GameController(b, game_grid, size, rows, cols, selection, gameMode, false, null, charSolution, tileColour, textColour);
        });
        JMenuItem redTile = new JMenuItem("Red");
        tileColor.add(redTile);
        redTile.addActionListener(e-> {setTileColour(Color.RED);
            clip.stop();
            dispose();
            new GameController(b, game_grid, size, rows, cols, selection, gameMode, false, null, charSolution, tileColour, textColour);
        });
        JMenuItem blueTile = new JMenuItem("Blue");
        tileColor.add(blueTile);
        blueTile.addActionListener(e-> {setTileColour(Color.BLUE);
            clip.stop();
            dispose();
            new GameController(b, game_grid, size, rows, cols, selection, gameMode, false, null, charSolution, tileColour, textColour);
        });
        JMenuItem yellowTile = new JMenuItem("Yellow");
        tileColor.add(yellowTile);
        yellowTile.addActionListener(e-> {setTileColour(Color.YELLOW);
            clip.stop();
            dispose();
            new GameController(b, game_grid, size, rows, cols, selection, gameMode, false, null, charSolution, tileColour, textColour);
        });

        JMenu textColor = new JMenu("Text Colour");
        edit.add(textColor);
        JMenuItem whiteText = new JMenuItem("White");
        textColor.add(whiteText);
        whiteText.addActionListener(e-> {setTextColour(Color.WHITE);
            clip.stop();
            dispose();
            new GameController(b, game_grid, size, rows, cols, selection, gameMode, false, null, charSolution, tileColour, textColour);
        });
        JMenuItem redText = new JMenuItem("Red");
        textColor.add(redText);
        redText.addActionListener(e-> {setTextColour(Color.RED);
            clip.stop();
            dispose();
            new GameController(b, game_grid, size, rows, cols, selection, gameMode, false, null, charSolution, tileColour, textColour);
        });
        JMenuItem blueText = new JMenuItem("Blue");
        textColor.add(blueText);
        blueText.addActionListener(e-> {setTextColour(Color.BLUE);
            clip.stop();
            dispose();
            new GameController(b, game_grid, size, rows, cols, selection, gameMode, false, null, charSolution, tileColour, textColour);
        });
        JMenuItem yellowText = new JMenuItem("Yellow");
        textColor.add(yellowText);
        yellowText.addActionListener(e-> {setTextColour(Color.YELLOW);
            clip.stop();
            dispose();
            new GameController(b, game_grid, size, rows, cols, selection, gameMode, false, null, charSolution, tileColour, textColour);
        });


        // Help menu
        JSeparator help_sep = new JSeparator();
        help = new JMenu("Help");
        menu.add(help);
        JMenuItem instructions, credits;
        instructions = new JMenuItem("Instructions");
        help.add(instructions);
        help.add(help_sep);

        instructions.addActionListener(e -> JOptionPane.showMessageDialog(
                this,"Hello there! Welcome to NumPuzz",
                "Instructions", JOptionPane.PLAIN_MESSAGE));


        credits = new JMenuItem("Credits");
        help.add(credits);

        int copyrightSymbolCodePoint = 169;
        String s = Character.toString( copyrightSymbolCodePoint );
        credits.addActionListener(e -> JOptionPane.showMessageDialog(
                this,"NumPuzz created by:\nByron Jones & Sean Wray\n" +
                        "Copyright " + s + "2022", "Credits", JOptionPane.PLAIN_MESSAGE));

        return menu;
    }

    /**
     * Function that creates the game grid with correct number of JButtons, rows and columns based on difficulty
     * selected by the user. For example if easy is selected, a game grid of 3 rows x 3 columns is created with 9
     * JButtons
     * @param difficulty takes in a string value that determines game difficulty, either easy, medium or hard
     */
    public void GameDifficulty(String difficulty) {
        JButton[][] b = new JButton[0][];
        GridLayout game_grid = null;
        int size = 0;
        int rows = 0;
        int cols = 0;
        int selection = 0;
        if(Objects.equals(difficulty, "Easy")) {
            b = new JButton[3][3];
            game_grid = new GridLayout(3, 3,3,3);
            size = 9;
            rows = 3;
            cols = 3;
        }
        else if (Objects.equals(difficulty, "Medium")) {
            b = new JButton[4][4];
            game_grid = new GridLayout(4, 4,3,3);
            size = 16;
            rows = 4;
            cols = 4;
            selection = 1;
        }
        else if (Objects.equals(difficulty, "Hard")) {
            b = new JButton[5][5];
            game_grid = new GridLayout(5, 5,3,3);
            size = 25;
            rows = 5;
            cols = 5;
            selection = 2;
        }
        clip.stop();
        dispose();
        new GameController(b, game_grid, size, rows, cols, selection,gameMode, false, null, charSolution, tileColour, textColour);
    }


    public void GameMode(String gameModeVal) {
        if(Objects.equals(gameModeVal, "Numbers")) {
         gameMode = DIGITS;
        }
        else if (Objects.equals(gameModeVal, "Letters")) {
         gameMode = LETTERS;
        }

        clip.stop();
        dispose();
        new GameController(b, game_grid, size, rows, cols, selection, gameMode, false, null, charSolution, tileColour, textColour);
    }





    /**
     * Function that allows tiles to move when clicked on provided there is an open space on the
     * game grid. Moves are tracked and added to an ArrayList, which also allows moves to be undone
     * @param b the two-dimensional array of JButtons that makes up the game grid
     * @param r number of rows in current game grid
     * @param c number of columns in current game grid
     * @param emptyColour colour for empty/blank JButton game grid button
     * @param undo used for undoing a player move
     */
    public void MoveTile(JButton[][] b, int r, int c, Color emptyColour, boolean undo) {
        String move;
        try {
            if(Objects.equals(b[r - 1][c].getText(), "")) {
                String temp = b[r][c].getActionCommand();
                b[r][c].setText("");
                b[r - 1][c].setText(temp);
                b[r][c].setBackground(emptyColour);
                b[r - 1][c].setBackground(getTileColour());
                if(!undo) {
                    this.lastR = r - 1;
                    this.lastC = c;
                    numMoves++;
                    move = String.format("%s to row %d:col %d", temp, lastR + 1, lastC + 1);
                    moveList.add(move);
                }
            }
        }
        catch (NullPointerException | ArrayIndexOutOfBoundsException ne) {
//            ne.printStackTrace();
        }
        try {
            if(Objects.equals(b[r][c + 1].getText(), "")){
                String temp = b[r][c].getActionCommand();
                b[r][c].setText("");
                b[r][c + 1].setText(temp);
                b[r][c].setBackground(emptyColour);
                b[r][c + 1].setBackground(getTileColour());
                if(!undo) {
                    this.lastR = r;
                    this.lastC = c + 1;
                    numMoves++;
                    move = String.format("%s to row %d:col %d", temp, lastR + 1, lastC + 1);
                    moveList.add(move);
                }
            }
        }
        catch (NullPointerException | ArrayIndexOutOfBoundsException ne) {
//            ne.printStackTrace();
        }
        try {
            if (Objects.equals(b[r + 1][c].getText(), "")) {
                String temp = b[r][c].getActionCommand();
                b[r + 1][c].setText(temp);
                b[r][c].setText("");
                b[r][c].setBackground(emptyColour);
                b[r + 1][c].setBackground(getTileColour());
                if(!undo) {
                    this.lastR = r + 1;
                    this.lastC = c;
                    numMoves++;
                    move = String.format("%s to row %d:col %d", temp, lastR + 1, lastC + 1);
                    moveList.add(move);
                }
            }
        }
        catch (NullPointerException | ArrayIndexOutOfBoundsException ne) {
//          ne.printStackTrace();
        }
        try {
            if(Objects.equals(b[r][c - 1].getText(), "")){
                String temp = b[r][c].getActionCommand();
                b[r][c - 1].setText(temp);
                b[r][c].setText("");
                b[r][c].setBackground(emptyColour);
                b[r][c - 1].setBackground(getTileColour());
                if(!undo) {
                    this.lastR = r;
                    this.lastC = c - 1;
                    numMoves++;
                    move = String.format("%s to row %d:col %d", temp, lastR + 1, lastC + 1);
                    moveList.add(move);
                }
            }
        }
        catch (NullPointerException | ArrayIndexOutOfBoundsException ne) {
            //            ne.printStackTrace();
        }
    }

    /**
     * Checks if the current NumPuzz game satisfies the win conditions using a simple array check.
     * If the game is won then a pop up appears congratulating the player and saving their scores.
     * @param b Two-dimensional array of JButtons that make up the NumPuzz game grid
     * @throws IOException Throws an exception if string does not care
     */
    public void winCheck(JButton[][] b) throws IOException {
        ArrayList<String> winCheck = new ArrayList<>();
        if (gameMode == DIGITS) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    winCheck.add(b[i][j].getActionCommand());
                }
            }
            int arrayCounter = 1;
            int _num = 0;
            for (String num : winCheck) {
                try {
                    _num = Integer.parseInt(num);
                } catch (NumberFormatException nfe) {
                    //            ne.printStackTrace();
                }
                if (_num == arrayCounter) {
                    arrayCounter++;
                    if (arrayCounter == winCheck.size()) {
                        if (selection == 0 && (easyScore == 0 || easyScore > numMoves)) {
                            wr = new FileWriter("easy_score.txt", false);
                            wr.write(numMoves + "");
                            wr.close();
                        } else if (selection == 1 && medScore == 0 || (medScore > numMoves)) {
                            wr = new FileWriter("med_score.txt", false);
                            wr.write(numMoves + "");
                            wr.close();
                        } else if (selection == 2 && (hardScore == 0 || hardScore > numMoves)) {
                            wr = new FileWriter("hard_score.txt", false);
                            wr.write(numMoves + "");
                            wr.close();
                        }
                        highestScore.setForeground(Color.YELLOW);
                        highestScore.setFont(new Font("Cooper Black", Font.BOLD, 21));
                        SwingUtilities.invokeLater(WinSplash::new);
                        WinPopUp(clip);
                    }
                } else {
                    break;
                }
            }
        }
        else {
            if (charSolution == null) {
                charSolution = (ArrayList<String>)numbers.clone();
                Collections.sort(charSolution);
            }
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    winCheck.add(b[i][j].getActionCommand());
                }
            }
            char arrayCounter = 1;
            for (int i = 0; i < size - 2;i++) {
                if(winCheck.get(i).equals(charSolution.get(i))){
                    arrayCounter++;
                }
                if(arrayCounter == size - 2 && winCheck.get(winCheck.size() - 1).equals("")){
                    SwingUtilities.invokeLater(WinSplash::new);
                    WinPopUp(clip);
                }
            }


        }
    }

    /**
     * Converts the ArrayList of player moves to a String with line breaks
     * after each move. Returns the string
     * @return String of all player moves
     */
    public String ArrayListToString() {
        StringBuilder sb = new StringBuilder();
        String[] moves = moveList.toArray(new String[0]);
        for(int i = 0; i < moves.length; i++) {
            if(i != moves.length - 1) {
                sb.append(moves[i]).append("\n");
            }
            else {
                sb.append(moves[i]);
            }
        }
        return sb.toString();
    }

    /**
     * A function to undo the last player move. It uses the moveList ArrayList and sets
     * the game board to position size - 1. If there is no move to undo a dialog pop up appears.
     */
    public void UndoLastMove() {
        char rChar;
        char cChar;
        if (moveList.size() > 0) {
            String lastMoveString = moveList.get(moveList.size() - 1);
            String lastMove = lastMoveString.substring(lastMoveString.indexOf("to") + 1);
            String intValue = lastMove.replaceAll("[^0-9]", "");

            rChar = intValue.charAt(0);
            cChar = intValue.charAt(1);

            lastR = Character.getNumericValue(rChar) - 1;
            lastC = Character.getNumericValue(cChar) - 1;

            moveList.remove(moveList.size() - 1);
        }
        else {
            JOptionPane.showMessageDialog(this, "No move to undo",
                    "Cannot Undo", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Function that allows a new game to be started with user selected settings, such as setting the
     * mode, difficulty and design game or play game
     */
    public void RestartWithSettings(){
        ArrayList<String> numsList;
        ArrayList<Integer> tempInt = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        String inputString = null;
        if(playGame.isSelected()) {
            clip.stop();
            dispose();
            new GameController(b, game_grid, size, rows, cols, selection,gameMode, true, numbers, charSolution, tileColour, textColour);
        }

        else if(designGame.isSelected() && gameMode == DIGITS) {
            try {
                if(selection == 0) {
                    inputString = JOptionPane.showInputDialog(this,
                            """
                                    Enter series of numbers for easy difficulty
                                    example: 1,3,2,8,4,5,6,7,0
                                    NOTE: Easy is 9 numbers
                                    0 is used for blank tile and MUST be placed last""",
                            "Custom Easy Number Design", JOptionPane.INFORMATION_MESSAGE);
                } else if(selection == 1) {
                    inputString = JOptionPane.showInputDialog(this,
                            """
                                    Enter series of numbers for medium difficulty
                                    example: 15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0
                                    NOTE: Medium is 16 numbers
                                    0 is used for blank tile and MUST be placed last""",
                            "Custom Medium Numbers Design", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    inputString = JOptionPane.showInputDialog(this,
                            """
                                    Enter series of numbers for current hard difficulty
                                    example: 24,23,22,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0
                                    NOTE: Hard is 25 numbers
                                    0 is used for blank tile and MUST be placed last""",
                            "Custom Hard Numbers Design", JOptionPane.INFORMATION_MESSAGE);
                }
                String[] str = inputString.split(",");

                temp.addAll(Arrays.asList(str));
                numsList = temp;
                for(String stringValue: temp) {
                    tempInt.add(Integer.parseInt(stringValue));
                }
                Collections.sort(tempInt);
                for(int i = 0; i < tempInt.size() - 1; i++) {
                    int j = i + 1;
                    if(tempInt.get(j) < tempInt.get(i) || tempInt.get(j) != tempInt.get(i) + 1) {
                        throw new Exception();
                    }
                }
                if(Objects.equals(numsList.get(numsList.size() - 1), "0") && numsList.contains("1") && numsList.size() == size) {
                    numbers = numsList;
                    clip.stop();
                    dispose();
                    new GameController(b, game_grid, size, rows, cols, selection, gameMode, true, numsList, charSolution, tileColour, textColour);
                }
                else {
                    throw new Exception();
                }

            }catch(Exception e) {
                if(inputString != null)
                    JOptionPane.showMessageDialog(this,
                            "Invalid number sequence, please try again",
                            "Invalid Numbers", JOptionPane.WARNING_MESSAGE);
            }
        }
        else {
            try {
                if(selection == 0) {
                    inputString = JOptionPane.showInputDialog(this,
                            """
                                    Enter series of characters for easy difficulty
                                    example: a,b,c,d,e,f,g,h,0
                                    NOTE: Easy is 9 characters
                                    0 is used for blank tile and MUST be placed last""",
                            "Custom Easy characters Design", JOptionPane.INFORMATION_MESSAGE);
                } else if(selection == 1) {
                    inputString = JOptionPane.showInputDialog(this,
                            """
                                    Enter series of characters for medium difficulty
                                    example: a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,0
                                    NOTE: Medium is 16 characters
                                    0 is used for blank tile and MUST be placed last""",
                            "Custom Medium characters Design", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    inputString = JOptionPane.showInputDialog(this,
                            """
                                    Enter series of characters for current hard difficulty
                                    example: a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,0
                                    NOTE: Hard is 25 characters
                                    0 is used for blank tile and MUST be placed last""",
                            "Custom Hard characters Design", JOptionPane.INFORMATION_MESSAGE);
                }
                String[] str = inputString.split(",");

                //TO DO CHANGE TO CHARS LOGIC BELOW

                temp.addAll(Arrays.asList(str));
                if(Objects.equals(temp.get(temp.size() - 1), "0") && temp.size() == size) {
                    numbers = (ArrayList<String>)temp.clone();
                    charSolution = temp;
                    numbers.remove(numbers.size()-1);
                    Collections.shuffle(numbers);
                    numbers.add("0");
                    clip.stop();
                    dispose();
                    new GameController(b, game_grid, size, rows, cols, selection, gameMode, true, numbers, charSolution, tileColour, textColour);
                }
                else {
                    throw new Exception();
                }

            }catch(Exception e) {
                if(inputString != null)
                    JOptionPane.showMessageDialog(this,
                            "Invalid character sequence, please try again",
                            "Invalid Characters", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Function that allows game sounds to be played during gameplay
     * @param filename filename of audio file
     */
    public static void PlaySound(String filename) {
        try (InputStream is = GameController.class.getResourceAsStream(filename)) {
            assert is != null;
            InputStream bufferedIn = new BufferedInputStream(is);
            try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn)) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Function that allows game music to be played continuously during gameplay
     * @param filename filename of audio file to be played
     * @return returns audio clip
     */
    public static Clip PlayGameSound(String filename) {
        try (InputStream is = GameController.class.getResourceAsStream(filename)) {
            assert is != null;
            InputStream bufferedIn = new BufferedInputStream(is);
            try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn)) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
                return clip;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Function that confirms user wants to restart a game after clicking restart button. If yes is selected
     * the game restarts with the same number layout.
     */
    public void RestartGame() {
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to restart?",
                "Restart Game",  JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION)
        {
            clip.stop();
            dispose();
            new GameController(b, game_grid, size, rows, cols, selection, gameMode, true, numbers, charSolution, tileColour, textColour);
        }
    }

    /**
     * Function that will display a winning image pop up if win condition is satisfied
     * @param clip audio file containing win sound effect
     */
    public void WinPopUp(Clip clip) {
        int choice = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "WINNER",  JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION)
        {
            clip.stop();
            dispose();
            new GameController(b, game_grid, size, rows, cols, selection, gameMode, false, null, charSolution, tileColour, textColour);
        }
    }

    // A utility function to count
    // inversions in given array 'arr[]'

    /**
     * A function that counts the number of inversions in a given number puzzle number set. This will
     * help determine if the number set is solvable or not, for example if a 3x3 number set has an odd
     * number of inversions then it is not solvable.
     * @param arr  array of numbers that make up a NumPuzz game
     * @param c  the number of columns (and rows as each puzzle is a square)
     * @return returns the inversion count, used in isSolvable()
     */
    static int getInvCount(int[] arr, int c)
    {
        int inv_count = 0;
        for (int i = 0; i < c * c; i++)
            for (int j = i + 1; j < c * c; j++)
                // Value 0 is used for empty space
                if (arr[i] > 0 &&
                        arr[j] > 0 && arr[i] > arr[j]) {
                    inv_count++;
                }
        return inv_count;
    }

    /**
     * Function that determines if a NumPuzz game is solvable, returns true if it is, false
     * if it is not.
     * @param numbers ArrayList of numbers that will make up a NumPuzz game
     * @param selection a number that tracks user selection
     * @param cols the number of columns (and rows as each puzzle is a square)
     * @return returns true if puzzle is solvable, false if it is not
     */
    static boolean isSolvable(ArrayList<String> numbers, int selection, int cols) {
        // ArrayList to array conversion
        String[] arr = numbers.toArray(new String[0]);
        int[] num_array = Arrays.stream(arr)
                .mapToInt(Integer::parseInt)
                .toArray();
        // Count inversions
        int invCount = getInvCount(num_array, cols);
        switch (selection) {
            case 0:
                // return true if inversion count is even
                return (invCount % 2 == 0);
            case 1:
            case 2:
                int row = 0;
                int blankRow = 0;
                int parity = 0;
                for (int i = 0; i < num_array.length; i++)
                {
                    if (i % cols == 0) { // advance to next row
                        row++;
                    }
                    if (num_array[i] == 0) { // the blank tile
                        blankRow = row; // save the row on which encountered
                        continue;
                    }
                    for (int j = i + 1; j < num_array.length; j++)
                    {
                        if (num_array[i] > num_array[j] && num_array[j] != 0)
                        {
                            parity++;
                        }
                    }
                }

                if (cols % 2 == 0) { // even grid
                    if (blankRow % 2 == 0) { // blank on odd row; counting from bottom
                        return parity % 2 == 0;
                    } else { // blank on even row; counting from bottom
                        return parity % 2 != 0;
                    }
                } else { // odd grid
                    return parity % 2 == 0;
                }

        }
        return true;
    }

    public void saveGame() throws IOException {
        int choice = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to save current game?\n" +
                        "NOTE: This will override any previously saved game",
                "Save Game",  JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION)
        {
            wr = new FileWriter("saved_game.txt");
            for (int i = 0; i < numbers.size(); i++) {
                if (i != numbers.size() - 1)
                    wr.write(numbers.get(i) + ",");
                else
                    wr.write(numbers.get(i));
            }
            wr.close();
            JOptionPane.showMessageDialog(this,
                    "Your game has been saved",
                    "Game Saved", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void loadGame() throws IOException {
        int choice = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to load your previously saved game?\n" +
                        "NOTE: You will lose all progress in your current game",
                "Save Game",  JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION)
        {
            BufferedReader reader =new BufferedReader(new FileReader("saved_game.txt"));
            String line;
            String [] tempList = new String[size];
            while ((line = reader.readLine()) != null) {
                tempList = line.split(",");
            }
            numbers = new ArrayList<>(Arrays.asList(tempList));
            clip.stop();
            dispose();
            new GameController(b, game_grid, size, rows, cols, selection, gameMode, true, numbers, charSolution, tileColour, textColour);
            JOptionPane.showMessageDialog(this,
                    "Your game has been successfully laded",
                    "Game Loaded", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public Color getTileColour() {
        return tileColour;
    }

    public void setTileColour(Color colour) {
        this.tileColour = colour;
    }
    public Color getTextColour() {
        return textColour;
    }

    public void setTextColour(Color colour) {
        this.textColour = colour;
    }

    /**
     * Action listener that is invoked when an action occurs
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    /**
     * A class that is implemented to display the win screen pop up
     */
    private static class WinSplash extends JFrame{
        @Serial
        private static final long serialVersionUID = 1L;

        JLabel image = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().
                getResource("winner.jpg"))));

        /**
         * Creates an instance of splash with win game image and sound effects
         */
        public WinSplash() {
            getContentPane().setLayout(null);
            setUndecorated(true);

            setSize(800, 800);
            setLocationRelativeTo(null);
            add(image);
            image.setSize(800, 800);
            setVisible(true);


            SwingUtilities.invokeLater(() -> {
                try {
                    PlaySound("applause.wav");
                    Thread.sleep(2000); // change this to button
                    setVisible(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }
    }

    /**
     * A class that is implemented to display the start screen pop up
     */
    private static class StartSplash extends JFrame{
        @Serial
        private static final long serialVersionUID = 1L;

        JLabel image = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().
                getResource("start.jpg"))));

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

}