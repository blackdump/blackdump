package com.github.blackdump.ui.windows;

import com.github.blackdump.annotations.ABDDesktopWidget;
import com.github.blackdump.annotations.ABDMenuItem;
import com.github.blackdump.annotations.WindowType;
import com.github.blackdump.base.BaseDesktopWidget;
import com.github.blackdump.data.ui.WidgetBuiltData;
import com.github.blackdump.interfaces.windows.IBDWindow;
import com.github.blackdump.interfaces.windows.IWindowListener;
import com.github.blackdump.interfaces.windows.dialogs.IConfirmDialogListener;
import com.github.blackdump.ui.widgets.ConfirmDialogWidget;
import com.github.blackdump.utils.AppInfo;
import com.github.blackdump.utils.FuncsUtility;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Classe per la creazione della menu bar
 */
@ABDDesktopWidget(fxmlFilename = "/windows/menuBar.fxml", loadAfterLogin = true, name = "menubar")
public class MenuBar extends BaseDesktopWidget implements IWindowListener {


    @FXML
    private BorderPane root;

    @FXML
    private Label lblClock;

    @FXML
    private HBox pnlWindowsList;

    @FXML
    private Menu mnuPrograms;

    @FXML
    private MenuItem btnShutdown;

    @Override
    protected void ready() {

        getEngine().getUiManager().addWindowListener(this);

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
                Platform.runLater(() -> lblClock.setText(FuncsUtility.getCurrentDateTime()));
            }
        },1000, 1000 );


        btnShutdown.setOnAction(event -> {
            WidgetBuiltData data = getEngine().getUiManager().buildWidget("confirmdialog");

            ConfirmDialogWidget controller = (ConfirmDialogWidget)data.getWidgetController();

            controller.showConfirm("//" + AppInfo.AppName, "Are you sure to exit?", new IConfirmDialogListener() {
                @Override
                public void onOK() {
                    getEngine().quit(true);
                    controller.removeFromDesktop();
                }

                @Override
                public void onAbort() {

                }
            });

            controller.addToDesktop();
            controller.center();
        });

        FadeTransition ft = new FadeTransition(Duration.millis(1000), root);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();



    }

    @Override
    public void onWindowCreated(IBDWindow window) {

        Button btnWindow = new Button();


        btnWindow.setOnAction(event -> {
            if (window.getParentWindow().isSelected())
                window.getParentWindow().setMinimized(true);
            else
                window.getParentWindow().setMoveToFront(true);

            if (window.getParentWindow().isMinimized())
                window.getParentWindow().setMinimized(false);
        });
        btnWindow.setText(window.getParentWindow().getTitle());
        btnWindow.setPrefHeight(-1);
        btnWindow.setId(Long.toString(window.getWindowUid()));
        pnlWindowsList.getChildren().add(btnWindow);
    }

    @Override
    public void onWindowClosed(IBDWindow window) {

        try {
            Optional<Node> btn = pnlWindowsList.getChildren().stream().filter(s -> s.getId().equals(Long.toString(window.getWindowUid()))).findFirst();

            if (btn.isPresent()) {
                pnlWindowsList.getChildren().remove(btn.get());
            }
        }
        catch (Exception ex)
        {

        }
    }

    @Override
    public void onBuildMenuTree(List<ABDMenuItem> items) {

        for (ABDMenuItem item : items) {
            Menu mToAdd = null;
            String[] pos = item.position().split("->");

            for (int i = 0; i < pos.length; i++) {
                String mt = pos[i];

                if (i == 0) {
                    if (!mnuPrograms.getItems().stream().filter(s -> s.getText().equals(mt)).findFirst().isPresent()) {

                        Menu mnu2 = new Menu();
                        mnu2.setText(mt);
                        mnuPrograms.getItems().add(mnu2);

                        mToAdd = mnu2;
                    }
                } else {
                    if (!mToAdd.getItems().stream().filter(s -> s.getText().equals(mt)).findFirst().isPresent()) {
                        Menu mnu3 = new Menu();
                        mnu3.setText(mt);
                        mToAdd.getItems().add(mnu3);

                        mToAdd = mnu3;
                    } else {
                        mToAdd = (Menu) mToAdd.getItems().stream().filter(s -> s.getText().equals(mt)).findFirst().get();
                    }

                }
            }


            MenuItem mItem = new MenuItem();
            mItem.setText(item.name());

            mItem.setOnAction(event -> {
                if (item.type() == WindowType.WINDOW) {
                    getEngine().getUiManager().createWindow(item.name(), item.fxml(), true, true, true);
                } else {
                    getEngine().getUiManager().createWidget(item.fxml());

                }
            });

            mToAdd.getItems().add(mItem);
        }
    }
}
