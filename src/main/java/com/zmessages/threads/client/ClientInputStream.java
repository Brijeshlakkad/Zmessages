package com.zmessages.threads.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Handles client input channel
 */
public class ClientInputStream extends Thread {
    private volatile boolean exit = false;
    /**
     * Username of the client
     */
    private final String username;
    /**
     * Client socket at client-side
     */
    private final Socket currentClient;

    public ClientInputStream(Socket currentClient, String username) {
        this.currentClient = currentClient;
        this.username = username;
    }

    public void run() {
        try {
            // Gets the output stream
            OutputStream os = this.currentClient.getOutputStream();
            while (!exit) {
                System.out.println("Enter message: ");

                // Wait for user input
                try {
                    // Enter data using BufferReader
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(System.in));

                    // Reading data using readLine
                    String message = reader.readLine();

                    // JSONObject input = new JSONObject();
                    // input.put("username",  this.username);
                    // input.put("message", message);
                    // Send JSON data composed of the message
                    // If data transferred over the air, it can be encrypted?
                    // We could use JSONObject and stringify it
                    // But knowing that this file will be run alone, we will format the string here.
                    String formattedString = String.format("%s: %s", username, message);

                    // Send bytes from string
                    os.write(formattedString.getBytes());
                } catch (Exception ignored) {
                    System.out.println("Error while sending the message!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void terminate() {
        this.exit = true;
    }
}
