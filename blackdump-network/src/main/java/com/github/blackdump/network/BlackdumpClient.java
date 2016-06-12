package com.github.blackdump.network;

import com.github.blackdump.eventbus.EventBusMessages;
import com.github.blackdump.eventbus.ObservableVariablesManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * WebSocket client per collegarsi al server
 */


public class BlackdumpClient {

    public String serverUrl;


    private Logger mLogger;


    public BlackdumpClient(String serverUrl)
    {
        this.mLogger = Logger.getLogger(getClass());
        this.serverUrl = serverUrl;
        ObservableVariablesManager.subscribe(EventBusMessages.NETWORK_SEND_MESSAGE, this::onMessage);
    }

    protected void log(Level level,String text, String ... args)
    {
        mLogger.log(level, String.format(text, args));
    }

    public void connect()
    {
        try
        {


        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during connect to host => %s", ex.getMessage());
        }

    }

    private void onMessage(Object obj)
    {
        log(Level.INFO, "Receiving message from eventBus type ==> %s", obj.getClass().getSimpleName());

    }

}
