package view;

import clientserver.GameClient;
import controller.MenuController;
import model.Constants;
import model.Path;
import model.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.Serial;

/**
 * This class contains the components of the frame
 */
public class MainFrame extends JFrame implements WindowListener {
    @Serial
    private static final long serialVersionUID = 1L;
    private final MenuController con;
    private final Container container;
    private final TextColor color;

    GameClient client;

    /**
     * This constructor initializes the components and settings and the frame
     */
    public MainFrame(GameClient client) {
        this.client = client;
        this.setTitle("NumPuzz");
        this.setPreferredSize(new Dimension(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT));
        this.setSize(new Dimension(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT));
        setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("resources/puzzle.png").getImage());
        this.setVisible(true);
        StartSplash splash = new StartSplash();
        this.setBackground(Color.RED);
        SoundPlayer.playGameSound("resources/jeopardy.wav");
        container = new Container(this, client);
        setContentPane(container);
        con = new MenuController(this, client);
        color = new TextColor(this);
        setJMenuBar(menu());

    }

    /**
     * This class contains the menu for the game
     *
     * @return returns the menu of the game
     */
    public JMenuBar menu() {
        JMenuBar menu = new JMenuBar();
        JMenu file, edit, help;
        // File menu
        JSeparator file_sep3 = new JSeparator();
        JSeparator file_sep2 = new JSeparator();
        file = new JMenu("File");
        menu.add(file);

        JMenuItem save, load, exit, newGame;
        newGame = new JMenuItem("New Game");
        file.add(newGame);
        ImageIcon i = new ImageIcon("resources/new.png");
        Image image = i.getImage();
        Image newimg = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        i = new ImageIcon(newimg);
        newGame.setIcon(i);
        file.add(file_sep2);

        save = new JMenuItem("Save");
        file.add(save);
        ImageIcon j = new ImageIcon("resources/save.png");
        Image image2 = j.getImage();
        Image newimg2 = image2.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        j = new ImageIcon(newimg2);
        save.setIcon(j);


        load = new JMenuItem("Load");
        file.add(load);
        file.add(file_sep3);
        ImageIcon k = new ImageIcon("resources/load.png");
        Image image3 = k.getImage();
        Image newimg3 = image3.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        k = new ImageIcon(newimg3);
        load.setIcon(k);

        newGame.addActionListener(con);

        save.addActionListener(con);

        load.addActionListener(con);

        exit = new JMenuItem("Exit");
        ImageIcon l = new ImageIcon("resources/exit.png");
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
        undo.addActionListener(con);
        edit.add(editSep1);
        // tile colour
        JMenu tileColor = new JMenu("Tile Colour");
        edit.add(tileColor);
        JMenuItem whiteTile = new JMenuItem("White");
        tileColor.add(whiteTile);
        whiteTile.addActionListener(con);
        JMenuItem redTile = new JMenuItem("Red");
        tileColor.add(redTile);
        redTile.addActionListener(con);
        JMenuItem blueTile = new JMenuItem("Blue");
        tileColor.add(blueTile);
        blueTile.addActionListener(con);
        JMenuItem yellowTile = new JMenuItem("Yellow");
        tileColor.add(yellowTile);
        yellowTile.addActionListener(con);

        JMenu textColor = new JMenu("Text Colour");
        edit.add(textColor);
        JMenuItem whiteText = new JMenuItem("White");
        textColor.add(whiteText);
        whiteText.addActionListener(color);
        JMenuItem redText = new JMenuItem("Red");
        textColor.add(redText);
        redText.addActionListener(color);
        JMenuItem blueText = new JMenuItem("Blue");
        textColor.add(blueText);
        blueText.addActionListener(color);
        JMenuItem yellowText = new JMenuItem("Yellow");
        textColor.add(yellowText);
        yellowText.addActionListener(color);

        // Help menu
        JSeparator help_sep = new JSeparator();
        help = new JMenu("Help");
        menu.add(help);
        JMenuItem instructions, credits;
        instructions = new JMenuItem("Instructions");
        help.add(instructions);
        help.add(help_sep);

        instructions.addActionListener(con);


        credits = new JMenuItem("Credits");
        help.add(credits);

        int copyrightSymbolCodePoint = 169;
        String s = Character.toString(copyrightSymbolCodePoint);
        credits.addActionListener(con);

        return menu;
    }

    /**
     * This method accesses the container
     *
     * @return returns the container
     */
    public Container getContainer() {
        return container;
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        client.sendScore();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
