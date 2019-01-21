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
    public synchronized void onConnectionReady(ConnectionTCP connectionTCP) {
        connections.add(connectionTCP);
        sendToAllConnections("Client connected: " + connectionTCP.toString());
    }

    @Override
    public synchronized void onReceiveString(ConnectionTCP connectionTCP, String message) {
        sendToAllConnections(message);
    }

    @Override
    public synchronized void onDisconnect(ConnectionTCP connectionTCP) {
        connections.remove(connectionTCP);
        sendToAllConnections("Client disconnected: " + connectionTCP.toString());
    }

    @Override
    public synchronized void onException(ConnectionTCP connectionTCP, Exception ex) {
        System.out.println("TCPConnection Exception: " + ex);
    }

    private void sendToAllConnections(String message) {
        System.out.println(message);
        connections.forEach(i -> i.sendMessage(message));
    }
}
