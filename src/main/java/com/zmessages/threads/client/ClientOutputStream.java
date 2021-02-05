package com.zmessages.threads.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientOutputStream extends Thread {
    private volatile boolean exit = false;
    /**
     * Client socket at client-side
     */
    private final Socket currentClient;

    public ClientOutputStream(Socket currentClient) {
        this.currentClient = currentClient;
    }

    public void run() {
        // Buffer to save the incoming message
        byte[] buffer;
        int read;

        try {
            // Gets InputStream from client socket
            InputStream inputStream = this.currentClient.getInputStream();
            while (!exit) {
                buffer = new byte[1024];
                // Reads from the client socket input stream
                read = inputStream.read(buffer);
                if (read < 0) {
                    this.terminate();
                    continue;
                }
                String dataString = new String(buffer);
                // username will be set to 'Anonymous' if it is null
                // Hence, every time there will be username
                if (!dataString.isEmpty()) {
                    System.out.println("-^-^-^-^-^-^-^-^-^-^-");
                    System.out.println(dataString);
                    System.out.println("-^-^-^-^-^-^-^-^-^-^-");
                    System.out.println("Enter Message: ");
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void terminate() {
        this.exit = true;
    }
}
