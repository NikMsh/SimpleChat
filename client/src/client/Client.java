package client;

import network.ConnectionTCP;
import network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Client extends JFrame implements ActionListener, TCPConnectionListener {
    private static final String IP_ADDRESS = ""; //TODO set your ip here
    private static final int PORT = 1234;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private ConnectionTCP connection;

    private JTextArea log = new JTextArea();
    private final JTextField nickname = new JTextField("user");
    private final JTextField inputMess = new JTextField();

    private Client() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        log.setEnabled(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);

        inputMess.addActionListener(this);
        add(inputMess, BorderLayout.SOUTH);

        add(nickname, BorderLayout.NORTH);

        setVisible(true);

        try {
            connection = new ConnectionTCP(this, IP_ADDRESS, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message = inputMess.getText();
        if (message.equals(" ")) {
            return ;
        }
        inputMess.setText(null);
        connection.sendMessage(nickname.getText() + ": " + message);
    }

    @Override
    public void onConnectionReady(ConnectionTCP connectionTCP) {
        printMessage("Connection ready");
    }

    @Override
    public void onReceiveString(ConnectionTCP connectionTCP, String message) {
        printMessage(message);
    }

    @Override
    public void onDisconnect(ConnectionTCP connectionTCP) {
        printMessage("Connection closed");
    }

    @Override
    public void onException(ConnectionTCP connectionTCP, Exception ex) {
        System.out.println("Connection Exception: " + ex);
    }

    private synchronized void printMessage(String mess) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(mess + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
