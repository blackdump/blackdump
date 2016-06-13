package com.github.blackdump.ui.windows;

import com.github.blackdump.annotations.ABDMenuItem;
import com.github.blackdump.annotations.WindowType;
import com.github.blackdump.base.BaseWindow;
import com.github.blackdump.data.ui.WidgetBuiltData;
import com.github.blackdump.eventbus.EventBusMessages;
import com.github.blackdump.eventbus.ObservableVariablesManager;
import com.github.blackdump.interfaces.windows.dialogs.DialogTypeEnum;
import com.github.blackdump.ui.widgets.MessageDialogWidget;
import com.github.blackdump.utils.AppInfo;
import com.github.blackdump.utils.JavascriptWrapScanner;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

import netscape.javascript.JSObject;
import org.apache.log4j.Level;



@ABDMenuItem(name = "Browser", fxml = "/windows/webWindows.fxml", type = WindowType.WINDOW, position = "Browser->Main", isInstalled = true)
public class WebWindow extends BaseWindow {


    @FXML
    private TextField edtAddress;

    @FXML
    private Button btnGo;

    @FXML
    private Button btnAbort;

    @FXML
    private WebView webBrowser;

    @FXML
    private ProgressBar prgBrowser;

    private WebEngine webEngine;

    @FXML
    private void initialize() {

        try {
            webBrowser.getStyleClass().clear();
            webEngine = webBrowser.getEngine();
            webEngine.setOnAlert(this::wrapAlert);
            webEngine.setUserAgent(AppInfo.AppName);
            webBrowser.setStyle("");
            webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) ->
            {
                if (newValue == Worker.State.SUCCEEDED)
                {
                    edtAddress.setText(webEngine.getLocation());
                }
            });
            prgBrowser.progressProperty().bind(webEngine.getLoadWorker().progressProperty());

            initJavascriptBridge();


            Platform.runLater(() -> {
                webBrowser.getEngine().load(getClass().getResource("/pages/test.html").toExternalForm());
            });

            btnGo.setOnAction(event -> {
                if (!edtAddress.getText().startsWith("http://"))
                    edtAddress.setText("http://" + edtAddress.getText());

                webBrowser.getEngine().load(edtAddress.getText());
            });
        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during creating web browser => %s", ex.getMessage() );
        }
    }

    private void wrapAlert(WebEvent<String> event)
    {
        WidgetBuiltData data =  getEngine().getUiManager().buildWidget("messagedialog");

        MessageDialogWidget prgProgressDialogWidget = (MessageDialogWidget)data.getWidgetController();

       prgProgressDialogWidget.center();

        getEngine().getUiManager().addDesktopChildren(data.getWidgetPane());
        prgProgressDialogWidget.show("// Blackdump",event.getData(), DialogTypeEnum.WARNING, () -> data.getWidgetPane().setVisible(false));
    }

    private void initJavascriptBridge()
    {
//        getEngine().broadcastEvent(EventBusMessages.UI_MANAGER_BROWSER_ADD, webEngine);

        webEngine.setJavaScriptEnabled(true);

        JSObject jsObject = (JSObject)webEngine.executeScript("window");

        JavascriptWrapScanner.getInstance().getJsObjects().forEach((s, o) -> jsObject.setMember(s,o));
    }
}
