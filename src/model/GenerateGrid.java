package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * A class that generates a game grid for NumPuzz game using either user selected settings
 * or game defaults (using constants)
 */
public class GenerateGrid {
    /**
     * An arraylist that holds custom user selected settings
     */
    public static ArrayList<String> custom;

    /**
     * A function that creates and retrieves an array list of shuffled numbers for NumPuzz
     * @return returns ArrayList of shuffled game numbers
     */
    public static ArrayList<String> getNumbers() {
        int row = Constants.ITEM_ROWS;
        int col = Constants.ITEM_COL;
        int total = row * col;
        /**
         * ArrayList of numbers used in NumPuzz game
         */
        ArrayList<String> num = new ArrayList<>();
        for (int i = 1; i <= total; i++) {
            num.add("" + i);
        }
        num.remove(num.size()-1);
        Collections.shuffle(num);
        num.add("");
        return num;
    }

    /**
     * A function that creates and retrieves an array list of strings that are actually
     * single chars that are used in the letters game mode
     * @return returns ArrayList of shuffled game letters
     */
    public static ArrayList<String> getDigit() {
        int row = Constants.ITEM_ROWS;
        int col = Constants.ITEM_COL;
        int total = row * col;
        /**
         * ArrayList of strings used in NumPuzz game, letters mode
         */
        ArrayList<String> num = new ArrayList<>();
        for (int i = 65; i < total+65; i++) {
            System.out.println((char) i);
            num.add("" + ((char) i));
        }
        num.remove(num.size()-1);
        Collections.shuffle(num);
        num.add("");
        return num;
    }

    /**
     * A method that returns an arraylist of either numbers or letters depending on the game mode
     * @return returns arraylist of numbers or letters
     */
    public static ArrayList<String> getArrayList() {
        if(Constants.CUSTOM == 1)
            return custom;
        if(Constants.GAME_MODE == 0)
            return getNumbers();
        return getDigit();
    }

}
