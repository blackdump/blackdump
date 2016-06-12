package com.github.blackdump.ui.widgets;

import com.github.blackdump.annotations.ABDDesktopWidget;
import com.github.blackdump.base.BaseDesktopWidget;
import com.github.blackdump.eventbus.EventBusMessages;
import com.github.blackdump.persistence.users.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


@ABDDesktopWidget(fxmlFilename = "/windows/hudWidget.fxml", loadAfterLogin = true, name = "hudwidget")
public class HudWidget extends BaseDesktopWidget {


    @FXML
    private HBox pnlMain;

    @FXML
    private Label lblCredits;




    @Override
    protected void ready() {
        super.ready();
        pnlMain.setPrefWidth(480);
        pnlMain.setPrefHeight(60);

        getEngine().subscribeEvent(EventBusMessages.PLAYER_PROPERTY_CHANGE, this::onPlayerPropertyChange);
        getEngine().subscribeEvent(EventBusMessages.CURRENT_USER_OBJECT_SEND, this::onCurrentPlayerBroadcast);

    }

    private void onPlayerPropertyChange(Object data)
    {


    }

    private void onCurrentPlayerBroadcast(Object data)
    {
        User user = (User)data;

        Platform.runLater(() -> {
            lblCredits.setText(String.format("%s $", user.getUserDetails().getCurrentMoney()));
        });
    }
}
