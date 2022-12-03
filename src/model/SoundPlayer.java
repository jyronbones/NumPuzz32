package model;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Class that handles playing of music and game sound effects during NumPuzz
 */
public class SoundPlayer {
    private static Clip clip;
    private static AtomicLong clipPausePos;
    /**
     * boolean that determines if music is paused or not
     */
    public static boolean paused = false;

    /**
     * Function that plays in-game music
     * @param path path to sound files
     * @return returns audio clip
     */
    public static Clip playGameSound(String path) {
        if (clip == null) {
            try {
                InputStream is = new BufferedInputStream(new FileInputStream(path));
                AudioInputStream audio = AudioSystem.getAudioInputStream(is);
                clip = AudioSystem.getClip();
                clip.open(audio);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return clip;
    }

    /**
     * Function that allows in game music to be paused
     */
    public static void pauseSong() {
        if (clip != null && !paused) {
            clipPausePos = new AtomicLong();
            clipPausePos.set(clip.getMicrosecondPosition());
            clip.stop();
            paused = true;
        }
    }

    /**
     * function that starts in game music
     */
    public static void start() {
        if (clip != null && paused) {
            clip.setMicrosecondPosition(clipPausePos.get());
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            paused = false;
        }
    }

    /**
     * getter for audio clip
     * @return returns audio clip file
     */
    public static Clip getClip() {
        return clip;
    }

    /**
     * Disposes/Terminates audio clip
     */
    public static void dispose() {
        clip = null;
    }
}
