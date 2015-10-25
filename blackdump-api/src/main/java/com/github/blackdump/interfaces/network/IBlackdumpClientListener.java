package com.github.blackdump.interfaces.network;

import com.github.blackdump.data.network.BlackdumpMessage;

/**
 * Interfaccia per connettersi al server
 */
public interface IBlackdumpClientListener {

    void onConnecting(String serverUrl);

    void onConnected(String serverUrl);

    void onDisconnecting();

    void onDisconnected();

    void onMessage(BlackdumpMessage object);
}
