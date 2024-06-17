package com.example.javaassignment.DataModels;

import java.util.ArrayList;
import java.util.List;

public class DLList {
    private QuestionNode head;
    private QuestionNode current;

    /**
     * Constructs an empty doubly linked list with a dummy head node.
     */
    public DLList() {
        head = new QuestionNode();
        current = head;
    }

    /**
     * Appends a new node to the end of the list with the given question data.
     *
     * @param questionData the question data to be stored in the new node
     */
    public void appendNode(QuestionModel questionData) {
        head.appendNode(new QuestionNode(questionData));
    }

    /**
     * Finds a QuestionNode containing the specified search term.
     *
     * @param searchTerm the string to search for within the questions
     * @return the QuestionNode containing the search term, or null if not found
     */
    public QuestionNode findQuestionNode(String searchTerm) {
        // Traverse through the list starting from the node after head
        for (QuestionNode current = head.next; current != head; current = current.next) {
            // Extracting the question string from the QuestionModel
            String[] question = current.getQuestionData().getSurveyQuestion();
            // Assuming the question is stored at index 0
            if (question[0].equalsIgnoreCase(searchTerm)) {
                return current;
            }
        }
        // Search term not found, return null
        return null;
    }

    /**
     * Generates a string representation of the contents of the doubly linked list.
     *
     * @return a string representing the contents of the linked list
     */
    public String toString() {
        String str = "";
        // Check if the list is empty
        if (head.next == head) {
            return "List is Empty";
        }
        // Start building the string representation
        str = "Linked List Content = ";
        // Traverse through the list starting from the node after head
        for (QuestionNode current = head.next; current != head && current != null; current = current.next) {
            // Extracting the question string from the QuestionModel
            String[] question = current.getQuestionData().getSurveyQuestion();
            // Question text is stored at index 2, topic at 1 and question number at index 0
            str = str + question[2] + " ";
        }
        return str;
    }

    /**
     * Moves the current node forward in the list.
     */
    public void moveForward() {
        if (current != null && current.next != null) {
            current = current.next;
        }
    }

    /**
     * Moves the current node backward in the list.
     */
    public void moveBackward() {
        if (current != null && current.prev != null) {
            current = current.prev;
        }
    }

    /**
     * Gets the current node in the list.
     *
     * @return the current node
     */
    public QuestionNode getCurrent() {
        return current;
    }

    /**
     * Sets the current node in the list.
     *
     * @param current the current node to be set
     */
    public void setCurrent(QuestionNode current) {
        this.current = current;
    }

    /**
     * Gets the head node of the list.
     *
     * @return the head node
     */
    public QuestionNode getHead() {
        return head;
    }

    // In-order traversal for doubly linked list
    public List<String> traverseInOrder() {
        List<String> traversalResult = new ArrayList<>();
        inorderTraversal(head.next, traversalResult);
        return traversalResult;
    }

    // Pre-order traversal for doubly linked list (same as in-order for DLL)
    public List<String> traversePreOrder() {
        return traverseInOrder();
    }

    // Post-order traversal for doubly linked list (reverse order)
    public List<String> traversePostOrder() {
        List<String> traversalResult = new ArrayList<>();
        postorderTraversal(head.prev, traversalResult);
        return traversalResult;
    }

    // In-order traversal method
    private void inorderTraversal(QuestionNode node, List<String> traversalResult) {
        while (node != null && !node.getQuestionData().getSurveyQuestion()[0].equals("head")) {
            traversalResult.add(node.getQuestionData().toString());
            node = node.next;
        }
    }

    // Post-order traversal method
    private void postorderTraversal(QuestionNode node, List<String> traversalResult) {
        while (node != null && node.prev != null && !node.prev.getQuestionData().getSurveyQuestion()[0].equals("head")) {
            node = node.prev;
        }
        while (node != null && !node.getQuestionData().getSurveyQuestion()[0].equals("head")) {
            traversalResult.add(node.getQuestionData().toString());
            node = node.prev;
        }
    }

}
