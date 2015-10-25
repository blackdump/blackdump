package com.github.blackdump.server;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import java.util.Set;

/**
 * Created by squid on 10/25/15.
 */
public class StartApp implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        ServerContainer container = (ServerContainer) ctx.getAttribute("javax.websocket.server.ServerContainer");
        try {
            container.addEndpoint(BlackdumpServer.class);
        } catch (DeploymentException e) {
            throw new ServletException("Unable to deploy websocket", e);
        }
    }
}
