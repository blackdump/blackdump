package com.github.blackdump.webserver.controllers;

import com.github.blackdump.data.network.BDMessageTypes;
import com.github.blackdump.data.network.BlackdumpMessage;
import com.github.blackdump.persistence.users.User;
import com.github.blackdump.serializer.JsonSerializer;
import com.github.blackdump.webserver.controllers.base.BaseController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

/**
 * Appena il client si connette, spedisce le informazioni del server
 */
@Controller

public class WelcomeController extends BaseController {

    @RequestMapping(value ="/welcome",method = RequestMethod.GET)

    public String sayHello(ModelMap model) {

        model.addAttribute("greeting", "Hello World from Spring 4 MVC");
        model.addAttribute("data", JsonSerializer.Deserialize(BDMessageTypes.buildErrorMessage("test"), BlackdumpMessage.class));
        return "welcome";
    }

    @RequestMapping(value="/helloagain", method = RequestMethod.GET)
    public String sayHelloAgain(ModelMap model, String q) {
        model.addAttribute("greeting", "Hello World Again, from Spring 4 MVC and Q is" + q);
        return "welcome";
    }

    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public BlackdumpMessage sendMessage(BlackdumpMessage message) {
        return new BlackdumpMessage(1, new Date());
    }
}