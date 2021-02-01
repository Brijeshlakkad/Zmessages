package com.zmessages.threads.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Objects;

/**
 * Thread to handle input stream from the client socket
 */
public class ClientThread extends Thread {
    /**
     * Points to the socket of the client which has made connection to this server socket
     */
    private Socket client;
    private volatile boolean exit = false;

    public ClientThread(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            while (!exit) {
                // https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API/Writing_a_WebSocket_server_in_Java
                // https://tools.ietf.org/html/rfc6455#section-5.2
                // # Decoding a message
                InputStream is = client.getInputStream();
                byte[] data = new byte[1024];
                int read = is.read(data);
                // First byte should be 129
                // Convert byte to unsigned integer
                int firstByte = data[0] & 0xff;
                if (firstByte == 129) {
                    if (read < 0) {
                        this.terminate();
                        break;
                    }
                    // Size of the message
                    // As per RFC6455
                    int sizeOfMessage = (data[1] & 0xff) - 128;
                    byte[] decoded = new byte[sizeOfMessage];
                    byte[] key = new byte[]{data[2], data[3], data[4], data[5]};
                    for (int i = 0; i < sizeOfMessage; i++) {
                        decoded[i] = (byte) (data[i + 6] ^ key[i & 0x3]);
                    }
                    TCPServer.broadcastMessage(this, decoded);
                } else {
                    this.terminate();
                    break;
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