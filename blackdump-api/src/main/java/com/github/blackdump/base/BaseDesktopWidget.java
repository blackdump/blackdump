package com.github.blackdump.base;

import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.windows.IBDDesktopWidget;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Classe base per la crazione dei widgets
 */
public class BaseDesktopWidget implements IBDDesktopWidget{

    private Logger mLogger;

    @Getter(AccessLevel.PROTECTED)
    private AnchorPane parentDesktop;

    @Getter(AccessLevel.PROTECTED)
    private IBlackdumpEngine engine;



    public BaseDesktopWidget()
    {
        mLogger = Logger.getLogger(getClass());
    }

    protected void log(Level level, String text, Object... args) {
        mLogger.log(level, String.format(text, args));
    }

    @Override
    public void setEngine(IBlackdumpEngine engine) {
        this.engine = engine;
    }

    @Override
    public void setDesktop(AnchorPane parent) {

        this.parentDesktop = parent;
        ready();
    }


    protected void ready()
    {

    }
}
