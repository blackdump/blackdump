package com.github.blackdump.interfaces.engine;

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



}
