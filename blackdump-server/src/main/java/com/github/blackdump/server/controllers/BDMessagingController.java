package com.github.blackdump.server.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by squid on 10/26/15.
 */
@Controller
public class BDMessagingController {


    @MessageMapping("/server")
    @SendTo("/topic/server")
    public String message(String message) throws Exception
    {
        Thread.sleep(3000); // simulated delay
        return "";
    }
}
