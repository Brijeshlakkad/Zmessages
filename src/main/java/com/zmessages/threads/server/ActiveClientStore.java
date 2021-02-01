package com.zmessages.threads.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores the list of active clients
 * @param <T> Type of the list; can be thread or socket
 */
public class ActiveClientStore<T> {
    /**
     * To keep the track of all connected clients
     */
    private final List<T> clientList = new ArrayList<>();

    /**
     * Adds client to the list and shows the result of the operation
     * @param client client socket needs to be added
     */
    public void add(T client) {
        this.clientList.add(client);
        this.showTotalActiveClients();
    }

    /**
     * Removes client from the list and shows the result of the operation
     * @param client client socket needs to be removed
     */
    public void remove(T client) {
        this.clientList.remove(client);
        this.showTotalActiveClients();
    }

    /**
     * Gets the list of all clients except the client provided in the argument
     * @param thatClient client which needs to be ignored from the list
     */
    public List<T> getClientList(T thatClient) {
        return this.clientList.stream().filter((client) ->
                !thatClient.equals(client)
        ).collect(Collectors.toList());
    }

    /**
     * Gets all the clients
     * @return list of the clients
     */
    public List<T> getAllClientList() {
        return this.clientList;
    }

    /**
     * Shows the total number of active clients
     */
    public void showTotalActiveClients() {
        System.out.println("Active clients: " + this.clientList.size());
    }
}
