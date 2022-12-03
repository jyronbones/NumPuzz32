package clientserver;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A class that allows a server to be created and have clients
 * connect to it for the purposes of enabling a client-server
 * NumPuzz connection
 */
public class GameServer extends JFrame implements ActionListener {
    /**
     * Port number that clients will need to connect to
     */
    int portNumber;
    /**
     * A map data structure for storing user scores
     */
    Map<String, String> scores;
    /**
     * Server socket.
     */
    static ServerSocket servsock;
    /**
     * serial version user ID allows serialization to occur
     */
    // basic
    static final long serialVersionUID = 1L;

    // GUI initialization
    /**
     * This icon is the NumPuzz jpg logo
     */
    Icon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("server.png")));

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
    JLabel portLabel = new JLabel("Port:");
    /**
     * A button that is used to start a server
     */
    JButton startBtn = new JButton("Start");
    /**
     * Button used to display player results
     */
    JButton resultBtn = new JButton("Result");
    /**
     * Button used to end a server session
     */
    JButton endBtn = new JButton("End");
    /**
     * Check box used to finalize server settings
     */
    JCheckBox finalizeChk = new JCheckBox("Finalize");
    /**
     * Creates a border of specified colour and thickness
     */
    Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
    /**
     * A JTextArea used for displaying player moves
     */
    JTextArea textArea = new JTextArea(14, 43);
    /**
     * Text field used for port number
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
     * Constructor, creates instances of GameServer
     */
    public GameServer() {
        scores = new HashMap<>();

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

        infoPanel.add(portLabel);
        infoPanel.add(portText);
        portText.setBorder(border);

        infoPanel.add(startBtn);
        startBtn.addActionListener(e -> {
            try {
                portNumber = Integer.parseInt(portText.getText());
                startBtn.setEnabled(false);
                endBtn.setEnabled(true);
                textArea.append("Starting...\n");
                ThreadServer.main(new String[]{portText.getText()});
                ThreadServer threadServer = new ThreadServer(Integer.parseInt(portText.getText()), this);
            } catch (NumberFormatException ne) {
                textArea.append("Error: Incorrect port number\n");
            }
        });
        infoPanel.add(resultBtn);
        resultBtn.setEnabled(false);

        infoPanel.add(finalizeChk);

        infoPanel.add(endBtn);
        endBtn.setEnabled(false);
        endBtn.addActionListener(e -> {
            startBtn.setEnabled(true);
            endBtn.setEnabled(false);
            textArea.append("Ending...\n");
            try {
                servsock.close();
            } catch (IOException ex) {
                textArea.append(ex.getMessage() + "\n");
            }
        });

        resultBtn.addActionListener(e -> {
            StringBuilder str = new StringBuilder("Game results:");
            for (String score : scores.values()) {
                str.append("\n").append(score);
            }
            JOptionPane.showMessageDialog(null, str.toString(), "Message", JOptionPane.INFORMATION_MESSAGE);
        });

        infoPanel.add(textArea);
        textArea.setBorder(border);


        setTitle("Game Server");
        setResizable(false);
        setLocationRelativeTo(null); // center game on screen
        setVisible(true); //show frame
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
    class ThreadServer extends Thread {

        /**
         * Socket variable
         */
        Socket sock;

        /**
         * Variables for number clients
         */
        static int nclient = 0, nclients = 0;


        /**
         * Default port
         */
        static int PORT = 3000;

        /**
         * Number of port
         */
        static int portNumber = 0;
        GameServer gameServer;

        /**
         * Default constructor
         */
        public ThreadServer(int portNumber, GameServer gameServer) {
            this.gameServer = gameServer;
            try {
                servsock = new ServerSocket(portNumber);
                start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Main method
         *
         * @param args Param arguments
         */
        public static void main(String[] args) {

        }

        /**
         * Run method
         */
        public void run() {
            while (!servsock.isClosed()) {
                try {
                    sock = servsock.accept();
                    nclient += 1;
                    nclients += 1;
                    if (!gameServer.resultBtn.isEnabled()) {
                        gameServer.resultBtn.setEnabled(true);
                    }
                    gameServer.textArea.append("Connecting " + sock.getInetAddress() + " at port " + sock.getPort() + ".\n");
                    Worked w = new Worked(sock, nclient);
                    w.start();
                } catch (IOException ioe) {
                    gameServer.textArea.append(ioe.getMessage() + "\n");
                }
                if (gameServer.finalizeChk.isSelected()) {
                    gameServer.textArea.append("Finalized! No more client accepting..\n");
                    break;
                }
            }
        }

        /**
         * Inner class for Theads
         *
         * @author sousap
         */
        class Worked extends Thread {

            /**
             * Socket variable
             */
            Socket sock;

            /**
             * Integers for client and positions
             */
            int clientid, poscerq;

            /**
             * String for data
             */
            String strcliid, dadosCliente;

            /**
             * Default constructor
             *
             * @param s       Socket
             * @param nclient Number of client.
             */
            public Worked(Socket s, int nclient) {
                sock = s;
                clientid = nclient;
            }

            /**
             * Run method
             */
            public void run() {
                String data;
                PrintStream out;
                BufferedReader in;
                try {
                    out = new PrintStream(sock.getOutputStream());
                    in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    out.println(clientid);
                    dadosCliente = "";
                    while (!dadosCliente.equals(GameBasics.PROTOCOL_END)) {
                        data = in.readLine();
                        if (data == null) {
                            break;
                        }
                        gameServer.textArea.append("Cli[" + strcliid + "]: " + data + "\n");
                        if (!Character.isDigit(data.charAt(0))) {
                            if (data.split(",").length > 0) {
                                gameServer.textArea.append("Score received..\n");
                                scores.put(data.substring(0, data.indexOf(",")), data);
                            }
                        }else {
                            poscerq = data.indexOf("#");
                            strcliid = data.substring(0, poscerq);
                            dadosCliente = data.substring(poscerq + 1);
                            if (dadosCliente.startsWith(GameBasics.PROTOCOL_RECVGAME)) {
                                gameServer.textArea.append("Configuration requested..\n");
                                gameServer.textArea.append("Sending configuration..\n");
                                out.println(clientid + GameBasics.PROTOCOL_SEPARATOR
                                        + "Number;1,2,3,4,5,6,7,8,9,0");
                                out.flush();
                            }
                        }
                    }
                    gameServer.textArea.append("Disconnecting " + sock.getInetAddress() + "!\n");
                    nclients -= 1;
                    gameServer.textArea.append("Current client number: " + nclients + "\n");
                    if (nclients == 0) {
                        gameServer.textArea.append("Ending server...\n");
                        Thread.sleep(1000);
                        sock.close();
                        System.exit(0);
                    }
                } catch (IOException | InterruptedException e) {
                    gameServer.textArea.append(e.getMessage() + "\n");
                }
            }
        }
    }

}

