package com.github.blackdump.interfaces.shell;

import com.github.blackdump.shell.IShellCommand;

/**
 * Interfaccia che notifica l'esecuzione del comando
 */
public interface IShellCommandResult {

    void onCommandResult(IShellCommand shellExecuter, String cmd, String[] args, Object result);

    void onCommandNotFound(String cmd, String[] args);
}
