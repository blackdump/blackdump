package com.github.blackdump.interfaces.windows;

import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import jfxtras.labs.scene.control.window.Window;

/**
 * Interfaccia per creare le finestre
 */
public interface IBDWindow {
    void setEngine(IBlackdumpEngine engine);

    void setParentWindow(Window parentWindow);

}
