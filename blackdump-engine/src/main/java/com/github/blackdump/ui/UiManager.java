package com.github.blackdump.ui;
/**
 * Fa partire la GUI
 */

import com.github.blackdump.annotations.ABDDesktopWidget;
import com.github.blackdump.annotations.ABDManager;
import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.managers.IBlackdumpManager;
import com.github.blackdump.interfaces.managers.IUiManager;
import com.github.blackdump.interfaces.windows.IBDDesktopWidget;
import com.github.blackdump.interfaces.windows.IBDWindow;
import com.github.blackdump.ui.windows.less.at.bestsolution.efxclipse.less.LessCSSLoader;
import com.github.blackdump.utils.AppInfo;
import com.github.blackdump.utils.FuncsUtility;
import com.github.blackdump.utils.ReflectionUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import jfxtras.labs.scene.control.window.Window;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ABDManager
public class UiManager extends Application implements IBlackdumpManager, IUiManager {


    private static IBlackdumpEngine engine;
    private static Logger mLogger = Logger.getLogger(UiManager.class);
    private static AnchorPane root;
    private static Stage primaryStage;
    private Thread mGuiThread;

    private static String mThemesDirectory;
    private static String mCssThemesDirectory;
    private static String mLessThemeDirectory;

    private List<IBDWindow> mActiveWindows = new ArrayList<>();

    private LessCSSLoader mLessCompiler;

    private static List<String> mAvaiableThemes= new ArrayList<>();


    private static List<ABDDesktopWidget> mWidgets = new ArrayList<>();

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




        try {
            root = new AnchorPane();

            scanForWidgets();

            primaryStage.setResizable(true);
            primaryStage.setFullScreen(true);
            primaryStage.setScene(new Scene(root, 600, 500));

            File mTheme = new File(mCssThemesDirectory + engine.getConfig().getDefaultTheme());
            primaryStage.getScene().getStylesheets().add("file:///" + mTheme.getAbsolutePath().replace("\\", "/"));
            primaryStage.setTitle(AppInfo.AppName + " v" + AppInfo.AppVersion);
            primaryStage.setOnCloseRequest(event -> engine.quit(false));


            root.getStyleClass().add("desktopPane");

            createWindow("Login", "/windows/loginWindow.fxml", false, false, false);

        }
        catch (Exception ex)
        {

        }

    }

    @Override
    public void notifyAfterLogin() {
        createAfterLoginWidgets();
    }

    private void createAfterLoginWidgets()
    {
        for (ABDDesktopWidget annotation : mWidgets.stream().filter( s -> s.loadAfterLogin() == true).collect(Collectors.toList()))
        {
            createWidget(annotation.fxmlFilename());

        }
    }

    private void scanForWidgets()
    {
        try
        {
            log(Level.INFO, "Scanning for widgets");
            Set<Class<?>> classes = ReflectionUtils.getAnnotation(ABDDesktopWidget.class);
            log(Level.INFO, "Found %s desktop widgets", classes.size());

            for (Class<?> classz : classes )
            {
                ABDDesktopWidget annotation = classz.getAnnotation(ABDDesktopWidget.class);

                log(Level.INFO, "Adding class %s to widgets", classz.getSimpleName());

                mWidgets.add(annotation);

            }



        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during scanning widgets...");
        }

    }

    public void createWidget(String fxml)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));

            Pane pane = fxmlLoader.load();

            IBDDesktopWidget controller = fxmlLoader.<IBDDesktopWidget>getController();

            if (controller != null)
            {
                controller.setEngine(engine);
                controller.setDesktop(root);

            }

            root.getChildren().add(pane);

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during creation of widget filename: %s => %s", fxml, ex.getMessage());
        }

    }

    @Override
    public void createWindow(String title, String fxml, boolean minizeButton, boolean closeButton, boolean center)
    {
        try
        {
            Window window = new Window(title);
            window.setTitleBarStyleClass("default-window-titlebar");
            window.setOpacity(0.9);


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
                window.getRightIcons().add(new CloseIcon(window));

            if (minizeButton)
                window.getLeftIcons().add(new MinimizeIcon(window));


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

        initLessCompiler();

        mGuiThread = new Thread(() -> launch(new String[] {}));
        mGuiThread.start();
    }

    private void initLessCompiler()
    {

        try {

            mThemesDirectory = engine.getRootDirectory() + "themes" + File.separator;
            mCssThemesDirectory = mThemesDirectory + "css" + File.separator;
            mLessThemeDirectory = mThemesDirectory + "less" + File.separator;

            log(Level.INFO, "Creating LESS Manager");

            mLessCompiler = new LessCSSLoader();

            log(Level.INFO, "Creating LESS Manager OK");

            if (!new File(mThemesDirectory).exists())
            {
                log(Level.INFO, "Creating default themes directory in %s", mThemesDirectory);
                new File(mThemesDirectory).mkdirs();
            }

            if (!new File(mCssThemesDirectory).exists())
            {
                log(Level.INFO, "Creating default css themes directory in %s", mCssThemesDirectory);
                new File(mCssThemesDirectory).mkdirs();
            }

            if (!new File(mLessThemeDirectory).exists())
            {
                log(Level.INFO, "Creating default less themes directory in %s", mLessThemeDirectory);
                new File(mLessThemeDirectory).mkdirs();
            }

            log(Level.INFO, "Environment is ok");


            scanAndCompileLess();
            checkInternalFonts();

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during initializing LESS compiler");
        }
    }

    private void scanAndCompileLess()
    {
        try
        {
            log(Level.INFO, "Scanning for LESS themes {internal} to compile");

            Set<String> files = new Reflections(ClasspathHelper.forClass(getClass()), new ResourcesScanner()).getResources(Pattern.compile(".*\\.less"));

            for (String file : files)
            {
                log(Level.INFO, "Compiling %s LESS ....", file);
                String destFile = file.split("/")[file.split("/").length -1].replace(".less", ".css");

                URL compiledCss =  mLessCompiler.loadLess(getClass().getResource("/"+file));
                log(Level.INFO, "Compiled %s LESS!", file);

                String css = FileUtils.readFileToString(new File(compiledCss.toURI()));

                FileUtils.writeStringToFile(new File(mCssThemesDirectory + destFile), css );

                log(Level.INFO, "Coping to %s", mCssThemesDirectory + destFile);

                mAvaiableThemes.add(destFile);

            }

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during compiling files");
        }
    }

    private void checkInternalFonts()
    {
        try
        {
            log(Level.INFO, "Scanning for FONTS themes {internal} to compile");

            Set<String> files = new Reflections(ClasspathHelper.forClass(getClass()), new ResourcesScanner()).getResources(Pattern.compile(".*\\.ttf"));

            if (!new File(mCssThemesDirectory + "fonts").exists())
            {
                new File(mCssThemesDirectory + "fonts").mkdirs();
                log(Level.INFO, "Creating fonts directory and copy internal fonts");

                for(String file : files)
                {
                    String destFile = file.split("/")[file.split("/").length -1];

                    FileUtils.copyInputStreamToFile(getClass().getResource("/" + file).openStream(), new File(mCssThemesDirectory + "fonts" + File.separator + destFile));


                    log(Level.INFO, "File %s copied", destFile);
                }

            }


        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during check internal fonts => %s ", ex.getMessage());
        }
    }

}
