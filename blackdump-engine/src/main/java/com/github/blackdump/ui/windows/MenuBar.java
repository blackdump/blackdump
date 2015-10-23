package com.github.blackdump.ui.windows;

import com.github.blackdump.annotations.ABDDesktopWidget;
import com.github.blackdump.base.BaseDesktopWidget;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Classe per la creazione della menu bar
 */
@ABDDesktopWidget(fxmlFilename = "/windows/menuBar.fxml", loadAfterLogin = true)
public class MenuBar extends BaseDesktopWidget {


    @FXML
    private BorderPane root;

    @FXML
    private Label lblClock;

    @FXML
    private HBox pnlWindowsList;


    @Override
    protected void ready() {


        root.setOpacity(0);
        root.setLayoutX(0);
        root.setLayoutY(getParentDesktop().getHeight() - root.getPrefHeight() - 10);
        root.setMinWidth(getParentDesktop().getWidth());
        root.setPrefWidth(getParentDesktop().getWidth());

        pnlWindowsList.getStyleClass().add("mnuDefault-hbox");

        Timer tmrClock = new Timer();
        tmrClock.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> lblClock.setText(new Date().toString()));
            }
        },1000, 1000 );

        FadeTransition ft = new FadeTransition(Duration.millis(1000), root);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();


    }
}
