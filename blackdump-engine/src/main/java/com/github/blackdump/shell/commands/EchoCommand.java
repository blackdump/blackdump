package com.github.blackdump.shell.commands;

import com.github.blackdump.annotations.ABDShellCommand;
import com.github.blackdump.base.BaseShellCommand;
import com.google.common.base.Strings;

/**
 * Implementa la possibilita di scrivere a schermo
 */
@ABDShellCommand(commands = {"echo"}, help = "Write to screen input", loadAtStartup = true)
public class EchoCommand extends BaseShellCommand {


    @Override
    public Object invoke(String[] args) {


        if (Strings.isNullOrEmpty(args[0]) && args.length == 1)
        {
            return "\n";
        }

        else
        {
            String raw = transformToRawString(args);

            raw = raw.replace("\"","");

            return raw + "\n";
        }
    }
}
