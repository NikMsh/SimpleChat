package network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class ConnectionTCP {

    private final Socket socket;
    private final Thread receiveThread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final TCPConnectionListener eventListener;

    public ConnectionTCP(TCPConnectionListener eventListener, String ipAddr, int port) throws IOException{
        this(eventListener, new Socket(ipAddr, port));
    }

    public ConnectionTCP(TCPConnectionListener tcpConnectionListener, Socket socket) throws IOException {
        this.eventListener = tcpConnectionListener;
        this.socket = socket;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));

        receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(ConnectionTCP.this);
                    while (!receiveThread.isInterrupted()) {
                        eventListener.onReceiveString(ConnectionTCP.this, in.readLine());
                    }
                } catch (IOException e) {
                    eventListener.onException(ConnectionTCP.this, e);
                } finally {
                    eventListener.onDisconnect(ConnectionTCP.this);
                }
            }
        });
        receiveThread.start();
    }

    public synchronized void sendMessage(String mess) {
        try {
            out.write(mess + "\r\n");
            out.flush();
        } catch (IOException e) {
            eventListener.onException(ConnectionTCP.this, e);
        }
    }

    public synchronized void disconnect() {
        receiveThread .interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(ConnectionTCP.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}
