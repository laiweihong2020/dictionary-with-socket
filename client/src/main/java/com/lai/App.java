package com.lai;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The entry point of the entry point of the client software.
 * Java doesn't allow an entry point to extend a class.
 */
public class App extends Application
{
    /**
     * initialise the user interface
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxml = new FXMLLoader();

        fxml.setLocation(getClass().getResource("/fxml/login.fxml"));
        fxml.setController(new LoginFXMLController(primaryStage));
        VBox root = fxml.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Multi-threaded dictionary");
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }
}
