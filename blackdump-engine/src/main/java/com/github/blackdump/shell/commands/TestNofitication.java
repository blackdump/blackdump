package com.github.blackdump.shell.commands;

import com.github.blackdump.annotations.ABDShellCommand;
import com.github.blackdump.base.BaseShellCommand;
import com.github.blackdump.eventbus.EventBusMessages;
import com.github.blackdump.eventbus.ObservableVariablesManager;
import com.github.blackdump.interfaces.windows.dialogs.DialogTypeEnum;
import com.github.blackdump.utils.AppInfo;
import com.github.blackdump.utils.NotificationUtil;

/**
 * Esegue il test delle notifications
 */
@ABDShellCommand(commands = {"testnotify"}, help = "Test notification", loadAtStartup = true)
public class TestNofitication extends BaseShellCommand {
    @Override
    public Object invoke(String[] args) {
        NotificationUtil.showNotification(DialogTypeEnum.INFO, AppInfo.AppName, "CIAO");
        return "OK";
    }
}
