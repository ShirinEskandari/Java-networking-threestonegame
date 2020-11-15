package threestoneclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * This is the controller class for the client side of the 3 stones game.
 * This class will manage updating the visuals on the client side.
 * The visuals will depend on the server side game logic.
 * 
 * @author Michael Mishin 1612993
 */
public class FXMLDocumentController {

    //FXML variables ================================================
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML // fx:id="playBtn"
    private Button playBtn; // Value injected by FXMLLoader
    @FXML // fx:id="stopBtn"
    private Button stopBtn; // Value injected by FXMLLoader
    
    @FXML // fx:id="outputTxt"
    private TextArea outputTxt; // Value injected by FXMLLoader
    @FXML // fx:id="ImageGrid"
    private GridPane imageGrid; // Value injected by FXMLLoader
    
    @FXML // fx:id="serverIP"
    private TextField serverIP; // Value injected by FXMLLoader
    @FXML // fx:id="serverPort"
    private TextField serverPort; // Value injected by FXMLLoader
    
    @FXML // fx:id="whiteScoreTxt"
    private Label whiteScoreTxt; // Value injected by FXMLLoader
    @FXML // fx:id="blackScoreTxt"
    private Label blackScoreTxt; // Value injected by FXMLLoader
    
    @FXML // fx:id="stonesLeftTxt"
    private Label stonesLeftTxt; // Value injected by FXMLLoader
    
    @FXML // fx:id="serverStoneImage"
    private ImageView serverStoneImage; // Value injected by FXMLLoader
    @FXML // fx:id="userStoneImage"
    private ImageView userStoneImage; // Value injected by FXMLLoader
    
    // Normal variables =============================================
    private final Image WHITE_STONE_IMAGE = new Image("file:images/whiteStone.png");
    private final Image BLACK_STONE_IMAGE = new Image("file:images/blackStone.png");
    
    private Socket connection;

    /**
     * Checks if there is a connection to the server and if so, the player can start playing.
     * 
     * @param event
     * @throws IOException If there was a problem setting up the server connection.
     */
    @FXML
    void onPlayClick(ActionEvent event)
    {        
        int servPort; //for parsing string to int.
        try 
        {
            servPort = Integer.parseInt(serverPort.getText());
            // Create socket that is connected to server on specified port
            connection = new Socket(serverIP.getText(), servPort);
            addOutput("Connected to server!");
            addOutput("Game started!");
            playBtn.setDisable(true);
            stopBtn.setDisable(false);
            serverIP.setDisable(true);
            serverPort.setDisable(true);
            imageGrid.setDisable(false);

        } 
        catch (NumberFormatException ex) 
        {
            addOutput("The port should be a number, please enter a valid port.");
        } 
        catch (IOException ex) 
        {
            addOutput("Connection failed. Please try again.");
        }
    }
    
    /**
     * This will close the connection between the client and the server and end the game.
     * @param event 
     */
    @FXML
    void onStopClick(ActionEvent event) 
    {
        if(!connection.isClosed())
        {
            try {
                connection.close();
                addOutput("Connection ended!");
            } catch (IOException ex) {
                System.out.println("something went wrong!" + ex);
            }
        }
        
        resetGameVisuals();
        stopBtn.setDisable(true);
        playBtn.setDisable(false);
        serverIP.setDisable(false);
        serverPort.setDisable(false);
        imageGrid.setDisable(true);
    }
    
    /**
     * This method will close the connection socket if it is still open.
     */
    public void closeSocket()
    {
        if(connection != null && connection.isConnected())
        {
            try 
            {
                connection.close();
            } catch (IOException ignore) {}
        }
    }
    
    /**
     * This method will clear the board visuals.
     * Reset grid , reset score, and reset stones left
     */
    private void resetGameVisuals()
    {
        for(Node image:imageGrid.getChildren())
        {
            if(image instanceof ImageView)
            {
                ((ImageView) image).setImage(null);
            }
        }
        blackScoreTxt.setText(0+"");
        whiteScoreTxt.setText(0+"");
        stonesLeftTxt.setText(15+"");
    }

    /**
     * This method is called when the FXML gets initialized.
     * 
     * The method sets up the client and server images.
     * The method sets up click listeners for each imageView that is located inside the grid
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        userStoneImage.setImage(WHITE_STONE_IMAGE); 
        serverStoneImage.setImage(BLACK_STONE_IMAGE);
        
        // Creating an event listener for every ImageView that is located within the grid.
        imageGrid.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            // loops through the grid image view to match the click with the currect node.
            for( Node node: imageGrid.getChildren()) 
            {
                // checks is the node is an instance of ImageView
                if( node instanceof ImageView) 
                {
                    // check is the ImageView selected is within the bounds of the list
                    if( node.getBoundsInParent().contains(event.getSceneX(),  event.getSceneY())) 
                    {
                        // checks if the position selected has been selected before (has an image on it)
                        if(((ImageView)node).getImage() == null)
                        {
                            int row = 0;
                            int column = 0;
                            if(GridPane.getRowIndex(node) != null)
                                row = GridPane.getRowIndex(node);

                            if(GridPane.getColumnIndex(node) != null)
                                column = GridPane.getColumnIndex(node);

                            sendPlayIfLegal(row,column,(ImageView) node);

                            break; //stop loop
                        }
                        else
                        {
                            addOutput("Position is already taken.");
                            break; //stop loop
                        }
                    }
                }
            }
        });
    }
    
    /**
     * This method checks if the clicked position should be updated with the server.
     * If so it will update it visually.
     * 
     * @param x Coordinate for array
     * @param y Coordinate for array
     * @param currentImageView The reference to update the ImageView image source if the click was valid.
     */
    private void sendPlayIfLegal(int x, int y, ImageView currentImageView)
    {       
        try 
        {
            InputStream in = connection.getInputStream();
            OutputStream out = connection.getOutputStream();
            byte[] coordinates = new byte[2];
            Integer currentX = x;
            Integer currentY = y;
            coordinates[0] = currentX.byteValue();
            coordinates[1] = currentY.byteValue();
            
            // sending the server the last position of the player's move
            out.write(coordinates);
            
            // getting the server response into a byte array.
            byte[] result = new byte[6];
            in.read(result); // read the result from the server
            
            //checks if the move was valid.
            if(isResultValid(result))
            {
                currentImageView.setImage(WHITE_STONE_IMAGE);
                addOutput("Cliked on: ("+ x +","+ y +")");
                addOutput("Server played on: ("+ result[0] +","+ result[1]+")");
                roundResult(result);
            }
            else
            {
                addOutput("Wrong move, please click on a valid position.");
            }
        } 
        catch (IOException ex) 
        {
            addOutput("lost connection with the server...");
            onStopClick(new ActionEvent()); // if connection is lost return game back to original state
        }
    }
    
    /**
     * This method will add a line to the output box of the UI
     * Adds a new line at the end
     * 
     * @param text the line to add
     */
    private void addOutput(String text)
    {
        outputTxt.setText(outputTxt.getText() + text + "\n");
        outputTxt.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
    }    

    /**
     * We are getting an array from the server the array.
     * result[2] => should have a 1 or a 0 that says if the last move of the client was valid
     * 1 => true
     * 0 => false
     * @param result The array from the server
     * @return If the last move of the client was a valid move.
     */
    private boolean isResultValid(byte[] result) 
    {
        return result[2] == 1;
    }
    
    /**
     * This method will setup the text of the UI to show how much stones are left to play 
     * @param stones the amount of stones left
     */
    private void setStonesLeft(int stones)
    {
        if(stones > 15 || stones < 0)
        {
            throw new IllegalArgumentException("the current amount of stones is not currect");
        }
        this.stonesLeftTxt.setText(stones+"");
    }
    
    /**
     * This method will setup the score text in the UI for the client
     * @param score The new score
     */
    private void setClientScore(int score)
    {
        if(score < 0)
        {
            throw new IllegalArgumentException("The score is negative");
        }
        this.whiteScoreTxt.setText(score+"");
    }
    
    /**
     * This method will setup the score text in the UI for the server
     * @param score The new score
     */
    private void setServerScore(int score)
    {
        if(score < 0)
        {
            throw new IllegalArgumentException("The score is negative");
        }
        this.blackScoreTxt.setText(score+"");
    }
    
    /**
     * This method will update the UI for the image of where the server has played his move.
     * 
     * @param x X position
     * @param y Y position
     */
    private void updateserverStoneUI(int x, int y)
    {
        for(Node childNode : imageGrid.getChildren())
        {
            if(childNode instanceof ImageView)
            {
                int row = 0;
                int column = 0;
                
                //in a gridPane first position is null!
                if(GridPane.getRowIndex(childNode) != null)
                    row = GridPane.getRowIndex(childNode);
                
                //in a gridPane first position is null!
                if(GridPane.getColumnIndex(childNode) != null)
                    column = GridPane.getColumnIndex(childNode);
                
                if(row == x && column == y)
                {
                    ((ImageView) childNode).setImage(BLACK_STONE_IMAGE);
                    // make this ImageView highlighted (using brightness)!
                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setBrightness(0.4);
                    ((ImageView)childNode).setEffect(colorAdjust);
                    
                }
                else
                {
                    ((ImageView)childNode).setEffect(null);
                }
            }
        }
    }
    
    /**
     * This method gets the array from the server.
     * result[0] -> x position of the server move
     * result[1] -> y position of the server move
     * result[3] -> stones left to play
     * result[4] -> client score
     * result[5] -> server score
     * 
     * @param result 
     */
    private void roundResult(byte[] result)
    {
        if(result[3] > 0)
        {
            updateserverStoneUI((int)result[0], (int)result[1]);
            setClientScore((int)result[4]);
            setServerScore((int)result[5]);
            setStonesLeft((int)result[3]);
        }
        else
        {
            gameResult(result);
        }

    }
    
    /**
     * This method will display the winner and the scores.
     * result[0] -> x position of the server move
     * result[1] -> y position of the server move
     * result[3] -> stones left to play
     * result[4] -> client score
     * result[5] -> server score
     * @param result The array from the server.
     */
    private void gameResult(byte[] result)
    {
        String winnerMsg = "";
        if(result[4]>result[5])
        {
            winnerMsg = "Client Won! with "+result[4]+" points against "
                    + result[5] +" points";
        }
        if(result[4]<result[5])
        {
            winnerMsg = "Server Won! with "+result[5]+" points against "
                    + result[4] +" points";
        }
        if(result[4] == result[5])
        {
            winnerMsg = "Its a draw!";
        }
        
        if(result[3] == 0)
        {
            Alert alert = new Alert(AlertType.NONE, winnerMsg, ButtonType.FINISH);
            alert.showAndWait();
        }
        
        onStopClick(new ActionEvent()); // reset game.
    }
}
