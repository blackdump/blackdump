package com.github.blackdump.interfaces.persistence;

/**
 * Interfaccia per gestire il database
 */
public interface IBDDatabaseManager {

    /**
     * Salva il database su disco
     */
    void saveDatabase();

    void loadDatabase();
}
