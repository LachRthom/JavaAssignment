package com.example.javaassignment;

import com.example.javaassignment.Networking.ClientThread;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.net.*;
import java.io.*;

public class ClientController {

    // GUI shite
    @FXML
    private Label lblMessage;
    @FXML
    private TextField questionTopicTextField, questionTextField, questionAnswer1, questionAnswer2,
            questionAnswer3, questionAnswer4, questionAnswer5, userAnswerTextField;

    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ClientThread client2 = null;
    private static String serverName = "localhost";
    private static int serverPort = 4444;

    public void connect() {
        println("Establishing connection. Please wait ...");
        try {
            socket = new Socket(serverName, serverPort);
            println("Connected: " + socket);
            open();
        } catch (UnknownHostException uhe) {
            println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            println("Unexpected exception: " + ioe.getMessage());
        }
    }



    public void send() {
        try {
            // TODO change to only send the information required by the scenario

            // Get the answer from userAnswerTextField
            String answer = userAnswerTextField.getText();

            // Append an identifier string "client" to the answer
            String modifiedAnswer = answer + ":client";

            System.out.println("client is sending this to host: " + modifiedAnswer);

            // Send the modified answer back to the host
            streamOut.writeUTF(modifiedAnswer);
            streamOut.flush();

        } catch (IOException ioe) {
            println("Sending error: " + ioe.getMessage());
            close();
        }
    }


    public void handle(String msg) {
        Platform.runLater(() -> {
            // Split the received message by ": " delimiter
            String[] parts = msg.split(": ");

            // Check if the message originated from the host
            if (parts.length > 0 && "host".equals(parts[parts.length - 1])) {
                // Print to console for testing
                System.out.println("Client Received message: " + msg); // Print received message to console
                // Update text fields with the received data
                questionTopicTextField.setText(parts[2]);
                questionTextField.setText(parts[3]);
                questionAnswer1.setText(parts[4]);
                questionAnswer2.setText(parts[5]);
                questionAnswer3.setText(parts[6]);
                questionAnswer4.setText(parts[7]);
                questionAnswer5.setText(parts[8]);

                // TODO - clear answer field when recieved new question


            } else {
                // Ignore the message
                System.out.println("Message not from Host. Ignoring...");
            }
        });
    }


    public void open() {
        try {
            streamOut = new DataOutputStream(socket.getOutputStream());
            client2 = new ClientThread(this, socket);
        } catch (IOException ioe) {
            println("Error opening output stream: " + ioe);
        }
    }

    public void close() {
        try {
            if (streamOut != null) {
                streamOut.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            println("Error closing ...");
        }
        client2.close();
    }

    void println(String msg) {
        lblMessage.setText(msg);
    }

    /**
     * This method is to handle the function of the exit button
     * It prompts the user to confirm or cancel the exit action
     * then either closes the application correctly or returns to the main screen
     */
    public void exitApplication() {
        SurveyManagerMain.exitApplication();
    }

}
