package com.example.javaassignment.Networking;

import com.example.javaassignment.MainController;

import java.net.*;
import java.io.*;

public class MainThread extends Thread
{

    private Socket socket = null;
    private MainController client1 = null;
    private DataInputStream streamIn = null;

    public MainThread(MainController _client1, Socket _socket)
    {
        client1 = _client1;
        socket = _socket;
        open();
        start();
    }

    public void open()
    {
        try
        {
            streamIn = new DataInputStream(socket.getInputStream());
        }
        catch (IOException ioe)
        {
            System.out.println("Error getting input stream: " + ioe);
            //client1.stop();
            client1.close();
        }
    }

    public void close()
    {
        try
        {
            if (streamIn != null)
            {
                streamIn.close();
            }
        }
        catch (IOException ioe)
        {
            System.out.println("Error closing input stream: " + ioe);
        }
    }

    public void run()
    {
        while (true)
        {
            try
            {
                client1.handle(streamIn.readUTF());
            }
            catch (IOException ioe)
            {
                System.out.println("Listening error: " + ioe.getMessage());
                //client1.stop();
                client1.close();
            }
        }
    }
}

