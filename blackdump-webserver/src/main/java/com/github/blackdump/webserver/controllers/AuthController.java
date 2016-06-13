package com.github.blackdump.webserver.controllers;


import com.github.blackdump.network.data.AuthResponse;
import com.github.blackdump.webserver.jpa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;



    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public @ResponseBody AuthResponse authorizeUser(@RequestParam("username") String username, @RequestParam("password") String password)
    {
        AuthResponse response = new AuthResponse();
        response.setType(AuthResponse.AuthResponseType.OK);
        response.setSessionId(UUID.randomUUID().toString().replace("-",""));
        return response;
    }


}
