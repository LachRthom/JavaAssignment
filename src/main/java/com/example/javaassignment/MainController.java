package com.example.javaassignment;

import com.example.javaassignment.DataAccess.FileManager;
import com.example.javaassignment.DataModels.*;
import com.example.javaassignment.Networking.MainThread;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.net.*;
import java.io.*;
import java.util.HashMap;

public class MainController {

    @FXML private TableView<QuestionModel> questionTableView;
    @FXML private TableColumn<QuestionModel, String> questionNumber, questionTopic, questionText;
    @FXML private TextField questionTopicTextField, questionNumberTextField, questionTextField;
    @FXML private TextField questionAnswer1, questionAnswer2, questionAnswer3, questionAnswer4, questionAnswer5;
    @FXML private Button prevButton, nextButton;
    @FXML private TextField linkedSearchTextField, binarySearchTextField;
    @FXML private TextArea linkedListTextArea, binaryTreeTextArea;
    @FXML private Label lblMessage;
    private DLList linkedList;
    private BTree binaryTree;
    private HashMap<Integer, String> hashMap;

    // Setting up the network settings
    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private MainThread client = null;
    private String serverName = "localhost";
    private int serverPort = 4444;

    /**
     * This method handles populating the TableView upon starting the application and any other "on-start" requirements
     */
    public void initialize() {

        // Initialize the linked list
        linkedList = new DLList();

        // Initialize the Binary Tree
        binaryTree = new BTree();

        // Initialize the Hash Map
        hashMap = new HashMap<>();

        // File path for the question data
        String filePath = "src/main/resources/com/example/javaassignment/testdata/SurveyByNetwork_SampleData.txt";

        // Use FileManager to read in the data
        ArrayList<QuestionModel> questionList = FileManager.readDataFromFile(filePath);

        // Extract the first line of data for column headers
        if (!questionList.isEmpty()) {
            String[] columnHeaders = questionList.removeFirst().getSurveyQuestion();
            // Set column headers
            questionNumber.setText(columnHeaders[0]);
            questionTopic.setText(columnHeaders[1]);
            questionText.setText(columnHeaders[2]);
        }

        // Set cell value factories for each column
        questionNumber.setCellValueFactory(cellData -> {
            String[] surveyQuestion = cellData.getValue().getSurveyQuestion();
            return new javafx.beans.property.SimpleStringProperty(surveyQuestion.length > 0 ? surveyQuestion[0] : "");
        });

        questionTopic.setCellValueFactory(cellData -> {
            String[] surveyQuestion = cellData.getValue().getSurveyQuestion();
            return new javafx.beans.property.SimpleStringProperty(surveyQuestion.length > 1 ? surveyQuestion[1] : "");
        });

        questionText.setCellValueFactory(cellData -> {
            String[] surveyQuestion = cellData.getValue().getSurveyQuestion();
            return new javafx.beans.property.SimpleStringProperty(surveyQuestion.length > 2 ? surveyQuestion[2] : "");
        });

        // Using .addAll instead of converting the ArrayList to an Observable list, this is fine unless we need
        // dynamic updates to the list, if we add the ability to add/edit/delete questions for example
        questionTableView.getItems().addAll(questionList);

        // Add listener for TableView selection changes
        questionTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Call method to populate text fields with selected QuestionModel data
                populateTextFields(newSelection);
            }
        });
    }

    /**
     * Populates the text fields with data from the selected QuestionModel object.
     *
     * @param questionModel The selected QuestionModel object.
     */
    private void populateTextFields(QuestionModel questionModel) {
        // Extract selected QuestionModel object from TableView and populate TextFields
        String[] surveyQuestion = questionModel.getSurveyQuestion();
        questionNumberTextField.setText(surveyQuestion[0]);
        questionTopicTextField.setText(surveyQuestion[1]);
        questionTextField.setText(surveyQuestion[2]);
        questionAnswer1.setText(surveyQuestion[3]);
        questionAnswer2.setText(surveyQuestion[4]);
        questionAnswer3.setText(surveyQuestion[5]);
        questionAnswer4.setText(surveyQuestion[6]);
        questionAnswer5.setText(surveyQuestion[7]);
    }

    private void clearTextFields() {
        // Clear text fields
        questionNumberTextField.clear();
        questionTopicTextField.clear();
        questionTextField.clear();
        questionAnswer1.clear();
        questionAnswer2.clear();
        questionAnswer3.clear();
        questionAnswer4.clear();
        questionAnswer5.clear();
    }

    /**
     * Sorts the questions displayed in the table view by a specific topic.
     * This method retrieves the data from the table view, sorts it based on
     * the topic of the questions, and updates the table view with the sorted data.
     *
     */
    public void sortQuestionTopic() {
        // Get data from table view
        ArrayList<QuestionModel> questionList = new ArrayList<>(questionTableView.getItems());
        // Sort, refactored this out in case in the future the sorting algorithm
        // needs to be changed or current one reused
        insertSort(questionList);
        // Update the table view with sorted ArrayList
        questionTableView.getItems().setAll(questionList);
    }


    /**
     * Sorts the given ArrayList of QuestionModel objects using the insertion sort algorithm.
     * The sorting is based on the second element of the 'surveyQuestion' array within each QuestionModel,
     * which typically represents the topic of the question.
     *
     * @param al An ArrayList of QuestionModel objects to be sorted.
     *           This ArrayList should contain QuestionModel objects with 'surveyQuestion' arrays
     *           where the second element holds the topic information used for sorting.
     */
    public void insertSort(ArrayList<QuestionModel> al) {
        for (int i = 1; i < al.size(); i++) {
            QuestionModel key = al.get(i);
            int j = i - 1;

            // sort based on the 2nd element of the surveyQuestion array, which is 'topic'
            String[] keyQuestion = key.getSurveyQuestion();
            String keyProperty = keyQuestion[1];

            // Compare the keyProperty with previous elements' properties
            while (j >= 0 && al.get(j).getSurveyQuestion()[1].compareTo(keyProperty) > 0) {
                al.set(j + 1, al.get(j));
                j = j - 1;
            }
            al.set(j + 1, key);
        }
    }


    /**
     * Sorts the questions displayed in the table view by text using the merge sort algorithm.
     * This method retrieves the data from the table view, sorts it using the merge sort algorithm,
     * and updates the table view with the sorted data.
     *
     */
    public void sortQuestionText(){
        // Get data from table view
        ArrayList<QuestionModel> questionList = new ArrayList<>(questionTableView.getItems());
        // Sort, refactored this out in case in the future the sorting algorithm
        // needs to be changed or current one reused
        mergeSort(questionList, 0, questionList.size() - 1);
        // Update the table view with sorted ArrayList
        questionTableView.getItems().setAll(questionList);
    }


    /**
     * Recursively(break into smaller chunks) sorts the given ArrayList of QuestionModel objects
     * within the specified range using the merge sort algorithm.
     *
     * @param al    An ArrayList of QuestionModel objects to be sorted.
     * @param left  The index of the left boundary.
     * @param right The index of the right boundary.
     */
    private void mergeSort(ArrayList<QuestionModel> al, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            // Recursively sort the left and right halves
            mergeSort(al, left, mid);
            mergeSort(al, mid + 1, right);
            // Merge the sorted halves
            merge(al, left, mid, right);
        }
    }


    /**
     * Merges two sorted subarrays into one sorted array.
     *
     * @param al    An ArrayList of QuestionModel objects to be sorted.
     * @param left  The index of the left boundary of the current range.
     * @param mid   The index of the middle element of the current range.
     * @param right The index of the right boundary of the current range.
     */
    private void merge(ArrayList<QuestionModel> al, int left, int mid, int right) {
        // Create a temporary ArrayList to hold the merged elements
        ArrayList<QuestionModel> temp = new ArrayList<>(right - left + 1);
        int i = left;
        int j = mid + 1;

        // Merge the two sorted subarrays
        while (i <= mid && j <= right) {
            // Compare elements and add them to the temporary ArrayList in sorted order
            if (al.get(i).getSurveyQuestion()[2].compareToIgnoreCase(al.get(j).getSurveyQuestion()[2]) <= 0) {
                temp.add(al.get(i));
                i++;
            } else {
                temp.add(al.get(j));
                j++;
            }
        }

        // Add remaining elements from the left subarray to the temporary ArrayList
        while (i <= mid) {
            temp.add(al.get(i));
            i++;
        }

        // Add remaining elements from the right subarray to the temporary ArrayList
        while (j <= right) {
            temp.add(al.get(j));
            j++;
        }

        // Copy sorted elements from the temporary ArrayList back to the original ArrayList
        for (int k = left; k <= right; k++) {
            al.set(k, temp.get(k - left));
        }
    }


    /**
     * This Method is to handle the function of the Question Number Sort button
     * It gets the question data from the TableView then calls the bubbleSort method
     * Once the data is sorted the TableView is updated
     */
    public void sortQuestionNumber() {

        // Get data from table view
        ArrayList<QuestionModel> questionList = new ArrayList<>(questionTableView.getItems());
        // Sort, refactored this out in case in the future the sorting algorithm
        // needs to be changed or current one reused
        bubbleSort(questionList);
        // Update the table view with sorted ArrayList
        questionTableView.getItems().setAll(questionList);
    }


    /**
     * Sorts the given ArrayList of QuestionModel objects using the Bubble Sort algorithm.
     * Sorting is performed based on the first element of the 'surveyQuestion' array within each QuestionModel.
     *
     * @param al An ArrayList of QuestionModel objects to be sorted.
     *           This ArrayList should contain QuestionModel objects with 'surveyQuestion' arrays.
     *           The sorting is based on the first element of the 'surveyQuestion' array,
     *           which typically represents the primary sorting criteria.
     */
    public void bubbleSort(ArrayList<QuestionModel> al) {
        for(int j=0; j<al.size(); j++) {
            for(int i=j+1; i<al.size(); i++) {
                // Declare and Instantiate the two variables to compare first to make it more readable
                String[] question1 = al.get(j).getSurveyQuestion();
                String[] question2 = al.get(i).getSurveyQuestion();

                //Now run the comparison
                if (question2[0].compareToIgnoreCase(question1[0]) < 0){
                    QuestionModel temp = al.get(j);
                    al.set(j, al.get(i));
                    al.set(i, temp);
                }
            }
        }
    }

    public void sendQuestion() {
        // TODO change this so only the handle method posts to the DLL and BT and displays
        // TODO push to the hash map as well

        // Get the selected question from the table view
        QuestionModel selectedQuestion = questionTableView.getSelectionModel().getSelectedItem();

        if (selectedQuestion != null) {
            // Push the selected question data into a node in the DLList and BTree
            linkedList.appendNode(selectedQuestion);
            binaryTree.appendNode(selectedQuestion);

            // Put the Key and Value into the hash map
            // Requires an integer (Question Number) and string (Question topic)
            int key = Integer.parseInt(questionNumberTextField.getText());
            String value = questionTopicTextField.getText();
            hashMap.put(key, value);


            // Display the data in both text areas
            displayDataInTextArea(selectedQuestion, binaryTreeTextArea);
            displayDataInTextArea(selectedQuestion, linkedListTextArea);

            // Send the selected question across the network
            send(selectedQuestion);
        }
    }

    private void displayDataInTextArea(QuestionModel question, TextArea textArea) {
        // Append the data to the text area without clearing it
        double average = 0.0; // Placeholder value to test
        textArea.appendText("Question Number: " + question.getSurveyQuestion()[0] + "\n");
        textArea.appendText("Question Topic: " + question.getSurveyQuestion()[1] + "\n");
        textArea.appendText("Average: " + average + "\n\n");
    }
    // Method to sort and display data in inorder traversal
    public void sortAndDisplayInorder() {
        // Clear text areas before displaying sorted data
        binaryTreeTextArea.clear();

        // Sort and display binary tree in inorder traversal
        binaryTree.traverseInOrder().forEach(binaryTreeTextArea::appendText);
    }

    // Method to sort and display data in preorder traversal
    public void sortAndDisplayPreorder() {
        // Clear text areas before displaying sorted data
        binaryTreeTextArea.clear();

        // Sort and display binary tree in preorder traversal
        binaryTree.traversePreOrder().forEach(binaryTreeTextArea::appendText);

    }

    // Method to sort and display data in postorder traversal
    public void sortAndDisplayPostorder() {
        // Clear text areas before displaying sorted data
        binaryTreeTextArea.clear();


        // Sort and display binary tree in postorder traversal
        binaryTree.traversePostOrder().forEach(binaryTreeTextArea::appendText);
    }

    /**
     * This method is to handle the function of the exit button
     * It prompts the user to confirm or cancel the exit action
     * then either closes the application correctly or returns to the main screen
     */
    public void exitApplication() {

        // Creating an alert screen to popup upon button click,
        // this could've been done with a new scene and its own controller
        // but that is dumb for the small amount of code involved
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Exit Confirmation");
        alert.setContentText("Are you sure you want to exit?");

        // Creating the two customized buttons for the alert
        ButtonType buttonTypeYes = new ButtonType("Exit");
        ButtonType buttonTypeNo = new ButtonType("Cancel");

        // Replace the default buttons with the above custom ones
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Display the alert we have created and wait for input, then deal with the result
        alert.showAndWait().ifPresent(result -> {
            if (result == buttonTypeYes){
                System.out.println("Exiting");
                Platform.exit();
            }
        });
    }

    /**
     * Moves to the next node in the linked list and updates the display accordingly.
     * If a valid node is found, it populates the text fields with the question data.
     * If no valid node is found or if the current node is the head node, clears the text fields and disables the next button.
     */
    public void nextNode() {
        // Move current node forward
        linkedList.moveForward();

        // Display question object if current node is valid and not the head node
        if (linkedList.getCurrent() != null && linkedList.getCurrent() != linkedList.getHead()) {
            populateTextFields(linkedList.getCurrent().getQuestionData());
        }
        else{
            // Does what it says on the box
            clearTextFields();
            // Disable the next button to stop it cycling or doing weird shit
            nextButton.setDisable(true);
        }

        // Enable previous button if it was disabled, otherwise it stays inactive if previously set
        prevButton.setDisable(false);
    }

    /**
     * Moves to the previous node in the linked list and updates the display accordingly.
     * If a valid node is found, it populates the text fields with the question data.
     * If no valid node is found or if the current node is the head node, clears the text fields and disables the previous button.
     */
    public void previousNode() {
        // Move current node backward
        linkedList.moveBackward();

        // Display question object if one found, otherwise clear the textfields and disable the button
        if (linkedList.getCurrent() != null && linkedList.getCurrent() != linkedList.getHead()) {
            populateTextFields(linkedList.getCurrent().getQuestionData());
        }
        else{
            clearTextFields();
            prevButton.setDisable(true);
        }

        // Enable next button if it was disabled
        nextButton.setDisable(false);
    }

    /**
     * Searches for a specific question node in the linked list based on the provided question number.
     * If the node is found, populates the text fields with the question data.
     * If no node is found, clears the text fields.
     */
    public void linkedSearch() {
        // Retrieve the text from linkedSearchTextField
        String questionNumber = linkedSearchTextField.getText();

        // Find the QuestionNode with the specified question number
        QuestionNode foundNode = linkedList.findQuestionNode(questionNumber);

        // Check if the node is found
        if (foundNode != null) {
            // Display the question object in the text fields
            populateTextFields(foundNode.getQuestionData());
        } else {
            // Does what it says on the box champ
            clearTextFields();
        }
    }

    /**
     * Searches for a specific question node in the binary tree based on the provided question number.
     * If the node is found, populates the text fields with the question data.
     * If no node is found, clears the text fields.
     */
    public void binarySearch() {
        // Retrieve the text from the text area
        String questionNumber = binarySearchTextField.getText();

        // Find the QuestionNode with the specified question number
        QuestionBTNode foundNode = binaryTree.search(questionNumber);

        // Check if the node is found
        if (foundNode != null) {
            // Display the question object in the text fields
            populateTextFields(foundNode.getQuestionData());
        } else {
            // Clear the text fields if no node is found
            clearTextFields();
        }
    }

    public void connect()
    {
        println("Establishing connection. Please wait ...");
        try
        {
            socket = new Socket(serverName, serverPort);
            println("Connected: " + socket);
            open();
        }
        catch (UnknownHostException uhe)
        {
            println("Host unknown: " + uhe.getMessage());
        }
        catch (IOException ioe)
        {
            println("Unexpected exception: " + ioe.getMessage());
        }
    }

    // Method to send the selectedQuestion object over the network
    public void send(QuestionModel selectedQuestion) {
        try {
            // TODO change this and the handle method to properly handle receipt of answers and
            //  storage + display in linked list and binary tree

            // Convert the QuestionModel object to a string delimited by ':'
            String questionString = String.join(": ", selectedQuestion.getSurveyQuestion());

            // Append "host" to the end of the string
            questionString += ": host";

            // Send the string over the network
            streamOut.writeUTF(questionString);
            streamOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handle(String msg) {
        // Check if the message contains the client identifier "client"
        if (msg.contains("client")) {
            // Extract the answer from the message
            String[] parts = msg.replace("client", "").trim().split(":");
            if (parts.length >= 2) {
                String answer = parts[1].trim(); // Extract the number after the colon
                // Display the received answer in the console
                System.out.println("Host Received answer from client: " + answer);

                // Update UI components on the JavaFX Application Thread
                Platform.runLater(() -> {
                    lblMessage.setText("The client application submitted the following answer: " + answer);
                });
            } else {
                // Handle cases where the message format is incorrect
            }
        } else {
            // Handle other cases
        }
    }

    public void open()
    {
        try
        {
            streamOut = new DataOutputStream(socket.getOutputStream());
            client = new MainThread(this, socket);
        }
        catch (IOException ioe)
        {
            println("Error opening output stream: " + ioe);
        }
    }

    public void close()
    {
        try
        {
            if (streamOut != null)
            {
                streamOut.close();
            }
            if (socket != null)
            {
                socket.close();
            }
        }
        catch (IOException ioe)
        {
            println("Error closing ...");
        }
        client.close();
    }

    // METHOD TO PRINT NETWORKING STATUS AT BOTTOM OF WINDOW
    void println(String msg)
    {
        lblMessage.setText(msg);
    }

    /**
     * Method to save data from the HashMap to a file
     */
    public void saveToFile() {
        // Check if hashMap is initialized
        if (hashMap == null) {
            // Handle case where hashMap is not initialized
            return;
        }

        // Call FileManager.saveDataToFile() and pass the hashMap
        FileManager.saveDataToFile(hashMap);
    }
}


