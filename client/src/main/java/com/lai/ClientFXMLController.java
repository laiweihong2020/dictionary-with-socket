package com.lai;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

/**
 * The Client FXML Controller controls all interactions that the end user may have with the application
 */
public class ClientFXMLController {
    private Client client;
    private Stage primaryStage;

    @FXML
    private TextField addWordInput;

    @FXML
    private TextField getWordInput;

    @FXML
    private TextField deleteWordInput;

    @FXML
    private TextField updateWordInput;

    @FXML
    private TextField addMeaningInput;

    @FXML
    private TextField updateMeaningInput;

    @FXML
    private TextArea addOutputArea;

    @FXML
    private TextArea getOutputArea;

    @FXML
    private TextArea deleteOutputArea;

    @FXML
    private TextArea updateOutputArea;

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    public ClientFXMLController() {
    }

    public ClientFXMLController(String ip, int port, Stage primaryStage) {
        this.client = new Client(ip, port);
        this.primaryStage = primaryStage;
    }

    public ClientFXMLController(String ip, String port, Stage primaryStage) {
        this.client = new Client(ip, port);
        this.primaryStage = primaryStage;
    }

    public ClientFXMLController(Client client, Stage primaryStage) {
        this.client = client;
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {

    }

    /**
     * Adds a word to the dictionary
     */
    @FXML
    private void addWord() {
        String word = addWordInput.getText().toLowerCase();
        String meaning = addMeaningInput.getText();
        System.out.println(meaning.length());
        if(meaning.length() > 0) {
            Packet reply = new Packet();
            try {
                reply = this.client.addWord(word, meaning);
            } catch (IOException e) {
                connectionRefused();
            }

            String[] meaningArr = meaningStringToArr(reply.getMeaning());

            addOutputArea.setText("Operation status: " + reply.getIsOperationSuccess() + "\n");
            addOutputArea.appendText(reply.getWord() + "\n");
            for (int i = 0; i < meaningArr.length; i++) {
                int wordNumber = i+1;
                addOutputArea.appendText("Meaning " + wordNumber + ": " + meaningArr[i] + "\n");
            }
        } else {
            addOutputArea.setText("To add a word into the dictionary, please make sure that there is an associated meaning.");
        }
    }

    /**
     * Change servers
     */
    @FXML
    private void changeServer() {
        FXMLLoader fxml = new FXMLLoader();

        fxml.setLocation(getClass().getResource("/fxml/login.fxml"));
        fxml.setController(new LoginFXMLController(primaryStage));
        VBox root = new VBox();
        try {
            root = fxml.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Test to see if the connection is up
     */
    @FXML
    private void testConnection() {
        Packet reply = new Packet();
        try {
            reply = client.testConnection();
            // System.out.println(reply.getIsOperationSuccess());
            if(reply.getIsOperationSuccess()) {
                Alert a = new Alert(AlertType.INFORMATION);
                a.setContentText("Server is live.");
                a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                a.show(); 
            } else {
                throw new SocketException();
            }
        } catch (SocketTimeoutException e) {
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText("Connection to server timed out. Check server ip or try again later.");
            a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            a.show();
        } catch (SocketException e) {
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText("Could not connect to the server. Try again later.");
            a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            a.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve the meaning of the word
     */
    @FXML
    private void getMeaning() {
        String word = getWordInput.getText().toLowerCase();
        Packet reply = new Packet();
        try {
            reply = this.client.getMeaning(word);
        } catch (IOException e) {
            connectionRefused();
        }
        
        if(reply.getMeaning().length() == 0) {
            // The word does not exist
            getOutputArea.setText(word + " has not been added to the dictionary yet.");
        } else {
            String[] meaningArr = meaningStringToArr(reply.getMeaning());
            getOutputArea.setText("Operation status: " + reply.getIsOperationSuccess() + "\n");
            getOutputArea.appendText(reply.getWord() + "\n");
            for (int i = 0; i < meaningArr.length; i++) {
                int wordNumber = i+1;
                getOutputArea.appendText("Meaning " + wordNumber + ": " + meaningArr[i] + "\n");
            }
        }
    }

    /**
     * Deletes the word from the dictionary
     */
    @FXML
    private void deleteWord() {
        String word = deleteWordInput.getText().toLowerCase();
        Packet reply = new Packet();
        try {
            reply = this.client.removeWord(word);
        } catch (IOException e) {
            connectionRefused();
        }
        
        if(reply.getIsOperationSuccess()) {
            deleteOutputArea.setText("Operation status: " + reply.getIsOperationSuccess());
        } else {
            deleteOutputArea.setText("Operation status: " + reply.getIsOperationSuccess() + "\n");
            deleteOutputArea.appendText(reply.getDescription());
        }

    }

    /**
     * Updates the word in the dictionary
     */
    @FXML
    private void updateWord() {
        String word = updateWordInput.getText().toLowerCase();
        String meaning = updateMeaningInput.getText();

        Packet reply = new Packet();
        try {
            reply = this.client.updateWord(word, meaning);
        } catch (IOException e) {
            connectionRefused();
        }

        if(reply.getIsOperationSuccess()) {
            updateOutputArea.setText("Operation status: " + reply.getIsOperationSuccess() + "\n");

            String[] meaningArr = meaningStringToArr(reply.getMeaning());
            updateOutputArea.setText("Operation status: " + reply.getIsOperationSuccess() + "\n");
            updateOutputArea.appendText(reply.getWord() + "\n");
            for (int i = 0; i < meaningArr.length; i++) {
                int wordNumber = i+1;
                updateOutputArea.appendText("Meaning " + wordNumber + ": " + meaningArr[i] + "\n");
            }
        } else {
            updateOutputArea.setText("Operation status: " + reply.getIsOperationSuccess() + "\n");
            updateOutputArea.appendText(reply.getDescription());
        }
    }

    /**
     * Alert box when connection is refused
     * This can happen when the server is offline
     */
    private void connectionRefused() {
        Alert a = new Alert(AlertType.ERROR);
        a.setContentText("Error encountered when connecting to the server. Try again later.");
        a.show();
    }

    /**
     * Splits strings to an array
     * @param meaning
     * @return
     */
    private String[] meaningStringToArr(String meaning) {
        String[] arr = meaning.split("\\.");
        return arr;
    }
}