package com.example.javaassignment.Networking;

import com.example.javaassignment.ClientController;

import java.net.*;
import java.io.*;

public class ClientThread extends Thread
{

    private Socket socket = null;
    private ClientController client2 = null;
    private DataInputStream streamIn = null;

    public ClientThread(ClientController _client2, Socket _socket)
    {
        client2 = _client2;
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
            //client2.stop();
            client2.close();
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
                client2.handle(streamIn.readUTF());
            }
            catch (IOException ioe)
            {
                System.out.println("Listening error: " + ioe.getMessage());
                //client2.stop();
                client2.close();
            }
        }
    }
}

