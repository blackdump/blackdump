package com.github.blackdump.shell.commands;

import com.github.blackdump.annotations.ABDShellCommand;
import com.github.blackdump.base.BaseShellCommand;
import com.github.blackdump.utils.AppInfo;

/**
 * Comando per stampare l'header per OS
 */
@ABDShellCommand(commands = {"header"}, help = "Print SquidOS header", loadAtStartup = true)
public class HeaderCommand extends BaseShellCommand {

    @Override
    public Object invoke(String[] args) {
        return AppInfo.AppSquidOsHeader;
    }
}
