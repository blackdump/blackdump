package com.github.blackdump.network;

/**
 * WebSocket client per collegarsi al server
 */

import com.github.blackdump.data.network.BlackdumpMessage;
import com.github.blackdump.eventbus.EventBusMessages;
import com.github.blackdump.eventbus.ObservableVariablesManager;
import com.github.blackdump.interfaces.network.IBlackdumpClientListener;
import com.github.blackdump.serializer.JsonSerializer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.websocket.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@ClientEndpoint
public class BlackdumpClient {


    public static Object waitLock = new Object();
    private static Logger mLogger = Logger.getLogger(BlackdumpClient.class);
    private Session mCurrentSession;
    private WebSocketContainer mContainer;
    private List<IBlackdumpClientListener> mListeners = new ArrayList<>();
    private String serverUrl;

    public BlackdumpClient(String serverUrl) {
        this.serverUrl = serverUrl;
        ObservableVariablesManager.subscribe(EventBusMessages.NETWORK_SEND_MESSAGE, o -> {

            BlackdumpMessage message = (BlackdumpMessage) o;
            String strMessage = JsonSerializer.Serialize(message);

            mCurrentSession.getAsyncRemote().sendText(strMessage);
        });
    }

    public void connect() {
        try {
            mContainer = ContainerProvider.getWebSocketContainer();

            notifyConnecting();

            mCurrentSession = mContainer.connectToServer(this, URI.create(serverUrl));

            notifyConnected();

        } catch (Exception ex) {
            log(Level.FATAL, "Error during connect %s ==> %s", serverUrl, ex.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String message) {
        try {
            BlackdumpMessage blackdumpMessage = JsonSerializer.Deserialize(message, BlackdumpMessage.class);

            ObservableVariablesManager.updateVariable(EventBusMessages.NETWORK_ON_MESSAGE, blackdumpMessage);

            for (int i = 0; i < mListeners.size(); i++) {
                mListeners.get(i).onMessage(blackdumpMessage);
            }
        } catch (Exception ex) {
            log(Level.FATAL, "Error during receiving network server => %s", ex.getMessage());
        }

    }

    public void addListener(IBlackdumpClientListener listener) {
        mListeners.add(listener);
    }

    public void notifyConnecting() {
        for (int i = 0; i < mListeners.size(); i++) {
            mListeners.get(i).onConnecting(serverUrl);
        }

    }

    public void notifyConnected() {
        for (int i = 0; i < mListeners.size(); i++) {
            mListeners.get(i).onConnected(serverUrl);
        }
    }

    protected void log(Level level, String text, Object... args) {
        mLogger.log(level, String.format(text, args));
    }
}
