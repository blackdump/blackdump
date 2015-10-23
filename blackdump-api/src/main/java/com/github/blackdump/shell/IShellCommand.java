package com.github.blackdump.shell;

import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.managers.IShellManager;

/**
 * Interfaccia per creare i comandi per la shell
 */
public interface IShellCommand {

    String getCommands();

    String getHelp();

    boolean getIsLoadAtStartup();

    void setEngine(IBlackdumpEngine engine);

    void setShellManager(IShellManager shellManager);

    Object invoke(String[] args);
}
