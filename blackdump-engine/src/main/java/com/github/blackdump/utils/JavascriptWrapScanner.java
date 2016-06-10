package com.github.blackdump.utils;

import com.github.blackdump.annotations.ADBJavascriptWrap;
import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.jsengine.IJsObject;
import lombok.Getter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Set;

public class JavascriptWrapScanner {

    private static Logger mLogger = Logger.getLogger(JavascriptWrapScanner.class);

    @Getter
    private HashMap<String, Object> jsObjects = new HashMap<>();


    private IBlackdumpEngine engine;


    protected static void log(Level level, String text, Object... args) {
        mLogger.log(level, String.format(text, args));
    }


    public JavascriptWrapScanner() {

    }

    private static class JavascriptWrapScannerHolder {
        public static final JavascriptWrapScanner INSTANCE = new JavascriptWrapScanner();
    }

    public static JavascriptWrapScanner getInstance() {
        return JavascriptWrapScannerHolder.INSTANCE;
    }


    public void init(IBlackdumpEngine engine)
    {
        this.engine = engine;
        try
        {
            mLogger.info("Start scanning javascript objects");

            Set<Class<?>> classes = ReflectionUtils.getAnnotation(ADBJavascriptWrap.class);

            log(Level.INFO,"Found %s classes", classes.size());

            for (Class<?> classz : classes)
            {
                log(Level.INFO,"Init JS object: %s", classz.getName());
                ADBJavascriptWrap annotation = classz.getAnnotation(ADBJavascriptWrap.class);
                IJsObject object = (IJsObject)classz.newInstance();
                object.init(engine);
                log(Level.INFO, "JsObject %s -> %s", classz.getName(), annotation.value());

                jsObjects.put(annotation.value(), object);
            }

            log(Level.INFO, "Completed...");
        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during initializing JSObjects: %s", ex.getMessage());

        }

    }
}
