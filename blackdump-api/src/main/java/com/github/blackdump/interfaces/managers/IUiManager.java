package com.github.blackdump.interfaces.managers;

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

}
