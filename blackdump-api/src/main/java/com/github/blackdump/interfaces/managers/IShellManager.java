package com.github.blackdump.interfaces.managers;

import com.github.blackdump.interfaces.shell.IShellCommandResult;

/**
 * Interfaccia per la creazione dello shell manager
 */
public interface IShellManager {

    void addCommand(Class<?> classCommand);

    void parse(String terminal, String input);

    void addShellCommandResult(String terminal, IShellCommandResult result);

    void removeShellCommandResult(String terminal, IShellCommandResult result);

}
