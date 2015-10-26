package com.github.blackdump.server;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by squid on 10/25/15.
 */
@Component
public class BlackdumpServer extends Endpoint implements MessageHandler.Whole<String> {
    //queue holds the list of connected clients
    private static Queue<Session> queue = new ConcurrentLinkedQueue<Session>();
    private static Thread rateThread; //rate publisher thread


    public BlackdumpServer()
    {
        System.out.println("CREATED");
    }


    @Override
    public void onError(Session session, Throwable thr) {
        queue.remove(session);
        System.err.println("Error on session " + session.getId());
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        queue.remove(session);
        System.out.println("session closed: " + session.getId());
    }



    @Override
    public void onOpen(Session session, EndpointConfig config) {
        queue.add(session);
        session.addMessageHandler(this);
        System.out.println("New session opened: " + session.getId());
    }

    @Override
    public void onMessage(String message) {

    }
}
