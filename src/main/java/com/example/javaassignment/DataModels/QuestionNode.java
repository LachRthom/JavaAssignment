package com.example.javaassignment.DataModels;

public class QuestionNode {

    public QuestionNode prev;  // Make prev public
    public QuestionNode next;  // Make next public
    private QuestionModel questionData;

    // Get method
    public QuestionModel getQuestionData() {
        return  questionData;
    }

    // Constructor for the head node
    public QuestionNode() {
        // Initialize the head node to point to itself both prev and next
        prev = this;
        next = this;
        // Initialize the linkedQuestionModel to "HEAD"
        questionData = new QuestionModel();
        questionData.setSurveyQuestion(new String[]{"head"});
    }

    /**
     * Constructs a QuestionNode with the provided QuestionModel data.
     *
     * @param questionData the QuestionModel object containing the data to be stored in the node
     */
    public QuestionNode(QuestionModel questionData) {
        this.prev = null;
        this.next = null;
        this.questionData = questionData;
    }


    /**
     * Appends a new node after the current node.
     *
     * @param newQuestionNode the new QuestionNode to be appended
     */
    public void appendNode(QuestionNode newQuestionNode) {
        // Set the previous and next references of the new node
        newQuestionNode.prev = this;
        newQuestionNode.next = this.next;

        // Update the previous reference of the node after the new node
        if (this.next != null) {
            this.next.prev = newQuestionNode;
        }

        // Update the next reference of the current node
        this.next = newQuestionNode;
    }
}
