package com.github.blackdump.ui;
/**
 * Fa partire la GUI
 */

import com.asual.lesscss.LessEngine;
import com.github.blackdump.annotations.ABDDesktopWidget;
import com.github.blackdump.annotations.ABDManager;
import com.github.blackdump.annotations.ABDMenuItem;
import com.github.blackdump.annotations.ABDPopmenuEntry;
import com.github.blackdump.data.ui.WidgetBuiltData;
import com.github.blackdump.eventbus.EventBusMessages;
import com.github.blackdump.eventbus.ObservableVariablesManager;
import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.managers.IBlackdumpManager;
import com.github.blackdump.interfaces.managers.IUiManager;
import com.github.blackdump.interfaces.windows.IBDDesktopWidget;
import com.github.blackdump.interfaces.windows.IBDWindow;
import com.github.blackdump.interfaces.windows.IWindowListener;
import com.github.blackdump.utils.AppInfo;
import com.github.blackdump.utils.ReflectionUtils;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import jfxtras.labs.scene.control.window.Window;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.controlsfx.control.Notifications;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ABDManager
public class UiManager extends Application implements IBlackdumpManager, IUiManager {


    private static IBlackdumpEngine engine;
    private static Logger mLogger = Logger.getLogger(UiManager.class);
    private static AnchorPane root;
    private static Stage primaryStage;
    private static String mThemesDirectory;
    private static String mCssThemesDirectory;
    private static String mLessThemeDirectory;
    private static ContextMenu mPopupMenu;
    private static List<String> mAvaiableThemes= new ArrayList<>();
    private static List<ABDDesktopWidget> mWidgets = new ArrayList<>();
    private static List<ABDMenuItem> mMenuItems = new ArrayList<>();
    private Thread mGuiThread;
    private List<IBDWindow> mActiveWindows = new ArrayList<>();
    private List<IWindowListener> mListeners = new ArrayList<>();
    private LessEngine mLessCompiler;

    @Getter
    private List<WebEngine> browserList;

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

            root.setOnContextMenuRequested(event1 -> {

                mPopupMenu.show(root, event1.getX(), event1.getY());


            });
            scanForWidgets();
            scanForMenuItems();
            scanForPopupItems();

            primaryStage.setResizable(true);
            primaryStage.setFullScreen(true);

            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

            primaryStage.setScene(new Scene(root, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight()));

            File mTheme = new File(mCssThemesDirectory + engine.getConfig().getDefaultTheme());
            primaryStage.getScene().getStylesheets().add("file:///" + mTheme.getAbsolutePath().replace("\\", "/"));
            primaryStage.setTitle(AppInfo.AppName + " v" + AppInfo.AppVersion);
            primaryStage.setOnCloseRequest(event -> engine.quit(true));


            root.getStyleClass().add("desktopPane");
            String backgroundStr = String.format("-fx-background-image: url('%s')", new File(mThemesDirectory + "backgrounds" + File.separator + engine.getConfig().getDefaultBackground()).toURI());

            root.setStyle(backgroundStr);

            ObservableVariablesManager.subscribe(EventBusMessages.NOTIFICATION_SHOW_MESSAGE, this::showNotification);
            ObservableVariablesManager.subscribe(EventBusMessages.UI_MANAGER_BROWSER_ADD, this::onAddBrowser);
            ObservableVariablesManager.subscribe(EventBusMessages.UI_MANAGER_BROWSER_REMOVE, this::onCloseBrowser);

            createWindow("Login", "/windows/loginWindow.fxml", false, false, true);

        }
        catch (Exception ex)
        {

        }

    }

    @Override
    public double getDesktopHeight() {
        return root.getHeight();
    }

    @Override
    public double getDesktopWidth() {
        return root.getWidth();
    }

    private void showNotification(Object o) {
        Platform.runLater(() -> {
            Notifications.create().title(AppInfo.AppName).text((String) o).showInformation();
        });

    }

    /**
     * Viene invocata al momento della creazione di un browser
     * @param o
     */
    private void onAddBrowser(Object o)
    {
        browserList.add((WebEngine)o);

    }

    /**
     * Viene invocata al momento della chiusura
     * @param o
     */
    private void onCloseBrowser(Object o)
    {
        browserList.remove((WebEngine)o);

    }

    private void scanForPopupItems() {
        try {
            mPopupMenu = new ContextMenu();

            log(Level.INFO, "Start scanning Popupmenu annotation");

            Set<Class<?>> classes = ReflectionUtils.getAnnotation(ABDPopmenuEntry.class);

            log(Level.INFO, "Found %s Popupmenu annotation", classes.size());

            List<ABDPopmenuEntry> popmenuEntries = new ArrayList<>();

            for (Class<?> classz : classes) {
                ABDPopmenuEntry annotation = classz.getAnnotation(ABDPopmenuEntry.class);

                log(Level.INFO, "Adding %s to popupMenu", classz.getSimpleName());

                popmenuEntries.add(annotation);

            }

            popmenuEntries = popmenuEntries.stream().sorted((o1, o2) -> Integer.compare(o1.order(), o2.order())).collect(Collectors.toList());

            for (ABDPopmenuEntry menuItem : popmenuEntries) {
                MenuItem mi = new MenuItem(menuItem.text());
                mi.setOnAction(event -> createWindow(menuItem.text(), menuItem.fxml(), true, true, true));

                mPopupMenu.getItems().add(mi);
            }

            log(Level.INFO, "Popup menu build");


        } catch (Exception ex) {
            log(Level.FATAL, "Error during scanning popupMenu annotation => %s", ex.getMessage());
        }
    }

    @Override
    public void addWindowListener(IWindowListener listener) {

        mListeners.add(listener);
    }

    private void notifyAddWindowListeners(IBDWindow window) {
        for (int i = 0; i < mListeners.size(); i++) {
            mListeners.get(i).onWindowCreated(window);
        }
    }

    private void notifyCloseWindowListeners(IBDWindow window) {
        for (int i = 0; i < mListeners.size(); i++) {
            mListeners.get(i).onWindowClosed(window);
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

        List<ABDMenuItem> afterLoginMenuItems = mMenuItems.stream().filter(s -> s.isInstalled()).collect(Collectors.toList());

        for (IWindowListener listener : mListeners) {
            listener.onBuildMenuTree(afterLoginMenuItems);
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
            log(Level.FATAL, "Error during scanning widgets => %s", ex.getMessage());
        }

    }

    private void scanForMenuItems() {
        try {
            log(Level.INFO, "Scanning for menuItems");
            Set<Class<?>> classes = ReflectionUtils.getAnnotation(ABDMenuItem.class);
            log(Level.INFO, "Found %s desktop menuItems", classes.size());

            for (Class<?> classz : classes) {
                ABDMenuItem annotation = classz.getAnnotation(ABDMenuItem.class);

                mMenuItems.add(annotation);
            }

            log(Level.INFO, "Building menuItems completed", classes.size());

        } catch (Exception ex) {
            log(Level.FATAL, "Error during scanning MenuItems => %s", ex.getMessage());
        }
    }

    @Override
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
    public WidgetBuiltData buildWidget(String name)
    {

        WidgetBuiltData data = new WidgetBuiltData();
        try
        {
            ABDDesktopWidget widget = mWidgets.stream().filter(s -> s.name().toLowerCase().equals(name.toLowerCase())).findFirst().get();

            if (widget != null)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(widget.fxmlFilename()));

                Pane pane = fxmlLoader.load();

                IBDDesktopWidget controller = fxmlLoader.<IBDDesktopWidget>getController();

                if (controller != null)
                {
                    controller.setEngine(engine);
                    controller.setDesktop(root);

                }

                data.setWidgetController(controller);
                data.setWidgetPane(pane);
                data.getWidgetController().setMyPane(pane);
            }

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during build widget name %s => %s", name, ex.getMessage());
        }

        return data;
    }

    @Override
    public void addDesktopChildren(Node object)
    {
        root.getChildren().add(object);
    }

    @Override
    public void removeDesktopChildren(Node node) {

        root.getChildren().remove(node);
    }


    @Override
    public void createWindow(String title, String fxml, boolean minizeButton, boolean closeButton, boolean center)
    {
        try
        {
            Window window = new Window(title);
            window.getStyleClass().add("default-window");
            window.setTitleBarStyleClass("default-window-titlebar");
            window.setOpacity(0.9);


            if (!center) {

                window.setLayoutX(10);
                window.setLayoutY(10);
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

                if (center) {
                    window.setLayoutX(root.getWidth() / 2 - pnl.getPrefWidth() / 2);
                    window.setLayoutY(root.getHeight() / 2 - pnl.getPrefHeight() / 2);
                }
                window.setContentPane(pnl);
                IBDWindow controller = fxmlLoader.<IBDWindow>getController();

                if (controller != null) {
                    controller.setEngine(engine);
                    controller.setParentWindow(window);
                    controller.setWindowUid(new Random().nextInt());

                    notifyAddWindowListeners(controller);
                }

                window.setOnCloseAction(event -> {

                    notifyCloseWindowListeners(controller);
                });

                root.getChildren().add(window);


            }
            catch (Exception ex)
            {
                log(Level.FATAL, "Error during creation of window %s => %s", title, ex.getMessage());
            }

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during creation of window %s => %s", title, ex.getMessage());
        }
    }

    @Override
    public void start(IBlackdumpEngine engine) {
        this.engine = engine;

    }

    @Override
    public void dispose() {
        Platform.exit();
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

            mLessCompiler = new LessEngine();

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
            checkInternalFontsAndBackground();

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
                try {
                    log(Level.INFO, "Compiling %s LESS ....", file);
                    String destFile = file.split("/")[file.split("/").length - 1].replace(".less", ".css");

                    String compiledCss = mLessCompiler.compile(Resources.toString(getClass().getResource("/" + file), Charsets.UTF_8));
                    log(Level.INFO, "Compiled %s LESS!", file);


                    FileUtils.writeStringToFile(new File(mCssThemesDirectory + destFile), compiledCss);

                    log(Level.INFO, "Coping to %s", mCssThemesDirectory + destFile);

                    mAvaiableThemes.add(destFile);
                } catch (Exception ex) {
                    log(Level.FATAL, "Error during compile %s => %s", file, ex.getMessage());
                }
            }

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during compiling files");
        }
    }

    private void checkInternalFontsAndBackground()
    {
        try
        {
            log(Level.INFO, "Scanning for FONTS themes {internal} to compile");

            Set<String> files = new Reflections(ClasspathHelper.forClass(getClass()), new ResourcesScanner()).getResources(Pattern.compile(".*\\.ttf"));

            if (!new File(mCssThemesDirectory + "fonts").exists())
            {
                new File(mCssThemesDirectory + "fonts").mkdirs();
                log(Level.INFO, "Creating fonts directory and copy internal fonts");



            }

            for(String file : files)
            {
                String destFile = file.split("/")[file.split("/").length -1];

                if (!new File(mCssThemesDirectory + "fonts" + File.separator + destFile).exists()) {

                    FileUtils.copyInputStreamToFile(getClass().getResource("/" + file).openStream(), new File(mCssThemesDirectory + "fonts" + File.separator + destFile));


                    log(Level.INFO, "File %s copied", destFile);
                }
            }

            if (!new File(mThemesDirectory + File.separator + "backgrounds").exists()) {
                log(Level.INFO, "Creating backgrounds directory and copy internal backgrounds");
                new File(mThemesDirectory + File.separator + "backgrounds").mkdirs();

                files = new Reflections(ClasspathHelper.forClass(getClass()), new ResourcesScanner()).getResources(Pattern.compile(".*\\.jpg"));

                for (String file : files) {
                    String destFile = file.split("/")[file.split("/").length - 1];

                    FileUtils.copyInputStreamToFile(getClass().getResource("/" + file).openStream(), new File(mThemesDirectory + "backgrounds" + File.separator + destFile));


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
