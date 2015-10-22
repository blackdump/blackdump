package com.github.blackdump.shell;

import com.github.blackdump.annotations.ABDManager;
import com.github.blackdump.annotations.ABDShellCommand;
import com.github.blackdump.base.BaseManager;
import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.managers.IShellManager;
import com.github.blackdump.interfaces.shell.IShellCommandResult;
import com.github.blackdump.utils.ReflectionUtils;
import org.apache.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Parser della shell
 */
@ABDManager
public class ShellManager extends BaseManager implements IShellManager {


    private HashMap<String, IShellCommand> mAvailableCommands = new HashMap<>();
    private HashMap<String, IShellCommand> mInstalledCommands = new HashMap<>();

    private HashMap<String, List<IShellCommandResult>> mListeners = new HashMap<>();


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
    public void parse(String terminal, String input) {
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
                log(Level.DEBUG, "Execute command %s => args %s", commandExecuter.getClass().getSimpleName(), args);

                Object result = commandExecuter.invoke(args.split(" "));

                notifyCommandResult(terminal, commandExecuter, cmd, args.split(" "), result);

                log(Level.DEBUG, "Result => %s", result);
            } else {
                log(Level.DEBUG, "Command not found => %s", cmd);
                notifyCommandNotFound(terminal, cmd, args.split(" "));
            }


        } catch (Exception ex) {
            log(Level.FATAL, "Error during parse %s => %s", input, ex.getMessage());
        }
    }

    @Override
    public void addShellCommandResult(String terminal, IShellCommandResult result) {
        if (mListeners.get(terminal) == null)
            mListeners.put(terminal, new ArrayList<>());

        mListeners.get(terminal).add(result);
    }

    public void removeShellCommandResult(String terminal, IShellCommandResult result) {
        if (mListeners.get(terminal) == null)
            mListeners.get(terminal).remove(result);
    }

    private void notifyCommandResult(String terminal, IShellCommand shellExecuter, String cmd, String[] args, Object result) {
        if (mListeners.get(terminal) != null) {
            for (IShellCommandResult listener : mListeners.get(terminal)) {
                listener.onCommandResult(shellExecuter, cmd, args, result);
            }
        }

    }

    private void notifyCommandNotFound(String terminal, String cmd, String[] args) {
        if (mListeners.get(terminal) != null) {
            for (IShellCommandResult listener : mListeners.get(terminal)) {
                listener.onCommandNotFound(cmd, args);
            }
        }
    }
}
