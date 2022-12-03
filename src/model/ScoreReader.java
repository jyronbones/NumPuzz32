package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * A class for reading and writing user high scores to be displayed in game
 */
public class ScoreReader {
    /**
     * Takes in a path to the files containing high score data
     * @param path file path to score file
     * @return returns score values or 0 if file not found
     */
    private static int read(String path){
        try {
            Scanner sc = new Scanner(new File(path));
            int score = sc.nextInt();
            return score;
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * reads and retuns easy high score
     * @return int for easy high score
     */
    public static int readEasy(){
        return read("resources/easy_score.txt");
    }
    /**
     * reads and retuns medium high score
     * @return int for medium high score
     */
    public static int readMedium(){
        return read("resources/med_score.txt");
    }
    /**
     * reads and retuns hard high score
     * @return int for hard high score
     */
    public static int readHigh(){
        return read("resources/hard_score.txt");
    }

    /**
     * Writes high scores to external file so they are kept for future players
     * @param path path to file where high score will be written
     * @param move score value
     */
    public static void write(String path, int move) {
        try {
            PrintWriter writer = new PrintWriter(path);
            writer.write(""+move);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
