package com.github.blackdump.server;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by squid on 10/25/15.
 */
@ServerEndpoint("/server")
public class BlackdumpServer {
    //queue holds the list of connected clients
    private static Queue<Session> queue = new ConcurrentLinkedQueue<Session>();
    private static Thread rateThread; //rate publisher thread


    @OnMessage
    public void onMessage(Session session, String msg) {
//provided for completeness, in out scenario clients don't send any msg.
        try {
            System.out.println("received msg " + msg + " from " + session.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void open(Session session) {
        queue.add(session);
        System.out.println("New session opened: " + session.getId());
    }

    @OnError
    public void error(Session session, Throwable t) {
        queue.remove(session);
        System.err.println("Error on session " + session.getId());
    }

    @OnClose
    public void closedConnection(Session session) {
        queue.remove(session);
        System.out.println("session closed: " + session.getId());
    }

}
