package com.github.blackdump.shell;

import com.github.blackdump.interfaces.engine.IBlackdumpEngine;

/**
 * Interfaccia per creare i comandi per la shell
 */
public interface IShellCommand {

    String getCommands();

    String getHelp();

    boolean getIsLoadAtStartup();

    void setEngine(IBlackdumpEngine engine);

    Object invoke(String[] args);
}
