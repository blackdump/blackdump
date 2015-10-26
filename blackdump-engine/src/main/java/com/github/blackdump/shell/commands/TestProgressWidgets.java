package com.github.blackdump.shell.commands;

import com.github.blackdump.annotations.ABDShellCommand;
import com.github.blackdump.base.BaseShellCommand;
import com.github.blackdump.data.ui.WidgetBuiltData;
import com.github.blackdump.ui.widgets.ProgressDialogWidget;

/**
 * Comando per testare i progress dialog
 */
@ABDShellCommand(commands = {"testwidget"}, help = "Test progress widget", loadAtStartup = true)
public class TestProgressWidgets  extends BaseShellCommand {

    @Override
    public Object invoke(String[] args) {
        WidgetBuiltData data =  getEngine().getUiManager().buildWidget("progressdialog");

        ProgressDialogWidget prgProgressDialogWidget = (ProgressDialogWidget)data.getWidgetController();

        data.getWidgetPane().setLayoutX(getEngine().getUiManager().getDesktopWidth() / 2 - data.getWidgetPane().getPrefWidth() / 2);
        data.getWidgetPane().setLayoutY(getEngine().getUiManager().getDesktopHeight() / 2 - data.getWidgetPane().getPrefHeight() / 2);

        getEngine().getUiManager().addDesktopChildren(data.getWidgetPane());
        prgProgressDialogWidget.startProgress("//Dowload updates", "0 %",250, 100, () -> data.getWidgetPane().setVisible(false));



        return "OK";
    }
}
