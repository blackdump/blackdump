package com.github.blackdump.interfaces.windows;

import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import javafx.scene.layout.AnchorPane;

/**
 * Interfaccia per la creazione dei widget
 */
public interface IBDDesktopWidget {

    void setEngine(IBlackdumpEngine engine);

    void setDesktop(AnchorPane parent);


}
