package com.github.blackdump.interfaces.windows;

import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * Interfaccia per la creazione dei widget
 */
public interface IBDDesktopWidget {

    void setEngine(IBlackdumpEngine engine);

    void setDesktop(AnchorPane parent);

    void setMyPane(Pane pane);

    void center();

    void addToDesktop();

    void removeFromDesktop();

}
