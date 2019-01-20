package network;


public interface TCPConnectionListener {

    void onConnectionReady(ConnectionTCP connectionTCP);
    void onReceiveString(ConnectionTCP connectionTCP, String message);
    void onDisconnect(ConnectionTCP connectionTCP);
    void onException(ConnectionTCP connectionTCP, Exception ex);
}
