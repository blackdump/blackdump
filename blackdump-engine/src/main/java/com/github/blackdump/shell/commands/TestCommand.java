package com.github.blackdump.shell.commands;

import com.github.blackdump.annotations.ABDShellCommand;
import com.github.blackdump.base.BaseShellCommand;

/**
 * Comando semplice per il funzionamento dello ShellManager
 */
@ABDShellCommand(commands = {"test", "testcmd"}, help = "Test functionality of shell manager", loadAtStartup = true)
public class TestCommand extends BaseShellCommand {

    @Override
    public Object invoke(String[] args) {
        return String.format("OK  args => %s", args);
    }
}
