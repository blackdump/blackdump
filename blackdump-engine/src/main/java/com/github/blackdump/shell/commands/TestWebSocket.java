package com.github.blackdump.shell.commands;

import com.github.blackdump.annotations.ABDShellCommand;
import com.github.blackdump.base.BaseShellCommand;
import com.github.blackdump.network.BlackdumpClient;

/**
 * Created by squid on 10/26/15.
 */
@ABDShellCommand(commands = {"testsock"}, help = "Test remote web socket", loadAtStartup = true)
public class TestWebSocket extends BaseShellCommand {
    @Override
    public Object invoke(String[] args) {

        BlackdumpClient client = new BlackdumpClient("localhost:8080");

        client.connect();

        return "OK";
    }
}
