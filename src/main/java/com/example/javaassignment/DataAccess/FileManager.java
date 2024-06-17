
package com.example.javaassignment.DataAccess;

import com.example.javaassignment.DataModels.QuestionModel;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    /**
     * Reads data from a file and creates an ArrayList of QuestionModel objects
     *
     * @param filePath - The file path of the file that contains the question data
     * @return - An ArrayList of QuestionModel objects representing the data in the given file
     */

    public static ArrayList<QuestionModel> readDataFromFile(String filePath) {

        // Declaring and instantiating the ArrayList to hold multiple QuestionModel objects
        ArrayList<QuestionModel> questionList = new ArrayList<>();

        // Java wants a try/catch anytime you deal with external resources
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            // Declare and instantiate a temp variable to hold 8 lines of data whilst it is pushed
            // into the QuestionModel object
            String line;

            // Because line gets populated with data here to check the while conditional,
            // and br.readLine(0 goes to the next line, we need to grab line
            // before it updated for the first value of each QuestionModel object
            while ((line = br.readLine()) != null) {
                QuestionModel questionModel = new QuestionModel();

                // Read 8 lines into a surveyQuestion array
                String[] surveyQuestions = new String[8];
                for (int i = 0; i < 8; i++) {
                    // Store the first line separately (this is the line used to check the while condition)
                    if (i == 0) {
                        surveyQuestions[i] = line;
                    } else {
                        surveyQuestions[i] = br.readLine(); // Read the NEXT! line
                    }
                }

                // Using the set method of the data model to populate the new QuestionModel object
                questionModel.setSurveyQuestion(surveyQuestions);
                // Add this completed QuestionModel object to the questionList
                questionList.add(questionModel);
            }

        } catch (FileNotFoundException e) {
            showFileErrorPopup("File Not Found", "The specified file could not be found ");
            throw new RuntimeException(e);
        } catch (IOException e) {
            showFileErrorPopup("IO Exception", "An error occurred while reading the file");
            throw new RuntimeException(e);
        }

        return questionList;
    }

    /**
     * Saves the contents of a HashMap to a file
     *
     * @param data - The HashMap containing the data to be saved
     */
    public static void saveDataToFile(HashMap<Integer, String> data) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showSaveDialog(new Stage());

        if (selectedFile != null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile))) {
                for (Map.Entry<Integer, String> entry : data.entrySet()) {
                    bw.write(entry.getKey() + ":" + entry.getValue());
                    bw.newLine();
                }
            } catch (IOException e) {
                showFileErrorPopup("IO Exception", "An error occurred while writing to the file");
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Method created to handle exceptions encountered by the FIleManager class
     *
     * @param title - Name or Title of the exception
     * @param content - Content text of the exception
     */
    private static void showFileErrorPopup(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

