package com.github.blackdump.shell.commands;

import com.github.blackdump.annotations.ABDShellCommand;
import com.github.blackdump.base.BaseShellCommand;
import com.github.blackdump.eventbus.EventBusMessages;
import com.github.blackdump.eventbus.ObservableVariablesManager;

/**
 * Esegue il test delle notifications
 */
@ABDShellCommand(commands = {"testnotify"}, help = "Test notification", loadAtStartup = true)
public class TestNofitication extends BaseShellCommand {
    @Override
    public Object invoke(String[] args) {
        ObservableVariablesManager.updateVariable(EventBusMessages.NOTIFICATION_SHOW_MESSAGE, transformToRawString(args));
        return "OK";
    }
}
