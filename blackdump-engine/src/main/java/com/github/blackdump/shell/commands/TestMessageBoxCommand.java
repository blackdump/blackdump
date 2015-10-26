package com.github.blackdump.shell.commands;

import com.github.blackdump.annotations.ABDShellCommand;
import com.github.blackdump.base.BaseShellCommand;
import com.github.blackdump.data.ui.WidgetBuiltData;
import com.github.blackdump.ui.widgets.MessageDialogWidget;

/**
 * Created by squid on 10/26/15.
 */
@ABDShellCommand(commands = {"testmsgbox"}, help = "Test message box", loadAtStartup = true)
public class TestMessageBoxCommand extends BaseShellCommand {

    @Override
    public Object invoke(String[] args) {
        WidgetBuiltData data =  getEngine().getUiManager().buildWidget("messagedialog");

        MessageDialogWidget prgProgressDialogWidget = (MessageDialogWidget)data.getWidgetController();

        data.getWidgetPane().setLayoutX(getEngine().getUiManager().getDesktopWidth() / 2 - data.getWidgetPane().getPrefWidth() / 2);
        data.getWidgetPane().setLayoutY(getEngine().getUiManager().getDesktopHeight() / 2 - data.getWidgetPane().getPrefHeight() / 2);

        getEngine().getUiManager().addDesktopChildren(data.getWidgetPane());
        prgProgressDialogWidget.show("// Blackdump", "Are you sure ?", () -> data.getWidgetPane().setVisible(false));

        return "OK";
    }
}
