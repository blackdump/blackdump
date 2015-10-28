package com.github.blackdump.webserver.websockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by squid on 10/28/15.
 */

public class RandomDataGenerator implements
        ApplicationListener<BrokerAvailabilityEvent> {

    private final MessageSendingOperations<String> messagingTemplate;

    @Autowired
    public RandomDataGenerator( final MessageSendingOperations<String> messagingTemplate)
    {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent event) {

    }

    @Scheduled(fixedDelay = 1000)
    public void sendDataUpdates() {

        this.messagingTemplate.convertAndSend(
                "/data", new Random().nextInt(100));

    }
}
