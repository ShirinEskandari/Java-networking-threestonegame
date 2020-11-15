package threestoneclient;

import javafx.application.Application;
import javafx.event.EventType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;

/**
 * This is the main class for the client application.
 * The class will start the application and is going to use a FXML file and a controller.
 * 
 * @author Michael Mishin 1612993
 */
public class ThreeStoneClient extends Application {
    
    static protected String server;
    static protected int servPort;
    
    //this controller will be used for accessing the closing socket method inside the controller
    private FXMLDocumentController mainController;
    
    /**
     * This method will create the stage for the game and than shows it to the screen.
     * 
     * @param stage
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("FXMLDocument.fxml"));
        
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Parent root = loader.load();
        this.mainController = loader.getController();
        
        Scene scene = new Scene(root);
        stage.setTitle("JavaFX Echo Client GUI");
        stage.setScene(scene);
        
        // setting up an event to close the socket if it is open when client closes the window.
        stage.setOnCloseRequest((WindowEvent we) -> {
            mainController.closeSocket();
        });
        
        //setting up an event for when the player press the esc button to quit the game.
        stage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> 
        {
            if (KeyCode.ESCAPE == event.getCode()) 
            {
                mainController.closeSocket();
                stage.close();
            }
        });
        
        stage.show();
    }

    /**
     * Main method.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
