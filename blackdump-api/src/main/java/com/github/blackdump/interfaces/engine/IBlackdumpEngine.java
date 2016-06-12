package com.github.blackdump.interfaces.engine;

import com.github.blackdump.data.BDConfig;
import com.github.blackdump.interfaces.managers.IBlackdumpManager;
import com.github.blackdump.interfaces.managers.IShellManager;
import com.github.blackdump.interfaces.managers.IUiManager;
import com.github.blackdump.interfaces.persistence.IBDDatabaseManager;
import com.github.blackdump.interfaces.wrap.IPlayerManager;
import rx.functions.Action1;

import java.lang.reflect.Type;

/**
 * Interfaccia per esporre l'engine
 */
public interface IBlackdumpEngine {

    /***
     * Inizializza l'engine passando i parametri input
     *
     * @param args
     */
    void init(String[] args);


    /**
     * Ritorna la directory home dell'enegine
     *
     * @return
     */
    String getRootDirectory();

    BDConfig getConfig();


    /**
     * Aggiunge una nuovo manager
     * @param manager
     */
    void addManager(IBlackdumpManager manager);

    <T> T getManagerByClass(Type type);


    /**
     * Aggiunge una variabile RX/ all'evento
     * @param varname
     * @param onNext
     */
    void subscribeEvent(String varname, Action1<? super Object> onNext);


    /**
     * Esegue il broadcast dell'evento
     * @param varname
     * @param value
     */
    void broadcastEvent(String varname, Object value);


    IBDDatabaseManager getDatabaseManager();

    IUiManager getUiManager();

    IShellManager getShellManager();

    IPlayerManager getPlayerManager();


    /**
     * Esce dall'applicazione, se e' impostato il force a TRUE, chiude tutto subito
     * @param force
     */
    void quit(boolean force);
}
