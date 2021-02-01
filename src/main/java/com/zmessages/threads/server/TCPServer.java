package com.zmessages.threads.server;

import org.springframework.stereotype.Service;
import sun.nio.cs.US_ASCII;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Server socket
 */
@Service
public class TCPServer {
    /**
     * Keeps the track of active client threads
     */
    static ActiveClientStore<ClientThread> activeClients = new ActiveClientStore<>();

    /**
     * Server socket wrapper
     */
    SocketServer socketServer;

    @PostConstruct
    public void postConstructor() throws Exception {
        try {
            // creates the server socket at port 9999
            this.socketServer = new SocketServer(9999);
            socketServer.start();
        } catch (IOException ioException) {
            System.out.println("Port is already being used");
        }
    }

    /**
     * Broadcasts the message to the active client sockets
     *
     * @param thatClientThread Client socket thread which has sent the message
     * @param bytes            message that needs to be broadcast
     */
    static void broadcastMessage(ClientThread thatClientThread, byte[] bytes) {
        // Iterate through all the clients to broadcast the message we received from one client
        for (ClientThread clientThread : activeClients.getAllClientList()) {
            // To not send the message to the sender
            if (!thatClientThread.getClient().equals(clientThread.getClient())) {
                try {
                    // Gets the output stream of the client socket to write the message
                    OutputStream os = clientThread.getClient().getOutputStream();

                    // section 5.1 of RFC 6455 does mention
                    // server should not mask the message
                    // https://tools.ietf.org/html/rfc6455
                    // https://stackoverflow.com/questions/16932662/websockets-disconnects-on-server-message
                    byte[] send = new byte[bytes.length + 2];
                    send[0] = (byte) 0x81; // first frame for text message
                    send[1] = (byte) bytes.length; // not masked, the total length of the next bytes
                    System.arraycopy(bytes, 0, send, 2, bytes.length);
                    os.write(send, 0, send.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Removes the thread from the list of active client threads
     *
     * @param clientThread client thread to be removed
     */
    static void onRemoveClient(ClientThread clientThread) {
        activeClients.remove(clientThread);
    }
}