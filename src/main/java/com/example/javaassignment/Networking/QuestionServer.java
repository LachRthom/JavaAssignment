package com.example.javaassignment.Networking;

import java.net.*;
import java.io.*;

public class QuestionServer implements Runnable
{

    private ServerThread clients[] = new ServerThread[50];
    private ServerSocket server = null;
    private Thread thread = null;
    private int clientCount = 0;
    private volatile boolean running = true; // Flag to control the server thread execution

    public static void main(String args[])
    {
        QuestionServer server;
        if (args.length != 1)
        {
            //System.out.println("Usage: java ChatServer port");
            server = new QuestionServer(4444);
        }
        else
        {
            server = new QuestionServer(Integer.parseInt(args[0]));
        }
    }

    public QuestionServer(int port)
    {
        try
        {
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            start();
        }
        catch (IOException ioe)
        {
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
        }
    }

    public void run() {
        while (running) {
            try {
                System.out.println("Waiting for a client ...");
                addThread(server.accept());
            } catch (IOException ioe) {
                if (!running) { // Check if the server is supposed to stop
                    System.out.println("Server stopped.");
                    break;
                }
                System.out.println("Server accept error: " + ioe);
                // No need to call stop() here
            }
        }
    }

    public void start()
    {
        if (thread == null)
        {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        running = false; // Set the flag to false to stop the server thread
        try {
            if (server != null) {
                server.close(); // Close the server socket
            }
        } catch (IOException ioe) {
            System.out.println("Error closing server socket: " + ioe);
        }
        // Interrupt all client threads to unblock their operations and let them terminate gracefully
        for (int i = 0; i < clientCount; i++) {
            clients[i].stopThread();
        }
    }

    private int findClient(int ID)
    {
        for (int i = 0; i < clientCount; i++)
        {
            if (clients[i].getID() == ID)
            {
                return i;
            }
        }
        return -1;
    }

    public synchronized void handle(int ID, String input)
    {
        if (input.equals(".bye"))
        {
            clients[findClient(ID)].send(".bye");
            remove(ID);
        }
        else
        {
            for (int i = 0; i < clientCount; i++)
            {
                //if(clients[i].getID() != ID)
                clients[i].send(ID + ": " + input);
            }
        }
    }

    public synchronized void remove(int ID) {
        int pos = findClient(ID);
        if (pos >= 0) {
            ServerThread toTerminate = clients[pos];
            System.out.println("Removing client thread " + ID + " at " + pos);
            if (pos < clientCount - 1) {
                for (int i = pos + 1; i < clientCount; i++) {
                    clients[i - 1] = clients[i];
                }
            }
            clientCount--;
            try {
                toTerminate.close();
            } catch (IOException ioe) {
                System.out.println("Error closing thread: " + ioe);
            }
            // Instead of calling stop(), call stopThread() to gracefully terminate the thread
            toTerminate.stopThread();
            // Remove reference to the terminated thread to allow for garbage collection
            clients[pos] = null;
        }
    }


    private void addThread(Socket socket)
    {
        if (clientCount < clients.length)
        {
            System.out.println("Client accepted: " + socket);
            clients[clientCount] = new ServerThread(this, socket);
            try
            {
                clients[clientCount].open();
                clients[clientCount].start();
                clientCount++;
            }
            catch (IOException ioe)
            {
                System.out.println("Error opening thread: " + ioe);
            }
        }
        else
        {
            System.out.println("Client refused: maximum " + clients.length + " reached.");
        }
    }

}

