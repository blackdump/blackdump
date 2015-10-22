package com.github.blackdump.interfaces.persistence;

import com.github.blackdump.persistence.BDDatabase;

/**
 * Interfaccia per gestire il database
 */
public interface IBDDatabaseManager {

    /**
     * Salva il database su disco
     */
    void saveDatabase();

    void loadDatabase();

    BDDatabase getDatabase();
}
