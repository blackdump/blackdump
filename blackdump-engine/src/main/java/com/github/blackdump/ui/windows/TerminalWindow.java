package com.github.blackdump.ui.windows;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.fxmisc.richtext.StyleClassedTextArea;

/**
 * Created by squid on 10/22/15.
 */
public class TerminalWindow extends Pane {

    @FXML
    private TextField edtCommand;

    @FXML
    private StyleClassedTextArea txtConsole;


    @FXML
    private void initialize() {
        edtCommand.setText("CIAO");
        edtCommand.setOnAction(event ->
        {
            txtConsole.appendText(edtCommand.getText() + "\n");
            edtCommand.setText("");
        });
    }
}
