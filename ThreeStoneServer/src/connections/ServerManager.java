package connections;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * This class will handle server socket creation and accepting new clients
 * Will also display information about the server and its clients
 * 
 * @author Michael Mishin 1612993
 */
public class ServerManager {
    
    private final InetAddress serverIP;
    private static final int SERVER_PORT = 50000;
    
    private final ServerSocket serverSocket;
    
    /**
     * Default Constructor
     * 
     * @throws UnknownHostException
     * @throws IOException
     */
    public ServerManager() throws UnknownHostException, IOException
    {
        this.serverIP = InetAddress.getLocalHost();
        this.serverSocket = new ServerSocket(SERVER_PORT);
        
    }
    
    /**
     * Displays important information about the server
     */
    public void displayServerInformation()
    {
        System.out.println("Server Ip address is: " + serverIP);
        System.out.println("Listening on port: " + SERVER_PORT);
    }
    
    /**
     * Getter for the server socket
     * 
     * @return The server socket
     */
    public ServerSocket getServerSocket()
    {
        return this.serverSocket;
    }
    
    /**
     * This method will create a new client socket and will display the client information
     * 
     * @return The client socket
     * @throws IOException 
     */
    public Socket makeClientSocket() throws IOException 
    {
        Socket clntSock = this.serverSocket.accept();// Get client connection
        SocketAddress clientAddress = clntSock.getRemoteSocketAddress();
        System.out.println("Handling client at " + clientAddress);
        return clntSock;
    }
}
