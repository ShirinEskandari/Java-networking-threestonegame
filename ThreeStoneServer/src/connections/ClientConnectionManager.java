package connections;

import business.Board;
import business.EnumColor;
import business.GameEngine;
import business.Move;
import business.Score;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class connects the server with the client This class will use the game
 * logic class in here and will have the main loop.
 *
 * @author Michael Mishin 1612993, Shirin, Lin.
 */
public class ClientConnectionManager implements Runnable {

    private static final int BUFSIZE = 32;   // Size of receive buffer
    
    private Integer remainedStones;
    private Integer totalserverScore;
    private Integer totalClientScore;
    private int privateMoveX = -1;
    private int privateMoveY = -1;
    private Integer clientMoveIsOk;
    
    private final Socket clientSocket;

    /**
     * Constructor that receive a client socket
     * 
     * @param clientSocket The client socket
     */
    public ClientConnectionManager(Socket clientSocket) 
    {
        this.clientSocket = clientSocket;
    }
    
    /**
     * This method will create the game in a different thread
     */
    @Override
    public void run() 
    {
        try 
        {
            connect();
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(ClientConnectionManager.class.getName()).log(Level.SEVERE, "Client has failed to connect to the server", ex);
        }
    }
    
    /**
     * This method indicate a connection start/end and will start the game
     * 
     * @throws IOException
     * @throws Exception 
     */
    public void connect() throws IOException, Exception
    {
        System.out.println("client connecting!");
        readClientData(); // start the game
        System.out.println("client disconnected!");
    }

    /**
     * This method will create the byte array which will contain the important information to send back to the client
     * 
     * The byte array structure 
     * result[0] = the server x position played
     * result[1] = the server y position played
     * result[2] = was the client last play legal?
     * result[3] = remaining stones left
     * result[4] = the Client's total score
     * result[5] = the server's total score
     * 
     * @param game The current game object
     * @param score The current score object
     * @param serverX The server X position
     * @param serverY The server Y position
     * @return The byte array 
     */
    private byte[] makeServerDataForClient(GameEngine game, Score score, Integer serverX, Integer serverY) {

        remainedStones = game.getRemainingClientStone();
        totalserverScore = score.getBlackScore();
        totalClientScore = score.getWhiteScore();
        byte[] result = new byte[6];
        result[0] = serverX.byteValue();
        result[1] = serverY.byteValue();
        result[2] = clientMoveIsOk.byteValue();
        result[3] = remainedStones.byteValue();
        result[4] = totalClientScore.byteValue();
        result[5] = totalserverScore.byteValue();

        return result;
    }

    /**
     * Creating the byte array to return in case the user has clicked on an illegal position in the game
     * The byte array will have default 0 in all places indicating wrong move
     * 
     * @return The byte array
     */
    private byte[] makeDataForServerForWrongMove() {
        byte[] result = new byte[6];
        result[0] = 0;
        result[1] = 0;
        result[2] = 0;
        result[3] = 0;
        result[4] = 0;
        result[5] = 0;
        
        return result;
    }

    /**
     * This method handles the game rounds using the GameEngine object.
     * Will receive and send byte arrays according the the input and output of the game.
     * 
     * @throws IOException
     * @throws Exception 
     */
    private void readClientData() throws IOException, Exception {
        // try with resource to make sure the socket is closed
        try{
            int recvMsgSize;
            InputStream in = clientSocket.getInputStream();
            privateMoveX = -1;
            privateMoveY = -1;
            Board board = new Board(11, 11);
            Score score = new Score(board);
            Move serverMove;
            GameEngine game1 = new GameEngine(board, score);
            byte[] coordinates = new byte[2];
            // Receive until client closes connection, indicated by -1 return
            while ((recvMsgSize = in.read(coordinates)) != -1) 
            {
                int x = (int) coordinates[0];
                int y = (int) coordinates[1];
                if (game1.hasClientChosenEmptySpot(x, y, privateMoveX, privateMoveY)) {
                    
                    Move clientMove = new Move(x, y, EnumColor.WHITE);
                    
                    clientMoveIsOk = 1;
                    board.setBoardForOneMove(clientMove);
                    
                    GameEngine game = new GameEngine(clientMove, board, score);
                    
                    //server move x,y
                    serverMove = game.chooseTheBestOption();
                    score.getNewScroe(serverMove);
                    board.setBoardForOneMove(serverMove);
                    Integer serverX = serverMove.getX();
                    Integer serverY = serverMove.getY();
                    byte[] result = makeServerDataForClient(game, score, serverX, serverY);
                    
                    OutputStream out = clientSocket.getOutputStream();
                    out.write(result);
                    
                    privateMoveX = serverMove.getX();
                    privateMoveY = serverMove.getY();
                    
                } 
                else // if wrong move has been played
                {
                    byte[] result = makeDataForServerForWrongMove();
                    OutputStream out = clientSocket.getOutputStream();
                    out.write(result);
                }
            }
            
        }
        finally // Close the socket.  We are done with this client!
        {
            this.clientSocket.close();
        }
    }
}
