package clientserver;

import view.MainFrame;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Objects;
import java.net.Socket;

/**
 * A class that allows a client to be created and connected to our server,
 * allowing them to receive NumPuzz data from the server
 */
public class GameClient extends JFrame implements ActionListener {
    // client properties
    /**
     * tracks user score
     */
    int score;
    /**
     * tracks user game time
     */
    int time;
    /**
     * username for num puzz client
     */
    String username;
    /**
     * name of server client is connected to
     */
    String servername;
    /**
     * port number that client will use to connect to server
     */
    int portNumber;
    /**
     * an instance of socket that will allow client to connect to server
     */
    Socket client;
    /**
     * reader which allows client to receive and parse strings sent from server
     */
    BufferedReader incoming;
    /**
     * print stream that allows client to send information to server
     */
    PrintStream outgoing;
    /**
     * client identification number
     */
    int clientId;
    /**
     * serial version ID allows for serialization when communicating with server
     */
    // basic
    static final long serialVersionUID = 1L;

    // GUI initialization
    /**
     * This icon is the NumPuzz jpg logo
     */
    Icon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("client.png")));

    /**
     * Primary JPanel which will act as a container for NumPuzz game
     */
    JPanel container = new JPanel();
    /**
     * This panel will contain the grid of squares that make up NumPuzz game
     */
    JPanel infoPanel = new JPanel();
    /**
     * This panel will contain the settings buttons and functions for the NumPuzz game
     */
    JPanel imagePanel = new JPanel();

    /**
     * Affixes the NumPuzz icon to a JLabel to be displayed in settings panel
     */
    JLabel title_image = new JLabel(icon);
    /**
     * Affixes the NumPuzz icon to a JLabel to be displayed in settings panel
     */
    JLabel userLabel = new JLabel("User:");
    /**
     * Affixes the NumPuzz icon to a JLabel to be displayed in settings panel
     */
    JLabel serverLabel = new JLabel("Server:");
    /**
     * Affixes the NumPuzz icon to a JLabel to be displayed in settings panel
     */
    JLabel portLabel = new JLabel("Port:");
    /**
     * creates a button used to connect client to server
     */
    JButton connectBtn = new JButton("Connect");
    /**
     * creates a button to end client-server connection
     */
    JButton endBtn = new JButton("End");
    /**
     * creates a button to create a new NumPuzz game
     */
    JButton newGameBtn = new JButton("New Game");
    /**
     * Creates a button to send a NumPuzz game to server
     */
    JButton sendGameBtn = new JButton("Send Game");
    /**
     * Creates a button to receive a NumPuzz game from server
     */
    JButton receiveGameBtn = new JButton("Receive Game");
    /**
     * Creates a button to send data to NumPuzz server
     */
    JButton sendDataBtn = new JButton("Send Data");
    /**
     * Creates a button to play a game of NumPuzz using client-server
     */
    JButton playBtn = new JButton("Play");




    /*
    Creates a border of specified color and width
     */
    Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
    /**
     * A JTextArea used for displaying player moves
     */
    JTextArea textArea = new JTextArea(14, 43);
    /**
     * A text field used for inputting username
     */
    JTextField userText = new JTextField(5);
    /**
     * A text field used for inputting server info
     */
    JTextField serverText = new JTextField(GameBasics.DEFAULT_ADDR, 5);
    /**
     * A text field used for entering port number
     */
    JTextField portText = new JTextField(5);

    /**
     * A GridBayLayout used to display various settings and scores used in settings panel
     */
    GridBagLayout settings_grid = new GridBagLayout();
    /**
     * Creates a GridBagConstrains object used for manipulating various buttons and cells in GridBagLayout
     */
    GridBagConstraints gbc = new GridBagConstraints();
    /**
     * A GridLayout that is used for the NumPuzz game tiles
     */
    GridLayout game_grid;

    /**
     * Constructor, creates instances of GameClient
     */
    public GameClient() {
        // container panel
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        // image panel
        container.add(imagePanel);
        imagePanel.setPreferredSize(new Dimension(500, 100));
        add(imagePanel, BorderLayout.NORTH);
        imagePanel.add(title_image);
        title_image.setBorder(border);

        // info panel
        container.add(infoPanel);
        infoPanel.setPreferredSize(new Dimension(500, 300));
        add(infoPanel, BorderLayout.SOUTH);
        infoPanel.add(userLabel);
        infoPanel.add(userText);
        userText.setBorder(border);

        infoPanel.add(serverLabel);
        infoPanel.add(serverText);
        serverText.setBorder(border);

        infoPanel.add(portLabel);
        infoPanel.add(portText);
        portText.setBorder(border);

        infoPanel.add(connectBtn);
        connectBtn.addActionListener(e -> {
            try {
                username = userText.getText();
                servername = serverText.getText();
                portNumber = Integer.parseInt(portText.getText());
                if (username.equals("") || servername.equalsIgnoreCase(""))
                    throw new IllegalArgumentException();
                connectBtn.setEnabled(false);
                endBtn.setEnabled(true);
                textArea.append("Connecting...\n");
                try {
                    client = new Socket(servername, portNumber);
                    textArea.append("Connected...\n");
                    incoming = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    outgoing = new PrintStream(client.getOutputStream());
                    clientId = Integer.parseInt(incoming.readLine());
                    textArea.append("Your id is " + clientId + "\n");
                    sendGameBtn.setEnabled(true);
                    receiveGameBtn.setEnabled(true);
                    newGameBtn.setEnabled(true);
                } catch (Exception ex) {
                    System.out.println("Error: " + ex);
                }
            } catch (NumberFormatException ne) {
                textArea.append("Error: Incorrect port number\n");
            } catch (IllegalArgumentException iae) {
                textArea.append("Error: Invalid user or server\n");
            }
        });

        newGameBtn.addActionListener(e -> {
            MainFrame frame = new MainFrame(this);
            frame.pack();
        });

        infoPanel.add(endBtn);
        endBtn.setEnabled(false);
        endBtn.addActionListener(e -> {
            textArea.append("Disconnecting...\n");
            outgoing.println(clientId + GameBasics.PROTOCOL_SEPARATOR + GameBasics.PROTOCOL_END);
            try {
                client.close();
            } catch (IOException ex) {
                textArea.append(ex.getMessage());
            }
            connectBtn.setEnabled(true);
            endBtn.setEnabled(false);
            newGameBtn.setEnabled(false);
            sendGameBtn.setEnabled(false);
            receiveGameBtn.setEnabled(false);
        });

        newGameBtn.addActionListener(e -> {

        });
        sendGameBtn.addActionListener(e -> {
            String str = clientId +
                    GameBasics.PROTOCOL_SEPARATOR +
                    GameBasics.PROTOCOL_SENDGAME +
                    ";Number;" +
                    "1,2,3,4,5,6,7,8,9,0";
            textArea.append("Sending game configuration: " + str + "\n");
            outgoing.println(str);
            outgoing.flush();
        });

        receiveGameBtn.addActionListener(e -> {
            String str = clientId +
                    GameBasics.PROTOCOL_SEPARATOR +
                    GameBasics.PROTOCOL_RECVGAME;
            textArea.append("Sending request for game configuration: " + str + "\n");
            outgoing.println(str);
            outgoing.flush();
            new Thread(() -> {
                try {
                    String config = incoming.readLine();
                    textArea.append(config + "\n");
                } catch (IOException ex) {
                    textArea.append(ex.getMessage() + "\n");
                }
            }).start();
        });

        infoPanel.add(newGameBtn);
        newGameBtn.setEnabled(false);
        infoPanel.add(sendGameBtn);
        sendGameBtn.setEnabled(false);
        infoPanel.add(receiveGameBtn);
        receiveGameBtn.setEnabled(false);
        infoPanel.add(sendDataBtn);
        sendDataBtn.setEnabled(false);
        infoPanel.add(playBtn);
        playBtn.setEnabled(false);

        infoPanel.add(textArea);
        textArea.setBorder(border);


        setTitle("Game Client");
        setResizable(false);
        setLocationRelativeTo(null); // center game on screen
        setVisible(true); //show frame
    }

    /**
     * Sends score to be printed when result is clicked in server
     */
    public void sendScore(){
        outgoing.println(username+", Points: "+score+", Time: "+time);
        outgoing.flush();
    }

    /**
     * sets the score
     * @param score is the current score of user
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * sets the current time of game in play
     * @param time is current time of timer
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * handles actions performed by the user
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    /**
     *  Class that contains variables and methods associated with threading and the
     *  creation of threads which enables multiple client connections
     */
    static class ThreadClient extends Thread {

        /**
         * Default port
         */
        static int PORT = 3000;

        /**
         * Number of port
         */
        int portNumber;

        /**
         * Default hostname
         */
        static String HOSTNAME = "localhost";

        /**
         * Variable for hostname
         */
        String hostName;
        /**
         * An instance of GameClient
         */
        GameClient gameClient;

        /**
         * Default constructor
         */
        public ThreadClient(String host, int port, GameClient gameClient) {
            this.portNumber = port;
            this.hostName = host;
            this.gameClient = gameClient;
        }

        /**
         * Main method
         *
         * @param args Param arguments
         */
        public static void main(String[] args) {

        }

        /**
         *  Overrides built in run method to allow for client-server
         *  multithreaded functionality
         */
        @Override
        public void run() {
            System.out.println("Connecting with server on " + hostName + " at port " + portNumber);
            System.out.println("Starting Server Thread on port " + portNumber);
            try {
                Socket sock = new Socket(hostName, portNumber);
                BufferedReader dis = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                PrintStream dat = new PrintStream(sock.getOutputStream());
                String strcliid = dis.readLine();
                gameClient.textArea.append("\nYour id is " + strcliid);
                gameClient.newGameBtn.setEnabled(true);
                gameClient.receiveGameBtn.setEnabled(true);
                gameClient.sendGameBtn.setEnabled(true);
                StringBuilder consoleData;
                String serverData;
                /// DataInputStream inConsole = new DataInputStream(System.in);
                BufferedReader inConsole = new BufferedReader(new InputStreamReader(System.in));
                consoleData = new StringBuilder(inConsole.readLine());
                while (!consoleData.toString().equals("end")) {
                    consoleData.insert(0, strcliid + "#");
                    dat.println(consoleData);
                    dat.flush();
                    serverData = dis.readLine();
                    System.out.println("Server: " + serverData);
                    System.out.print("Client[" + strcliid + "]: ");
                    consoleData = new StringBuilder(inConsole.readLine());
                }
                consoleData.insert(0, strcliid + "#");
                dat.println(consoleData);
                dat.flush();
                sock.close();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

}


