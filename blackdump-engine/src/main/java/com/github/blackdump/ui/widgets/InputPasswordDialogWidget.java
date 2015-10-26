package com.github.blackdump.ui.widgets;

import com.github.blackdump.annotations.ABDDesktopWidget;
import com.github.blackdump.base.BaseDesktopWidget;
import com.github.blackdump.interfaces.windows.dialogs.IInputStringDialogListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Dialogo di input password
 */
@ABDDesktopWidget(fxmlFilename = "/windows/inputPasswordDialog.fxml", loadAfterLogin = false, name = "inputpassworddialog")
public class InputPasswordDialogWidget extends BaseDesktopWidget {

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblText;

    @FXML
    private Button btnAbort;

    @FXML
    private Button btnOK;

    @FXML
    private PasswordField edtPassword;


    private IInputStringDialogListener listener;

    public void show(String title, String text, String initialValue, IInputStringDialogListener listener)
    {
        lblTitle.setText(title);
        lblText.setText(text);

        edtPassword.setPromptText(initialValue);

        btnAbort.setOnAction(event -> listener.onNo());
        btnOK.setOnAction(event -> listener.onOK(edtPassword.getText()));
        edtPassword.setOnAction(event -> listener.onOK(edtPassword.getText()));

        edtPassword.requestFocus();
    }
}
