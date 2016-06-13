package com.github.blackdump.ui.widgets;

import com.github.blackdump.annotations.ABDDesktopWidget;
import com.github.blackdump.base.BaseDesktopWidget;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

@ABDDesktopWidget(fxmlFilename = "/windows/timerWidget.fxml", loadAfterLogin = false, name = "timerwidget")
public class TimerWidget extends BaseDesktopWidget {


    @FXML
    private Label lblTimer;

    @FXML
    private Label lblTimerInfo;

    @Override
    protected void ready() {
        super.ready();

    }
}
