package com.zmessages.threads.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {
    static final String DEFAULT_USERNAME = "anonymous";

    public static void main(String[] args) throws Exception {
        try {
            InetAddress ia = InetAddress.getByName("localhost");

            System.out.println("Enter username (press `enter` to continue with anonymous user):");

            // Ask username to show other clients the sender of the message
            // If user skips, this client socket will refer 'anonymous' as username
            // Enter data using BufferReader
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(System.in));

            // Reading data using readLine
            String username = reader.readLine();
            if (username.isEmpty()) {
                username = DEFAULT_USERNAME;
            }

            System.out.println(String.format("Hello %s!", username));

            System.out.println("Connecting to server...");

            // Connects to the server socket at provided hostname and port
            Socket currentClient = new Socket(ia, 9999);

            if (currentClient.isConnected()) {
                System.out.println("Connected to server...");
                // Steps the thread to send message to the server socket
                ClientInputStream clientInputStream = new ClientInputStream(currentClient, username);
                // Steps the thread to read the input stream from the server socket
                ClientOutputStream clientOutputStream = new ClientOutputStream(currentClient);

                clientInputStream.start();
                clientOutputStream.start();
            } else {
                System.out.println("You are not connected!");
            }

        } catch (Throwable t) {
            throw new Exception("Server is not up");
        }
    }
}
