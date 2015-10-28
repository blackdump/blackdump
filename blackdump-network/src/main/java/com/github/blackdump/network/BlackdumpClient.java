package com.github.blackdump.network;

import com.github.blackdump.eventbus.EventBusMessages;
import com.github.blackdump.eventbus.ObservableVariablesManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.fusesource.hawtbuf.AsciiBuffer;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.stomp.client.Callback;
import org.fusesource.stomp.client.CallbackConnection;
import org.fusesource.stomp.client.Stomp;
import org.fusesource.stomp.codec.StompFrame;

/**
 * WebSocket client per collegarsi al server
 */


public class BlackdumpClient {

    public String serverUrl;

    private Stomp mStomp;

    private StompFrame mHandleFrame;
    private Logger mLogger;

    private AsciiBuffer mId;

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
            mStomp = new Stomp(serverUrl.split(":")[0], Integer.parseInt(serverUrl.split(":")[1]));

            log(Level.INFO, "Connecting to %s", serverUrl);

            mStomp.connectCallback(new Callback<CallbackConnection>() {

                @Override
                public void onSuccess(CallbackConnection value) {
                    log(Level.INFO, "Connection is OK");
                    mId = value.nextId();

                }

                @Override
                public void onFailure(Throwable value) {
                    log(Level.ERROR, "Connection error => %s", value.getMessage());
                }
            });


            mHandleFrame = new StompFrame(Buffer.ascii("SUBSCRIBE"));
            mHandleFrame.addHeader(Buffer.ascii("DESTINATION"), StompFrame.encodeHeader("/queue/"));
            mHandleFrame.addHeader(Buffer.ascii("ID"), mId);

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
