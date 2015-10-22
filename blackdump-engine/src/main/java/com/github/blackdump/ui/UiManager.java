package com.github.blackdump.ui;
/**
 * Fa partire la GUI
 */

import com.github.blackdump.annotations.ABDManager;
import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.managers.IBlackdumpManager;
import com.github.blackdump.interfaces.managers.IUiManager;
import com.github.blackdump.interfaces.windows.IBDWindow;
import com.github.blackdump.utils.AppInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import jfxtras.labs.scene.control.window.Window;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@ABDManager
public class UiManager extends Application implements IBlackdumpManager, IUiManager {

    public static int NO_MINIZE = 1;
    public static int NO_CLOSE = 2;
    private static IBlackdumpEngine engine;
    private static Logger mLogger = Logger.getLogger(UiManager.class);
    private static Group root;
    private static Stage primaryStage;
    private Thread mGuiThread;
    private List<IBDWindow> mActiveWindows = new ArrayList<>();

    protected static void log(Level level, String text, Object... args) {
        mLogger.log(level, String.format(text, args));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        init(primaryStage);
        primaryStage.show();
    }

    private void init(Stage primaryStage) {

         engine.getConfig();
         root =  new Group();

        primaryStage.setResizable(true);
        primaryStage.setFullScreen(true);
        primaryStage.setScene(new Scene(root, 600, 500));

        primaryStage.getScene().getStylesheets().add("/css/default.css");
        primaryStage.setTitle(AppInfo.AppName + " v" + AppInfo.AppVersion);


        root.setStyle("-fx-background-color: black");

        createWindow("Login", "/windows/loginWindow.fxml", false, false, false);


    }

    @Override
    public void createWindow(String title, String fxml, boolean minizeButton, boolean closeButton, boolean center)
    {
        try
        {
            Window window = new Window(title);
            window.setTitleBarStyleClass("default-window-titlebar");


            if (!center) {
                window.setLayoutX(10);
                window.setLayoutY(10);
            }
            else
            {
                ;
              //  window.setLayoutX(primaryStage.getX() + primaryStage.getWidth() / 2 - primaryStage.getWidth() / 2);
              //  window.setLayoutY(primaryStage.getY() + primaryStage.getHeight() / 2 - primaryStage.getHeight() / 2);
            }
            window.setPrefSize(300, 200);


            if (closeButton)
                window.getLeftIcons().add(new CloseIcon(window));

            if (minizeButton)
                window.getRightIcons().add(new MinimizeIcon(window));


            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));

                Pane pnl = fxmlLoader.load();
                window.setContentPane(pnl);
                IBDWindow controller = fxmlLoader.<IBDWindow>getController();

                if (controller != null) {
                    controller.setEngine(engine);
                    controller.setParentWindow(window);
                }

                root.getChildren().add(window);

            }
            catch (Exception ex)
            {
                log(Level.FATAL, "Error during creation of window %s => %s", title, ex.getMessage());
            }

        }
        catch (Exception ex)
        {

        }
    }

    @Override
    public void start(IBlackdumpEngine engine) {
        this.engine = engine;

    }

    @Override
    public void dispose() {

    }

    @Override
    public void ready() {
        mGuiThread = new Thread(() -> launch(new String[] {}));
        mGuiThread.start();
    }

}
