package com.github.blackdump.data.ui;

import com.github.blackdump.interfaces.windows.IBDDesktopWidget;
import javafx.scene.layout.Pane;
import lombok.Data;

import java.io.Serializable;

/**
 * Per la creazione dei widget e per il passaggio dei dati
 * delle finestre.
 * Soddisfa l'esigenza dei dialoghi
 */
@Data
public class WidgetBuiltData implements Serializable {

    private IBDDesktopWidget widgetController;

    private Pane widgetPane;
}
