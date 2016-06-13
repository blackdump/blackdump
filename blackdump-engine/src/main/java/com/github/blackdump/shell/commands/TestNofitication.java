package com.github.blackdump.shell.commands;

import com.github.blackdump.annotations.ABDShellCommand;
import com.github.blackdump.base.BaseShellCommand;
import com.github.blackdump.data.ui.WidgetBuiltData;
import com.github.blackdump.eventbus.EventBusMessages;
import com.github.blackdump.eventbus.ObservableVariablesManager;
import com.github.blackdump.interfaces.windows.dialogs.DialogTypeEnum;
import com.github.blackdump.interfaces.windows.dialogs.ITimerWidgetListener;
import com.github.blackdump.ui.widgets.InputStringDialogWidget;
import com.github.blackdump.ui.widgets.TimerWidget;
import com.github.blackdump.utils.AppInfo;
import com.github.blackdump.utils.NotificationUtil;

/**
 * Esegue il test delle notifications
 */
@ABDShellCommand(commands = {"testnotify"}, help = "Test notification", loadAtStartup = true)
public class TestNofitication extends BaseShellCommand {
    @Override
    public Object invoke(String[] args) {
       // NotificationUtil.showNotification(DialogTypeEnum.INFO, AppInfo.AppName, "CIAO");

        WidgetBuiltData data =  getEngine().getUiManager().buildWidget("timerwidget");

        TimerWidget prgProgressDialogWidget = (TimerWidget)data.getWidgetController();

        prgProgressDialogWidget.center();
        prgProgressDialogWidget.left();

        getEngine().getUiManager().addDesktopChildren(data.getWidgetPane());

        prgProgressDialogWidget.setListener(new ITimerWidgetListener() {
            @Override
            public void onTimerStart() {
                getShellManager().parse("echo", "Terminal started");
            }

            @Override
            public void onTimerHeartbeat(long seconds) {
                getShellManager().parse("echo", "Terminal heartbeat");

            }

            @Override
            public void onTimerExpire() {
                getShellManager().parse("echo", "BOOM");

            }
        });

        prgProgressDialogWidget.start(15, "Test");
        return "OK";
    }
}
