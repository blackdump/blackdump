package com.github.blackdump.ui.windows;

import com.github.blackdump.annotations.ABDMenuItem;
import com.github.blackdump.annotations.WindowType;
import com.github.blackdump.base.BaseWindow;

/**
 * Finestra per gestire il debug
 */
@ABDMenuItem(name = "Debug", fxml = "/windows/debugWindow.fxml", type = WindowType.WINDOW, position = "Development->Debuggers", isInstalled = true)
public class DebugWindow extends BaseWindow {
}
