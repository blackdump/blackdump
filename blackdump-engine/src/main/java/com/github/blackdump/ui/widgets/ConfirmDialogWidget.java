package com.github.blackdump.ui.widgets;

import com.github.blackdump.annotations.ABDDesktopWidget;
import com.github.blackdump.base.BaseDesktopWidget;
import com.github.blackdump.interfaces.windows.IConfirmDialogListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * Dialog per la conferma
 */
@ABDDesktopWidget(fxmlFilename = "/windows/confirmDialog.fxml", loadAfterLogin = false, name = "confirmdialog")
public class ConfirmDialogWidget extends BaseDesktopWidget {


    @FXML
    private Label lblTitle;

    @FXML
    private Label lblText;

    @FXML
    private Button btnAbort;

    @FXML
    private Button btnOK;

    @FXML
    private ImageView imgDialog;

    @Override
    protected void ready() {

    }

    public void showConfirm(String title, String text, IConfirmDialogListener listener)
    {
        lblTitle.setText(title);
        lblText.setText(text);

        btnAbort.setOnAction(event -> listener.onAbort());
        btnOK.setOnAction(event -> listener.onOK());
    }
}
