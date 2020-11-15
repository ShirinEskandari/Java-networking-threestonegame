package threestoneserver;

import connections.ClientConnectionManager;
import connections.ServerManager;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the main class.
 * It will create a server object and will create clients object when a new Client connects.
 * 
 * @author Michael Mishin 1612993
 */
public class ThreeStoneServer {
    
    public static void main(String[] args) throws IOException {
        
        // creating the server manager object
        ServerManager serverManager = new ServerManager();
        serverManager.displayServerInformation(); // displays the ip and port
        
        // running forever, accepting and spawing threads to service eaach connection
        while(true)
        {
            try 
            {
                Socket clientSocket = serverManager.makeClientSocket(); // make a new socket for the current client
                ClientConnectionManager clientManager = new ClientConnectionManager(clientSocket); 
                
                // creating the separate thread
                Thread clientThread = new Thread(clientManager);
                clientThread.start(); 
            } 
            catch (IOException ex)
            {
                Logger.getLogger(ThreeStoneServer.class.getName()).log(Level.SEVERE, "IO has failed in the main method", ex);
            } 
            catch (Exception ex) 
            {
                Logger.getLogger(ThreeStoneServer.class.getName()).log(Level.SEVERE, "There was a problem within the main method", ex);
            }
        }
    }
    
}
