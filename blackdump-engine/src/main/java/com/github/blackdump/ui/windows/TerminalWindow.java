package com.github.blackdump.ui.windows;

import com.github.blackdump.base.BaseWindow;
import com.github.blackdump.interfaces.shell.IShellCommandResult;
import com.github.blackdump.shell.IShellCommand;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.util.Random;

/**
 * Finestra di terminale
 */
public class TerminalWindow extends BaseWindow {

    @FXML
    private TextField edtCommand;

    @FXML
    private StyleClassedTextArea txtConsole;

    private String terminal = "terminal " + new Random().nextInt(300);

    @FXML
    private void initialize() {
        edtCommand.setText("");

        getEngine().getShellManager().addShellCommandResult(terminal, new IShellCommandResult() {
            @Override
            public void onCommandResult(IShellCommand shellExecuter, String cmd, String[] args, Object result) {
                txtConsole.appendText((String) result + "\n");
                edtCommand.setText("");

            }

            @Override
            public void onCommandNotFound(String cmd, String[] args) {
                txtConsole.appendText(String.format("Command '%s' not found!\n", cmd));
            }
        });
        edtCommand.setOnAction(event ->
        {
            getEngine().getShellManager().parse(terminal, edtCommand.getText());

        });
    }


}
