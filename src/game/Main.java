package game;
import clientserver.GameClient;
import clientserver.GameServer;
import view.MainFrame;

/**
 * The main class running the numpuzz game
 * @author Byron Jones and Sean Wray
 */
public class Main {
    /**
     * driver method running the game
     * @param args is the runtime argument array
     */
    public static void main(String[] args) {
//        MainFrame frame = new MainFrame();
//        frame.pack();

        GameClient gameClient = new GameClient();
        gameClient.pack();

        GameServer gameServer = new GameServer();
        gameServer.pack();

    }
}
