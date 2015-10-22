package com.github.blackdump.ui.windows;

import com.github.blackdump.base.BaseWindow;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.fxmisc.richtext.StyleClassedTextArea;

/**
 * Created by squid on 10/22/15.
 */
public class TerminalWindow extends BaseWindow {

    @FXML
    private TextField edtCommand;

    @FXML
    private StyleClassedTextArea txtConsole;


    @FXML
    private void initialize() {
        edtCommand.setText("");


        edtCommand.setOnAction(event ->
        {
            getEngine().getShellManager().parse(edtCommand.getText());
            edtCommand.setText("");
        });
    }
}
