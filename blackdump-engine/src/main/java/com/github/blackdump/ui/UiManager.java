package com.github.blackdump.ui;
/**
 * Fa partire la GUI
 */

import com.github.blackdump.utils.AppInfo;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import jfxtras.labs.scene.control.window.Window;

public class UiManager extends Application {

    private static int counter = 1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }

    private void init(Stage primaryStage) {
        final Group root = new Group();

        Button button = new Button("Add more windows");

        root.getChildren().addAll(button);
        primaryStage.setResizable(true);
        primaryStage.setFullScreen(true);
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.getScene().getStylesheets().add("/css/default.css");
        primaryStage.setTitle(AppInfo.AppName + " v" + AppInfo.AppVersion);

        button.setOnAction(arg0 -> {
            Window w = new Window("My Window#" + counter);
            w.setLayoutX(10);
            w.setLayoutY(10);
            w.setPrefSize(300, 200);
            w.getLeftIcons().add(new CloseIcon(w));
            w.getRightIcons().add(new MinimizeIcon(w));

            w.getContentPane().getChildren().add(new Label("Content... \nof the window#" + counter++));
            root.getChildren().add(w);
        });
    }

    public double getSampleWidth() {
        return 600;
    }

    public double getSampleHeight() {
        return 500;
    }

}
