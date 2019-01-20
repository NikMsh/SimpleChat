package server;

import network.ConnectionTCP;
import network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListener {

    private final ArrayList<ConnectionTCP> connections = new ArrayList();

    private ChatServer() {
        System.out.println("Server running...");
        try(ServerSocket serverSocket = new ServerSocket(1234)) {
            while(true) {
                try {
                    new ConnectionTCP(this, serverSocket.accept());
                } catch (IOException ex) {
                    System.out.println("TCPConnection exception: " + ex);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        new ChatServer();
    }

    @Override
    public void onConnectionReady(ConnectionTCP connectionTCP) {

    }

    @Override
    public void onReceiveString(ConnectionTCP connectionTCP, String message) {

    }

    @Override
    public void onDisconnect(ConnectionTCP connectionTCP) {

    }

    @Override
    public void onException(ConnectionTCP connectionTCP, Exception ex) {

    }
}
