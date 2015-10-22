package com.github.blackdump.shell;

import com.github.blackdump.annotations.ABDManager;
import com.github.blackdump.annotations.ABDShellCommand;
import com.github.blackdump.base.BaseManager;
import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.managers.IShellManager;
import com.github.blackdump.utils.ReflectionUtils;
import org.apache.log4j.Level;

import java.util.HashMap;
import java.util.Set;

/**
 * Parser della shell
 */
@ABDManager
public class ShellManager extends BaseManager implements IShellManager {


    private HashMap<String, IShellCommand> mAvailableCommands = new HashMap<>();
    private HashMap<String, IShellCommand> mInstalledCommands = new HashMap<>();


    @Override
    public void start(IBlackdumpEngine engine) {
        super.start(engine);

        log(Level.INFO, "Loading shell commands...");
        scanShellCommands();
    }

    private void scanShellCommands() {
        log(Level.INFO, "Scanning for commands");

        Set<Class<?>> classes = ReflectionUtils.getAnnotation(ABDShellCommand.class);

        log(Level.INFO, "Found %s commands", classes.size());

        for (Class<?> classz : classes) {
            addCommand(classz);
        }

    }

    public void addCommand(Class<?> classCommand) {
        try {
            IShellCommand cmd = (IShellCommand) classCommand.newInstance();
            cmd.setEngine(getEngine());
            mAvailableCommands.put(cmd.getCommands(), cmd);

            if (cmd.getIsLoadAtStartup()) {
                log(Level.INFO, "Add cmd: %s to installed commands", cmd.getCommands());
                mInstalledCommands.put(cmd.getCommands(), cmd);
            }

        } catch (Exception ex) {
            log(Level.FATAL, "Error during add command %s => %s", classCommand.getClass().getSimpleName(), ex.getMessage());
        }
    }

    @Override
    public void parse(String input) {
        String cmd = input.split(" ")[0].trim();
        String args = input.replace(cmd, "");

        try {
            IShellCommand commandExecuter = null;

            for (String cmdsAv : mAvailableCommands.keySet()) {
                for (int i = 0; i < cmdsAv.split(",").length; i++) {
                    if (cmd.equals(cmdsAv.split(",")[i])) {
                        commandExecuter = mAvailableCommands.get(cmdsAv);
                        break;
                    }
                }
            }

            if (commandExecuter != null) {
                log(Level.INFO, "Execute command %s => args %s", commandExecuter.getClass().getSimpleName(), args);

                String result = (String) commandExecuter.invoke(args.split(" "));

                log(Level.INFO, "Result => %s", result);
            } else {
                log(Level.INFO, "Command not found => %s", cmd);
            }


        } catch (Exception ex) {
            log(Level.FATAL, "Error during parse %s => %s", input, ex.getMessage());
        }
    }
}
