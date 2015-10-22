package com.github.blackdump;

import com.github.blackdump.annotations.ABDManager;
import com.github.blackdump.data.BDConfig;
import com.github.blackdump.eventsqueue.ObservableVariablesManager;
import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.managers.IBlackdumpManager;
import com.github.blackdump.interfaces.managers.IShellManager;
import com.github.blackdump.interfaces.managers.IUiManager;
import com.github.blackdump.interfaces.persistence.IBDDatabaseManager;
import com.github.blackdump.serializer.JsonSerializer;
import com.github.blackdump.session.SessionManager;
import com.github.blackdump.utils.ReflectionUtils;
import com.google.common.base.Strings;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.context.DefaultContextLoader;
import rx.functions.Action1;

import java.io.File;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Framework del gioco, che estende l'interfaccia @link IBlackdumpEngine
 */
public class BlackdumpEngine implements IBlackdumpEngine {

    private Logger mLogger;

    @Getter
    private String rootDirectory = System.getProperty("user.home") + File.separator + ".blackdump" + File.separator;
    private String modsDirectory;
    private String configFilename = "blackdump.config";


    private List<IBlackdumpManager> mManagers = Collections.unmodifiableList(new ArrayList<>());

    private BDConfig mConfig;

    private JarClassLoader mClassLoader;


    @Getter
    private IBDDatabaseManager databaseManager;

    @Getter
    private IUiManager uiManager;

    @Getter
    private IShellManager shellManager;

    public BlackdumpEngine() {
        mLogger = Logger.getLogger(getClass());
    }

    @Override
    public void init(String[] args) {
        log(Level.DEBUG, "Initializing Blackdump engine");
        log(Level.DEBUG, "Args count %s", args.length);

        SessionManager.setEngine(this);

        initConfig();
        initClassLoader();
        loadMods();
        scanForManagers();

        

    }

    /**
     * Carica la configurazione
     */
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

    private void saveConfig()
    {
        try
        {
            FileUtils.writeStringToFile(new File(configFilename), JsonSerializer.Serialize(mConfig));
        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during save config => %s", ex.getMessage());
        }
    }

    /**
     * Inizializza il class loader per caricare i mods
     */
    private void initClassLoader()
    {
        try
        {
            mClassLoader = new JarClassLoader();
            DefaultContextLoader context = new DefaultContextLoader(mClassLoader);
            context.loadContext();
        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during init Dynamic Class loader => %s", ex.getMessage());
        }
    }

    private void loadMods()
    {
        try
        {
            if (Strings.isNullOrEmpty(mConfig.getModsDirectory()))
            {
                log(Level.DEBUG, "Creating mod directory");
                //Imposta la directory mod se e' vuota
                mConfig.setModsDirectory(rootDirectory + "mods" + File.separator);
                saveConfig();
                new File(mConfig.getModsDirectory()).mkdirs();
            }

            modsDirectory = mConfig.getModsDirectory();

            // I mod hanno estensione .bdmod e verranno caricati dinamicamente attraverso il JCL
            Collection<File> mods = FileUtils.listFiles(new File(modsDirectory), new String[] {"bdmod"},true);

            log(Level.INFO, "Found %s mods to load", mods.size());

            for(File f : mods)
            {
                if (f.getName().startsWith("_"))
                {
                    log(Level.INFO, "Mod %s is disabled ", f.getName());
                }
                else
                {
                    loadMod(f.getAbsolutePath());
                }
            }

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during load mods => %s", ex.getMessage());
        }

    }

    private void loadMod(String filename)
    {
        log(Level.INFO, "Adding to dynamic class path => filename %s", filename);

        try {
            mClassLoader.add(filename);
        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during load mod %s => %s", filename, ex.getMessage());
        }
    }

    private void scanForManagers()
    {
        try
        {
            Set<Class<?>> classes = ReflectionUtils.getAnnotation(ABDManager.class);

            log(Level.INFO, "Found %s managers to load", classes.size());

            for (Class<?> classz : classes)
            {
                log(Level.DEBUG, "Try to load %s class", classz.getSimpleName());

                IBlackdumpManager manager = (IBlackdumpManager)classz.newInstance();

                log(Level.DEBUG, "Class %s is OK!", classz.getSimpleName());

                manager.start(this);

                addManager(manager);
            }

            log(Level.INFO, "Manages loaded!");

            log(Level.DEBUG, "Setting database manager");

            databaseManager = getManagerByClass(IBDDatabaseManager.class);

            if (databaseManager == null)
            {
                log(Level.FATAL, "FATAL ERROR! DatabaseManager is null!");
                quit(true);
            }

            uiManager = getManagerByClass(IUiManager.class);

            if (uiManager == null) {
                log(Level.FATAL, "FATAL ERROR! UiManager is null!");
                quit(true);
            }

            shellManager = getManagerByClass(IShellManager.class);

            if (shellManager == null) {
                log(Level.FATAL, "FATAL ERROR! ShellManager is null!");
                quit(true);
            }

            broadcastManagerReady();

        }
        catch (Exception ex)
        {
            log(Level.ERROR, "Error during scanning manager annotation => %s", ex.getMessage());
        }
    }

    @Override
    public void addManager(IBlackdumpManager manager)
    {
        List<IBlackdumpManager> tmp = new ArrayList<>(mManagers);

        tmp.add(manager);

        mManagers = Collections.unmodifiableList(tmp);
    }

    private void broadcastManagerReady()
    {
        for (IBlackdumpManager manager : mManagers)
        {
            manager.ready();
        }
    }

    @Override
    public <T> T getManagerByClass(Type type) {

        IBlackdumpManager manager = null;

        for (IBlackdumpManager m : mManagers)
        {
           for(Class<?> classz : m.getClass().getInterfaces())
           {
               if (classz.equals(type))
                   manager = m;
           }
        }

        return (T)manager;
    }

    @Override
    public void subscribeEvent(String varname, Action1<? super Object> onNext) {

        ObservableVariablesManager.subscribe(varname, onNext);
    }

    @Override
    public void broadcastEvent(String varname, Object value) {
        ObservableVariablesManager.updateVariable(varname, value);
    }

    @Override
    public void quit(boolean force) {
        log(Level.WARN, "Request to exit!");

        if (force) {
            log(Level.WARN, "Force quit");
            System.exit(1);
        }

        for (IBlackdumpManager manager : mManagers)
        {
            manager.dispose();
        }
    }

    @Override
    public BDConfig getConfig()
    {
        return mConfig;
    }



    // Funzione utility di logging
    protected void log(Level level, String text, Object... args) {
        mLogger.log(level, String.format(text, args));
    }
}
