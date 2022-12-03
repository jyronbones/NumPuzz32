package model;

import java.nio.file.Paths;

/**
 * Class that handles getting file paths to all necessary resources (scores, images, audio files) to be used in NumPuzz game
 */
public class Path {
    /**
     * gets path to applause audio file
     */
    public static final String APPLAUSE = Paths.get(System.getProperty("user.dir"), "src", "resources", "applause.wav").toString();
    /**
     * gets path to best score file
     */
    public static final String B_SCORE = Paths.get(System.getProperty("user.dir"), "src", "resources", "bscore.jpg").toString();
    /**
     * gets path to "easy" image for easy best score label
     */
    public static final String EASY = Paths.get(System.getProperty("user.dir"), "src", "resources", "easy.png").toString();
    /**
     * gets path to exit image for exiting game
     */
    public static final String EXIT = Paths.get(System.getProperty("user.dir"), "src", "resources", "exit.png").toString();
    /**
     * gets path to "hard" image for hard best score label
     */
    public static final String HARD = Paths.get(System.getProperty("user.dir"), "src", "resources", "hard.png").toString();
    /**
     * gets path to high score label image file
     */
    public static final String HIGH_SCORE = Paths.get(System.getProperty("user.dir"), "/src", "resources", "highscores.gif").toString();
    /**
     * gets path to high scores image file
     */
    public static final String H_SCORE = Paths.get(System.getProperty("user.dir"), "src", "resources", "hscore.jpg").toString();
    /**
     * gets path to in game theme music audio file
     */
    public static final String JEOPARDY = Paths.get(System.getProperty("user.dir"), "src", "resources", "jeopardy.wav").toString();
    /**
     * gets path to load game menu icon image file
     */
    public static final String LOAD = Paths.get(System.getProperty("user.dir"), "src", "resources", "load.png").toString();
    /**
     * gets path to medium high score image file
     */
    public static final String MEDIUM = Paths.get(System.getProperty("user.dir"), "src", "resources", "medium.png").toString();
    /**
     * gets path to new game image file
     */
    public static final String NEW = Paths.get(System.getProperty("user.dir"), "src", "resources", "new.png").toString();
    /**
     * gets path to main game logo img file
     */
    public static final String NUM_PUZZLE = Paths.get(System.getProperty("user.dir"), "src", "resources", "numpuzz.jpg").toString();
    /**
     * gets path to player score label file
     */
    public static final String P_SCORE = Paths.get(System.getProperty("user.dir"), "src", "resources", "pscore.jpg").toString();
    /**
     * gets path to puzzle piece menu icon image file
     */
    public static final String PUZZLE = Paths.get(System.getProperty("user.dir"), "src", "resources", "puzzle.png").toString();
    /**
     * gets path to restart game image file
     */
    public static final String RESTART = Paths.get(System.getProperty("user.dir"), "src", "resources", "restart.png").toString();
    /**
     * gets path to save icon image file
     */
    public static final String SAVE = Paths.get(System.getProperty("user.dir"), "src", "resources", "save.png").toString();
    /**
     * gets path to song
     */
    public static final String SONG = Paths.get(System.getProperty("user.dir"), "src", "resources", "song.wav").toString();
    /**
     * gets path to start image
     */
     public static final String START = Paths.get(System.getProperty("user.dir"), "src", "resources", "start.jpg").toString();
    /**
     * gets path totimer image
     */
    public static final String TIMER = Paths.get(System.getProperty("user.dir"), "src", "resources", "timer.gif").toString();
    /**
     * gets path to undo image
     */
    public static final String UNDO = Paths.get(System.getProperty("user.dir"), "src", "resources", "undo.png").toString();
    /**
     * gets path to winner image
     */
    public static final String WINNER = Paths.get(System.getProperty("user.dir"), "src", "resources", "winner.jpg").toString();
    /**
     * gets path to easy score text file
     */
    public static final String EASY_SCORE_ = Paths.get(System.getProperty("user.dir"),  "easy_score.txt").toString();
    /**
     * gets path to medium score text file
     */
    public static final String MEDIUM_SCORE_ = Paths.get(System.getProperty("user.dir"),  "med_score.txt").toString();
    /**
     * gets path to hard score text file
     */
    public static final String HIGH_SCORE_ = Paths.get(System.getProperty("user.dir"),  "hard_score.txt").toString();
    /**
     * gets path to save game text file
     */
    public static final String SAVE_PATH = Paths.get(System.getProperty("user.dir"),  "saved_game.txt").toString();
}
