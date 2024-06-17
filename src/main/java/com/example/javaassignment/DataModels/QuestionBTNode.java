package com.example.javaassignment.DataModels;

public class QuestionBTNode {
    public QuestionBTNode left;
    public QuestionBTNode right;
    private QuestionModel questionData;

    public QuestionBTNode(QuestionModel questionData) {
        this.left = null;
        this.right = null;
        this.questionData = questionData;
    }

    // Get method for questionData
    public QuestionModel getQuestionData() {
        return questionData;
    }

    // Get method for left child node
    public QuestionBTNode getLeft() {
        return left;
    }

    // Get method for right child node
    public QuestionBTNode getRight() {
        return right;
    }
}
