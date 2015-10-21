package com.github.blackdump;

import com.github.blackdump.data.BDConfig;
import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.serializer.JsonSerializer;
import com.github.blackdump.ui.UiManager;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Framework del gioco, che estende l'interfaccia @link IBlackdumpEngine
 */
public class BlackdumpEngine implements IBlackdumpEngine {

    private Logger mLogger;

    @Getter
    private String rootDirectory = System.getProperty("user.home") + File.separator + ".blackdump" + File.separator;
    private String configFilename = "blackdump.config";
    private BDConfig mConfig;

    private Thread mGUIThread;


    public BlackdumpEngine() {
        mLogger = Logger.getLogger(getClass());
    }

    @Override
    public void init(String[] args) {
        log(Level.DEBUG, "Initializing Blackdump engine");
        log(Level.DEBUG, "Args count %s", args.length);

        initConfig();
        initGuiThread();
    }

    private void initConfig() {
        try {
            if (!new File(rootDirectory).exists())
                new File(rootDirectory).mkdirs();

            configFilename = rootDirectory + configFilename;
            log(Level.INFO, "Loading %s config", configFilename);

            if (!new File(configFilename).exists()) {
                log(Level.WARN, "Default config not exists, creating default");
                FileUtils.writeStringToFile(new File(configFilename), JsonSerializer.Serialize(new BDConfig()));
            }
            mConfig = JsonSerializer.Deserialize(FileUtils.readFileToString(new File(configFilename)), BDConfig.class);
            log(Level.INFO, "Config is OK");

        } catch (Exception ex) {
            log(Level.ERROR, "Error during loading config %s => %s", configFilename, ex.getMessage());
        }

    }

    private void initGuiThread() {
        mGUIThread = new Thread(() -> UiManager.main(null));
        mGUIThread.start();

    }

    // Funzione utility di logging
    protected void log(Level level, String text, Object... args) {
        mLogger.log(level, String.format(text, args));
    }
}
