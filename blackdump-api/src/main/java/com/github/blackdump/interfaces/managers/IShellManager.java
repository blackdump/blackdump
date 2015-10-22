package com.github.blackdump.interfaces.managers;

/**
 * Interfaccia per la creazione dello shell manager
 */
public interface IShellManager {

    void addCommand(Class<?> classCommand);

    void parse(String input);

}
