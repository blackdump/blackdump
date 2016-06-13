package com.github.blackdump.ui.windows;

import com.github.blackdump.annotations.ABDMenuItem;
import com.github.blackdump.annotations.ABDPopmenuEntry;
import com.github.blackdump.annotations.WindowType;
import com.github.blackdump.base.BaseWindow;
import com.github.blackdump.interfaces.shell.IShellCommandResult;
import com.github.blackdump.shell.IShellCommand;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Finestra di terminale
 */
@ABDMenuItem(name = "Terminal", fxml = "/windows/terminalWindow.fxml", type = WindowType.WINDOW, position = "Utility->Terminals", isInstalled = true)
@ABDPopmenuEntry(fxml = "/windows/terminalWindow.fxml", order = 1, text = "Open terminal here")
public class TerminalWindow extends BaseWindow {

    @FXML
    private TextField edtCommand;

    @FXML
    private Label lblPrompt;

    private List<String> mHistory = new ArrayList<>();

    private int currentHistoryIndex = 0;

    @FXML
    private StyleClassedTextArea txtConsole;

    private String terminal = "terminal " + new Random().nextInt(300);

    @FXML
    private void initialize() {
        edtCommand.setText("");

        getEngine().getShellManager().addShellCommandResult(terminal, new IShellCommandResult() {
            @Override
            public void onCommandResult(IShellCommand shellExecuter, String cmd, String[] args, Object result) {
                parseString((String) result + "\n");
                edtCommand.setText("");
            }

            @Override
            public void onCommandNotFound(String cmd, String[] args) {
                parseString(String.format("sqshell: command not found: %s\n", cmd));
                edtCommand.setText("");
            }
        });

        updatePromptLabel();
        getEngine().getShellManager().parse(terminal, "header");


        edtCommand.setOnAction(event ->
        {
            getEngine().getShellManager().parse(terminal, edtCommand.getText());
            mHistory.add(edtCommand.getText());
        });

        edtCommand.setOnKeyPressed(event -> {

            switch (event.getCode())
            {
                case DOWN:

                    if (currentHistoryIndex < mHistory.size())
                        currentHistoryIndex ++;


                    edtCommand.setText(mHistory.get(currentHistoryIndex));
                    break;
                case UP:
                    if (currentHistoryIndex == 0)
                    {
                        currentHistoryIndex = mHistory.size() - 1;
                    }


                    edtCommand.setText(mHistory.get(currentHistoryIndex));
                    break;
            }

        });
    }

    private void updatePromptLabel()
    {
        String text = getEngine().getShellManager().getPrompt(terminal);
        Platform.runLater(() -> {

            lblPrompt.setText(text);
        });
    }

    private void parseString(final String text)
    {

        Platform.runLater(() -> {

            int max_position = txtConsole.getLength();

            if (text.contains("[#"))
            {
                String[] lines = text.split("\n");

                for (String line : lines)
                {
                    int tokenIdx = line.indexOf("[#");
                    int tokenEnd = -1;

                    if (tokenIdx != -1)
                    {
                        tokenEnd = line.indexOf("]", tokenIdx);

                        String token = line.substring(tokenIdx, tokenEnd+1);

                        line = line.replace(token, "");
                        line += "\n";
                        token = token.replace("[#","").replace("]","").toLowerCase();

                        txtConsole.appendText(line);
                        txtConsole.setStyleClass(max_position, max_position + line.length(), "shell-output-text-"+token);
                        max_position += line.length();
                    }
                    else
                    {
                        txtConsole.appendText(line +"\n");
                        txtConsole.setStyleClass(max_position, max_position + line.length(), "shell-output-text-default");
                        max_position += line.length();
                    }
                }
            }
            else
            {
                txtConsole.appendText(text);
                txtConsole.setStyleClass(max_position, max_position + text.length(), "shell-output-text-default");
            }
        });
    }





}
