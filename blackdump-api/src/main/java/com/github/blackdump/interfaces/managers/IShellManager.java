package com.github.blackdump.interfaces.managers;

import com.github.blackdump.interfaces.shell.IShellCommandResult;
import com.github.blackdump.shell.IShellCommand;

import java.util.HashMap;

/**
 * Interfaccia per la creazione dello shell manager
 */
public interface IShellManager {

    void addCommand(Class<?> classCommand);

    void parse(String terminal, String input);

    void addShellCommandResult(String terminal, IShellCommandResult result);

    void removeShellCommandResult(String terminal, IShellCommandResult result);

    HashMap<String, IShellCommand> getAvaiableCommands();

    HashMap<String, IShellCommand> getInstalledCommands();


    String getPrompt(String terminal);

}
