package com.github.blackdump.ui.windows;

import com.github.blackdump.base.BaseWindow;
import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.windows.IBDWindow;
import com.github.blackdump.utils.AppInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.io.File;
import java.util.Random;


/**
 *  Finestra di login
 */
public class LoginWindow  {


    @FXML
    private TextArea txtHeader;

    @FXML
    private Text lblVersion;

    @FXML
    private TextField edtUsername;

    @FXML
    private PasswordField edtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnCreatenew;

    @FXML
    void initialize()
    {

        edtUsername.setEditable(false);
        edtPassword.setEditable(false);
        txtHeader.setText("");

        txtHeader.appendText("Connecting to 555-BL4CK-NETWORK BBS....\n");

        lblVersion.setText("// For your eyes only");
        edtUsername.requestFocus();




        try {
            Media sound = new Media(getClass().getResource("/sounds/56k_dialup.wav").toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();

            startLoginThread();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }



    }

    private void startLoginThread()
    {
        Thread thread = new Thread(() -> {

            try {
              //  Thread.sleep(30 * 1000 );
                txtHeader.appendText("Connected!\n\n");
            }
            catch (Exception ex)
            {

            }
            String textToWrite = AppInfo.AppHeader + "\n\n" + String.format("   > %s v%s <", AppInfo.AppName, AppInfo.AppVersion);

            String[] arr = textToWrite.split("\n");

            for (String str : arr)
            {
                try {
                    for (int i =0;i<str.length()-1;i++)
                    {
                        char chr = str.toCharArray()[i];
                        Platform.runLater(() -> txtHeader.appendText(new StringBuilder().append("").append(chr).toString()));
                        Thread.sleep(new Random().nextInt(60) + 1);
                    }
                    Platform.runLater(() -> txtHeader.appendText("\n"));
                    Thread.sleep(new Random().nextInt(200) + 1);
                }
                catch (Exception ex)
                {

                }
            }

            edtUsername.setEditable(true);
            edtPassword.setEditable(true);

        });
        thread.start();
    }


}
