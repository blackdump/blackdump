package com.github.blackdump.ui.widgets;

import com.github.blackdump.annotations.ABDDesktopWidget;
import com.github.blackdump.base.BaseDesktopWidget;
import com.github.blackdump.interfaces.windows.dialogs.IInputStringDialogListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Dialogo per ricevere la string
 */
@ABDDesktopWidget(fxmlFilename = "/windows/inputStringDialog.fxml", loadAfterLogin = false, name = "inputdialog")
public class InputStringDialogWidget extends BaseDesktopWidget {


    @FXML
    private Label lblTitle;

    @FXML
    private Label lblText;

    @FXML
    private Button btnAbort;

    @FXML
    private Button btnOK;

    @FXML
    private TextField edtOutput;


    private IInputStringDialogListener listener;

    public void show(String title, String text, String initialValue, IInputStringDialogListener listener)
    {
        lblTitle.setText(title);
        lblText.setText(text);

        edtOutput.setText(initialValue);

        btnAbort.setOnAction(event -> listener.onNo());
        btnOK.setOnAction(event -> listener.onOK(edtOutput.getText()));
        edtOutput.setOnAction(event -> listener.onOK(edtOutput.getText()));

        edtOutput.requestFocus();
    }
}
