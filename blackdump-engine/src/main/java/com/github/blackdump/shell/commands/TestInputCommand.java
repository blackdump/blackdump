package com.github.blackdump.shell.commands;

import com.github.blackdump.annotations.ABDShellCommand;
import com.github.blackdump.base.BaseShellCommand;
import com.github.blackdump.data.ui.WidgetBuiltData;
import com.github.blackdump.interfaces.windows.dialogs.IInputStringDialogListener;
import com.github.blackdump.ui.widgets.InputStringDialogWidget;
import com.github.blackdump.ui.widgets.MessageDialogWidget;
import org.apache.log4j.Level;

/**
 * Created by squid on 10/26/15.
 */
@ABDShellCommand(commands = {"testinput"}, help = "Test input dialog", loadAtStartup = true)
public class TestInputCommand extends BaseShellCommand {

    @Override
    public Object invoke(String[] args) {
        WidgetBuiltData data =  getEngine().getUiManager().buildWidget("inputdialog");

        InputStringDialogWidget prgProgressDialogWidget = (InputStringDialogWidget)data.getWidgetController();

        data.getWidgetPane().setLayoutX(getEngine().getUiManager().getDesktopWidth() / 2 - data.getWidgetPane().getPrefWidth() / 2);
        data.getWidgetPane().setLayoutY(getEngine().getUiManager().getDesktopHeight() / 2 - data.getWidgetPane().getPrefHeight() / 2);

        getEngine().getUiManager().addDesktopChildren(data.getWidgetPane());
        prgProgressDialogWidget.show("// Input dialog", "Enter your name", "", new IInputStringDialogListener() {
            @Override
            public void onOK(String result) {
                log(Level.INFO, "Your name is %s", result);
                data.getWidgetPane().setVisible(false);
            }

            @Override
            public void onNo() {
                data.getWidgetPane().setVisible(false);
            }
        });

        return "OK";
    }
}
