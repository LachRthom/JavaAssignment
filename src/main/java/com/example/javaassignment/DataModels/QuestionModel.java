package com.example.javaassignment.DataModels;

public class QuestionModel {

    // Declare our Array of Strings which is the container to hold a question and all its associated fields
    private String[] surveyQuestion;

    // A get method for accessing a survey question, this is done to promote data encapsulation by having the
    // surveyQuestion object private and access to it via a public get method
    public String[] getSurveyQuestion() {
        return surveyQuestion;
    }

    // A set method for setting the values of a surveyQuestion object using the passed array of strings
    public void setSurveyQuestion(String[] surveyQuestion) {
        this.surveyQuestion = surveyQuestion;
    }

    @Override
    public String toString() {
        // Assuming surveyQuestion is an array with elements [questionNumber, questionTopic, questionAverage]
        return "Question Number: " + surveyQuestion[0] + "\n" +
                "Question Topic: " + surveyQuestion[1] + "\n" +
                "Average: " + 69420 + "\n\n";
    }


}


