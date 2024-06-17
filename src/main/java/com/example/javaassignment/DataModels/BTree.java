package com.example.javaassignment.DataModels;

import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class BTree {
    private QuestionBTNode root;

    // Constructor for an empty Binary Tree
    public void BinaryTree() {
        root = null;
    }

    // Method to insert a value into the binary tree
    public void appendNode(QuestionModel questionData) {
        root = insertRecursive(root, questionData);
    }

    // Helper method for recursive insertion
    private QuestionBTNode insertRecursive(QuestionBTNode root, QuestionModel questionData) {
        if (root == null) {
            return new QuestionBTNode(questionData);
        }

        // Comparison logic sorting on question number?
        if (questionData.getSurveyQuestion()[0].compareTo(root.getQuestionData().getSurveyQuestion()[0]) < 0) {
            root.left = insertRecursive(root.left, questionData);
        } else if (questionData.getSurveyQuestion()[0].compareTo(root.getQuestionData().getSurveyQuestion()[0]) > 0) {
            root.right = insertRecursive(root.right, questionData);
        }

        return root;
    }

    // Inorder traversal for binary tree
    public List<String> traverseInOrder() {
        List<String> traversalResult = new ArrayList<>();
        inorderTraversal(root, traversalResult);
        return traversalResult;
    }

    // Preorder traversal for binary tree
    public List<String> traversePreOrder() {
        List<String> traversalResult = new ArrayList<>();
        preorderTraversal(root, traversalResult);
        return traversalResult;
    }

    // Postorder traversal for binary tree
    public List<String> traversePostOrder() {
        List<String> traversalResult = new ArrayList<>();
        postorderTraversal(root, traversalResult);
        return traversalResult;
    }

    // Inorder traversal method
    private void inorderTraversal(QuestionBTNode node, List<String> traversalResult) {
        if (node != null) {
            inorderTraversal(node.getLeft(), traversalResult);
            traversalResult.add(node.getQuestionData().toString());
            inorderTraversal(node.getRight(), traversalResult);
        }
    }

    // Preorder traversal method
    private void preorderTraversal(QuestionBTNode node, List<String> traversalResult) {
        if (node != null) {
            traversalResult.add(node.getQuestionData().toString());
            preorderTraversal(node.getLeft(), traversalResult);
            preorderTraversal(node.getRight(), traversalResult);
        }
    }

    // Postorder traversal method
    private void postorderTraversal(QuestionBTNode node, List<String> traversalResult) {
        if (node != null) {
            postorderTraversal(node.getLeft(), traversalResult);
            postorderTraversal(node.getRight(), traversalResult);
            traversalResult.add(node.getQuestionData().toString());
        }
    }

    // Method to get the root of the binary tree
    public QuestionBTNode getRoot() {
        return root;
    }

    // Method to search for a node with a specific question number
    public QuestionBTNode search(String questionNumber) {
        return searchRecursive(root, questionNumber);
    }

    // Helper method for recursive search
    private QuestionBTNode searchRecursive(QuestionBTNode root, String questionNumber) {
        if (root == null || root.getQuestionData().getSurveyQuestion()[0].equals(questionNumber)) {
            return root;
        }

        // Recursively search in the left subtree if the question number is less than the root's question number
        if (questionNumber.compareTo(root.getQuestionData().getSurveyQuestion()[0]) < 0) {
            return searchRecursive(root.getLeft(), questionNumber);
        }

        // Recursively search in the right subtree if the question number is greater than the root's question number
        return searchRecursive(root.getRight(), questionNumber);
    }
}
