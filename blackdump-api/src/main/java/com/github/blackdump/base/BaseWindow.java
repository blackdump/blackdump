package com.github.blackdump.base;

import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.windows.IBDWindow;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Classe che implementa l'intefaccia IBDWindow
 * Utilizzare questa classe per creare le finestre
 */
public class BaseWindow implements IBDWindow {

    private Logger mLogger;

    @Getter(AccessLevel.PROTECTED)
    private IBlackdumpEngine engine;

    public BaseWindow()
    {
        mLogger = Logger.getLogger(getClass());
    }

    @Override
    public void setEngine(IBlackdumpEngine engine) {
        this.engine = engine;
    }

    protected void log(Level level, String text, Object... args) {
        mLogger.log(level, String.format(text, args));
    }
}
