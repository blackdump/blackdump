package com.github.blackdump.base;

import com.github.blackdump.annotations.ABDShellCommand;
import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.managers.IShellManager;
import com.github.blackdump.shell.IShellCommand;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Classe utilizzata per creare i comandi per la shell
 * <p>
 * Estende questa classe per creare i comandi
 */
public class BaseShellCommand implements IShellCommand {

    private Logger mLogger;

    private ABDShellCommand annotation;

    private String commandAliases = "";

    @Getter(AccessLevel.PROTECTED)
    private IBlackdumpEngine engine;

    @Getter(AccessLevel.PROTECTED)
    private IShellManager shellManager;

    public BaseShellCommand() {
        mLogger = Logger.getLogger(getClass());
        annotation = getClass().getAnnotation(ABDShellCommand.class);

        if (annotation == null) {
            log(Level.FATAL, "Error for command %s, annotation is null! Do you remember to set @ABDShellCommand ?");
        }
        parseCommandsAnnotation();
    }

    private void parseCommandsAnnotation() {
        for (String cmd : annotation.commands()) {
            commandAliases += cmd + ",";
        }

        commandAliases = commandAliases.substring(0, commandAliases.length() - 1);
    }

    @Override
    public String getCommands() {
        return commandAliases;
    }

    @Override
    public String getHelp() {
        return annotation.help();
    }

    @Override
    public boolean getIsLoadAtStartup() {
        return annotation.loadAtStartup();
    }

    @Override
    public void setEngine(IBlackdumpEngine engine) {

        this.engine = engine;
    }

    @Override
    public void setShellManager(IShellManager shellManager) {
        this.shellManager = shellManager;
    }

    @Override
    public Object invoke(String[] args) {
        return null;
    }

    protected void log(Level level, String text, Object... args) {
        mLogger.log(level, String.format(text, args));
    }

    protected String transformToRawString(String[] args)
    {
        String out = "";
        for (int i =0;i<args.length;i++)
        {
            out += args[i];
        }

        return out;
    }
}
