package com.github.blackdump.base;

import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.managers.IBlackdumpManager;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Classe da estendere per creare i manager
 * implementa la possibilita dei log
 */
public class BaseManager implements IBlackdumpManager{

    private Logger mLogger;

    @Getter(AccessLevel.PROTECTED)
    private IBlackdumpEngine engine;

    public BaseManager()
    {
        mLogger = Logger.getLogger(getClass());
    }

    @Override
    public void start(IBlackdumpEngine engine) {
        this.engine = engine;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void ready() {

    }

    protected <T> T getManager(Class type)
    {
        return engine.getManagerByClass(type);
    }

    protected void log(Level level, String text, Object... args) {
        mLogger.log(level, String.format(text, args));
    }


}
