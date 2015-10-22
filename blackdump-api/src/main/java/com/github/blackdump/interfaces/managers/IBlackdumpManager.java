package com.github.blackdump.interfaces.managers;

import com.github.blackdump.interfaces.engine.IBlackdumpEngine;

/**
 * Interfaccia per creare i manager
 */
public interface IBlackdumpManager {

    void start(IBlackdumpEngine engine);

    void dispose();

    void ready();
}
