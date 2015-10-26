package com.github.blackdump.ui.windows;

import com.github.blackdump.annotations.ABDMenuItem;
import com.github.blackdump.annotations.WindowType;
import com.github.blackdump.base.BaseWindow;

/**
 * Finestra per le messioni
 */
@ABDMenuItem(name = "Missions", fxml = "/windows/missionsWindows.fxml", type = WindowType.WINDOW, position = "System->Main", isInstalled = true)
public class MissionsWindow extends BaseWindow {
}
