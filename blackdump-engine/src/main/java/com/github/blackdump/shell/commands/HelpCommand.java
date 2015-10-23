package com.github.blackdump.shell.commands;

import com.github.blackdump.annotations.ABDShellCommand;
import com.github.blackdump.base.BaseShellCommand;
import com.google.common.base.Strings;

/**
 * Stampa tutti i comandi disponibili
 */
@ABDShellCommand(commands = {"help,?"}, help = "Show this screen", loadAtStartup = true)
public class HelpCommand extends BaseShellCommand {


    @Override
    public Object invoke(String[] args) {

        StringBuilder stringBuilder = new StringBuilder();

        if (Strings.isNullOrEmpty(args[0]))
        {
            stringBuilder.append("[#red]Avaiable commands:\n");
            stringBuilder.append("------------\n");

            for (String command : getShellManager().getAvaiableCommands().keySet())
            {
                stringBuilder.append(String.format("%s : %s\n", command, getShellManager().getAvaiableCommands().get(command).getHelp()));
            }

            return stringBuilder.toString();

        }
        else
        {
            return "Asp\n";
        }

    }
}
