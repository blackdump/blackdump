package com.github.blackdump.shell.commands;

import com.github.blackdump.annotations.ABDShellCommand;
import com.github.blackdump.base.BaseShellCommand;
import com.github.blackdump.data.ui.WidgetBuiltData;
import com.github.blackdump.interfaces.windows.IConfirmDialogListener;
import com.github.blackdump.ui.widgets.ConfirmDialogWidget;
import com.github.blackdump.ui.widgets.ProgressDialogWidget;
import org.apache.log4j.Level;

/**
 * Created by squid on 10/26/15.
 */
@ABDShellCommand(commands = {"testconfirm"}, help = "Test confirm widget", loadAtStartup = true)
public class TestConfirmCommand extends BaseShellCommand {

    @Override
    public Object invoke(String[] args) {
        WidgetBuiltData data =  getEngine().getUiManager().buildWidget("confirmdialog");

        ConfirmDialogWidget prgProgressDialogWidget = (ConfirmDialogWidget)data.getWidgetController();

        data.getWidgetPane().setLayoutX(getEngine().getUiManager().getDesktopWidth() / 2 - data.getWidgetPane().getPrefWidth() / 2);
        data.getWidgetPane().setLayoutY(getEngine().getUiManager().getDesktopHeight() / 2 - data.getWidgetPane().getPrefHeight() / 2);

        getEngine().getUiManager().addDesktopChildren(data.getWidgetPane());
        prgProgressDialogWidget.showConfirm("// Blackdump", "Sicuro che 2+2 fa 4?", new IConfirmDialogListener() {
            @Override
            public void onOK() {
                log(Level.INFO, "OK");
                data.getWidgetPane().setVisible(false);
            }

            @Override
            public void onAbort() {
                log(Level.INFO, "NO");
                data.getWidgetPane().setVisible(false);
            }
        });

        return "OK";
    }
}
