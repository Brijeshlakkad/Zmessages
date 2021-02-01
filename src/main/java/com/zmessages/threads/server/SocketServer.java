package com.zmessages.threads.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Accepts the client socket connection and starts its thread to watch for client input stream
 */
public class SocketServer extends Thread {
    /**
     * Server socket
     */
    private final ServerSocket server;
    private volatile boolean exit = false;

    /**
     * Creates the server socket with given port
     *
     * @param port the port on which the server socket will be created
     * @throws IOException throws exception if the port is already in use
     */
    SocketServer(int port) throws IOException {
        this.server = new ServerSocket(port);
    }

    public void run() {
        while (!exit) {
            try {
                // Accepts the client socket connection
                Socket client = server.accept();
                System.out.println("New connection from " + client.getRemoteSocketAddress());
                // https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API/Writing_a_WebSocket_server_in_Java
                // WebSockets communicate over a TCP (Transmission Control Protocol) connection
                // Needs handshake
                InputStream in = client.getInputStream();
                OutputStream out = client.getOutputStream();

                String key = "";
                Scanner s = new Scanner(in, "UTF-8");
                try {
                    String data = s.useDelimiter("\\r\\n\\r\\n").next();
                    Matcher get = Pattern.compile("^GET").matcher(data);
                    if (get.find()) {
                        Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
                        match.find();
                        key = match.group(1);
                        byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                                + "Connection: Upgrade\r\n"
                                + "Upgrade: websocket\r\n"
                                + "Sec-WebSocket-Accept: "
                                + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes(StandardCharsets.UTF_8)))
                                + "\r\n\r\n").getBytes(StandardCharsets.UTF_8);
                        out.write(response, 0, response.length);
                    }
                } catch (Exception ignored) {
                    System.out.println("Handshake failed");
                }
                if(!key.isEmpty()){
                    ClientThread clientThread = new ClientThread(client, key);
                    clientThread.start();

                    // Add the client socket to the list of active clients
                    TCPServer.activeClients.add(clientThread);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void terminate() {
        this.exit = true;
    }
}
