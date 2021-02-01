package com.zmessages.threads.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

/**
 * Thread to handle input stream from the client socket
 */
public class ClientThread extends Thread {
    /**
     * Points to the socket of the client which has made connection to this server socket
     */
    private Socket client;
    private String key;
    private volatile boolean exit = false;

    public ClientThread(Socket client, String key) {
        this.client = client;
        this.key = key;
    }

    public void run() {
        // Used to store the message read from the input stream
        byte[] encoded;
        byte[] message;
        byte[] decoded;
        byte[] key;
        int read;

        try {
            while (!exit) {
                InputStream is = client.getInputStream();
                message = new byte[1024];
                read = is.read(message);

                System.out.println("RECEIVED: " + read+":"+message[0]);
                if (message[0] == (byte) 129) {
                    int secondByteDifference = message[1] - 128;
                    int lengthOfTheMessage = 0;
                    if (secondByteDifference > 0 &&
                            secondByteDifference <= 125) {
                        lengthOfTheMessage = secondByteDifference;
                    }

                    if (lengthOfTheMessage > 0) {
                        key = new byte[4];
                        encoded = new byte[lengthOfTheMessage];
                        decoded = new byte[encoded.length];
                        System.arraycopy(message, 2, key, 0, 4);
                        System.arraycopy(message, 6, encoded, 0, lengthOfTheMessage);

                        for (int i = 0; i < encoded.length; i++) {
                            decoded[i] = (byte) (encoded[i] ^ key[i & 0x3]);
                        }

                        System.out.println("RECEIVED2: " + new String(decoded));
                        if (read < 0) {
                            this.terminate();
                            continue;
                        }
                        TCPServer.broadcastMessage(this, encoded);
                    }
                }
            }
        } catch (IOException e) {
            // Remove client if connection is not available anymore
            this.terminate();
        }
    }

    /**
     * Get the client socket of this thread
     *
     * @return client socket
     */
    public Socket getClient() {
        return client;
    }

    /**
     * Sets the client socket to this thread
     *
     * @param client client socket
     */
    public void setClient(Socket client) {
        this.client = client;
    }

    /**
     * Terminates this thread by breaking the while loop inside run method of the thread
     */
    public void terminate() {
        // Exit this thread
        this.exit = true;
        // Remove client thread from the list
        TCPServer.onRemoveClient(this);
    }

    /**
     * Matches the given object with `this` object
     *
     * @param o object to be matched with `this` object
     * @return true if the both objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientThread that = (ClientThread) o;
        return Objects.equals(client, that.client);
    }

    /**
     * Gets the hashcode of the instance of this thread
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(client);
    }
}