package com.github.blackdump.interfaces.managers;

import com.github.blackdump.interfaces.windows.IWindowListener;

/**
 * Interfaccia per il creatore della UI
 */
public interface IUiManager {

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

}
