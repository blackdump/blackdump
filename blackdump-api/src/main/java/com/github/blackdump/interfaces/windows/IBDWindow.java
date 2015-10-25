package com.github.blackdump.interfaces.windows;

import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import jfxtras.labs.scene.control.window.Window;

/**
 * Interfaccia per creare le finestre
 */
public interface IBDWindow {

    long getWindowUid();

    void setWindowUid(long uid);

    void setEngine(IBlackdumpEngine engine);

    Window getParentWindow();

    void setParentWindow(Window parentWindow);


}
