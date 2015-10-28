package com.github.blackdump.webserver.controllers.base;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Classe base per la gestione del controller
 */
public class BaseController {

    private Logger mLogger;


    /**
     * Defautl logger
     * @param level livello del log
     * @param text
     * @param args
     */
    protected void log(Level level, String text, Object ... args)
    {
        if (mLogger == null) mLogger = Logger.getLogger(getClass());

        mLogger.log(level, String.format(text, args));
    }
}
