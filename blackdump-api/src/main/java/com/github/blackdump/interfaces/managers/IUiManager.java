package com.github.blackdump.interfaces.managers;

import com.github.blackdump.data.ui.WidgetBuiltData;
import com.github.blackdump.interfaces.windows.IWindowListener;
import javafx.scene.Node;

/**
 * Interfaccia per il creatore della UI
 */
public interface IUiManager {

    double getDesktopWidth();

    double getDesktopHeight();

    /**
     * Crea una nuova finestra caricato il file fxml
     *
     * @param title
     * @param fxml
     * @param minizeButton
     * @param closeButton
     * @param center
     */
    void createWindow(String title, String fxml, boolean minizeButton, boolean closeButton, boolean center);


    /**
     * Crea un nuovo widget
     * @param fxml
     */
    void createWidget(String fxml);


    void notifyAfterLogin();

    void addWindowListener(IWindowListener listener);

    WidgetBuiltData buildWidget(String name);

    void addDesktopChildren(Node object);

    void removeDesktopChildren(Node node);

}
