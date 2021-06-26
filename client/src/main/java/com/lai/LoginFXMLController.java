package com.lai;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The Login FXML Controller controls the interface for connecting the application to the server
 */
public class LoginFXMLController {
    private Stage primaryStage;

    @FXML
    private TextField ipAddrInput;

    @FXML
    private TextField portInput;

    @FXML
    private Text informationText;

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    public LoginFXMLController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {

    }

    /**
     * Checks if the server is live
     */
    @FXML
    private void checkConnection() {
        String ip = ipAddrInput.getText();
        String port = portInput.getText();
        boolean isValidIp = validateIP(ip);
        boolean isValidPort = validatePort(port);
        System.out.println(isValidIp + " " + isValidPort);
        if(isValidIp && isValidPort) {
            // Test connection to the server
            Client client = new Client(ip, port);
            Packet reply = new Packet();
            try {
                reply = client.testConnection();
                System.out.println(reply);
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

            if(reply.getIsOperationSuccess()) {
                // If the packet is successful
                FXMLLoader fxml = new FXMLLoader();
                fxml.setLocation(getClass().getResource("/fxml/client.fxml"));
                fxml.setController(new ClientFXMLController(client, primaryStage));
                VBox interfaceScene = new VBox();
                try {
                    interfaceScene = fxml.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Scene scene = new Scene(interfaceScene);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        } else {
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText("Incorrect ip and/or port number.");
            a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            a.show();
        }
    }

    /**
     * Regex to validate the ip address
     * @param ip
     * @return
     */
    private boolean validateIP(String ip) {
        Pattern pattern2 = Pattern.compile("[0][.][0][.][0][.][0]");
        Matcher match2 = pattern2.matcher(ip);
        if (match2.matches()) {
            return true;
        } else {
            Pattern pattern = Pattern.compile("[0-9]{3}[.][0-9]{3}[.][0-9]{1,}[.][0-9]{3}");
            Matcher match = pattern.matcher(ip);
            return match.matches();
        }
    }

    /**
     * Regex to validate the port num
     * @param port
     * @return
     */
    private boolean validatePort(String port) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher match = pattern.matcher(port);
        return match.matches();
    }
}
