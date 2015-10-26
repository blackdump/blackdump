package com.github.blackdump.ui.widgets;

import com.github.blackdump.annotations.ABDDesktopWidget;
import com.github.blackdump.base.BaseDesktopWidget;
import com.github.blackdump.interfaces.windows.dialogs.IMessageDialogListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Message dialog
 */
@ABDDesktopWidget(fxmlFilename = "/windows/messageDialog.fxml", loadAfterLogin = false, name = "messagedialog")
public class MessageDialogWidget extends BaseDesktopWidget {


    @FXML
    private Label lblTitle;

    @FXML
    private Label lblText;

    @FXML
    private Button btnOK;

    private IMessageDialogListener listener;


    public void show(String title, String text, IMessageDialogListener listener)
    {
        lblTitle.setText(title);
        lblText.setText(text);
        this.listener  = listener;

        btnOK.setOnAction(event -> listener.onOK());

    }
}
