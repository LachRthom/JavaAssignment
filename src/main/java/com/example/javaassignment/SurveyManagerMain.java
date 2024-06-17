package com.example.javaassignment;

import com.example.javaassignment.Networking.QuestionServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.IOException;

// TODO - Implement SQLite for data and user logins

/**
 * Entry point for the Survey Manager application. This class extends (adds my custom shit ontop of)
 * the javafx.application.Application class, which is the main class for JavaFX applications.
 * By extending {@code Application}, this class inherits the necessary functionality to launch and manage JavaFX
 * application lifecycles, this includes methods such as start(), init(), and stop().
 */
public class SurveyManagerMain extends Application {


    /**
     * The QuestionServer is instanced as a class variable
     * because we use it later to check for a running instance when closing.
     */
    private static QuestionServer questionServer;


    /**
     * Starts the Survey Manager application (all three applications).
     * This method is the entry point for launching the JavaFX application.
     * We are overriding the default javaFX start() because we have custom actions we want
     * It is invoked automatically by the JavaFX runtime system and requires a Stage object
     * as a parameter because the Stage represents the primary stage of the application (MainController).
     * Think of it like a physical theatre, the main scene is displayed on the primary stage
     * The client scene is not the main star, so a secondary stage is created for it to display on, javaFX
     * only REQUIRES a primary stage (as the application can't run without it), secondary stages are optional,
     * therefore can be created inside the method for their scene creation
     *
     * @param primaryStage The primary stage of the JavaFX application, representing the main window.
     * @throws IOException If an error occurs while loading the FXML file for the host scene.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Start the QuestionServer instance
        startServer();

        // Load and display the host scene
        startHostScene(primaryStage);

        // Load and display the client scene
        startClientScene();
    }


    /**
     * Starts the QuestionServer instance that we created earlier.
     */
    private void startServer() {
        questionServer = new QuestionServer(4444);
    }

    /**
     * Loads and displays the host scene on the primary stage of the JavaFX application.
     * The primary stage represents the main window of the application.
     *
     * @param primaryStage The primary stage of the JavaFX application where the host scene will be displayed.
     *                     This stage serves as the main window for the application.
     * @throws IOException If an error occurs while loading the FXML file for the host scene.
     */
    private void startHostScene(Stage primaryStage) throws IOException {

        // Load the FXML (GUI) file for the host scene
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("Main-View.fxml"));
        Scene mainScene = new Scene(mainLoader.load());

        // Configure the primary scene
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Survey Manager");

        // Handle the close request for the primary stage
        primaryStage.setOnCloseRequest(event -> {
            event.consume(); // Prevent default close operation
            exitApplication(); // Run our custom close operation
        });

        primaryStage.show();
    }


    /**
     * Loads and displays the client scene in a separate window.
     * This method loads the FXML file for the client scene, creates a new stage to contain the scene,
     * configures the stage with the necessary properties, and displays it to the user.
     *
     * @throws IOException If an error occurs while loading the FXML file for the client scene.
     */
    private void startClientScene() throws IOException {
        // Load the FXML (GUI) file for the client scene
        FXMLLoader clientLoader = new FXMLLoader(getClass().getResource("Question-View.fxml"));
        Scene clientScene = new Scene(clientLoader.load());

        // Create a new stage for the client scene
        Stage clientStage = new Stage();
        clientStage.setScene(clientScene);
        clientStage.setResizable(false);
        clientStage.setTitle("Client Window");

        // Handle the close request for the client stage
        clientStage.setOnCloseRequest(event -> {
            event.consume(); // Prevent default close operation
            exitApplication(); // Run our custom close operation
        });

        clientStage.show();
    }

    /**
     * Displays an exit confirmation dialog and exits the application if confirmed.
     * This custom process ensures that the application is exited gracefully and any
     * necessary cleanup operations are performed, such as stopping the server and releasing
     * resources, before terminating the application.
     */
    public static void exitApplication() {
        // Create an exit confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Any unsaved data may be lost.");

        // Add "Exit" and "Cancel" buttons to the dialog
        ButtonType buttonTypeExit = new ButtonType("Exit");
        ButtonType buttonTypeCancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(buttonTypeExit, buttonTypeCancel);

        // TODO - Learn more about threads and garbage collection to make sure this method
        //  closes ALL threads gracefully and release all resources properly

        // Show the dialog and wait for user's choice
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeExit) {
                // If user chooses "Exit", stop the server and exit the application
                if (questionServer != null) {
                    questionServer.stop();
                }

                // Terminate the application
                System.exit(0);
            }
        });
    }

    /**
     * This method serves as the entry point for the application. It is a standard Java main method,
     * which is the starting point for the execution of any Java application.
     * The "main" method invokes the "launch" method from the JavaFX Application class. This method is responsible
     * for starting the JavaFX application lifecycle. It initializes the JavaFX toolkit, starts the JavaFX application
     * thread, and calls the "start" method of the Application subclass provided as an argument. In this case, the
     * SurveyManagerMain class extends the Application class, so its "start" method is invoked.
     * <p>
     * The "args" parameter is an array of Strings representing the command-line arguments passed to the program
     * when it is launched. In this case, the "args" parameter is not used, as indicated by the comment.
     * <p>
     * Inside the "main" method, the "launch" method is called with "args" as its argument. This starts the JavaFX
     * application and begins the execution of the "start" method in the SurveyManagerMain class. The "launch" method
     * does not return until the application exits, either normally or due to an error.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

