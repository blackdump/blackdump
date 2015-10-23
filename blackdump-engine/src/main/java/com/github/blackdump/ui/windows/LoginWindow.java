package com.github.blackdump.ui.windows;

import com.github.blackdump.base.BaseWindow;
import com.github.blackdump.persistence.BDDatabase;
import com.github.blackdump.persistence.users.User;
import com.github.blackdump.session.SessionManager;
import com.github.blackdump.utils.AppInfo;
import com.google.common.base.Strings;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.util.Optional;
import java.util.Random;


/**
 *  Finestra di login
 */
public class LoginWindow extends BaseWindow {


    @FXML
    private TextArea txtHeader;

    @FXML
    private Text lblVersion;

    @FXML
    private Pane pnlLogin;

    @FXML
    private TextField edtUsername;

    @FXML
    private PasswordField edtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnCreatenew;

    @FXML
    private ProgressIndicator prgLogin;


    private double m56KLenght;

    private boolean mDotThread = true;
    @FXML
    void initialize()
    {

        edtUsername.setEditable(false);
        edtPassword.setEditable(false);
        pnlLogin.setVisible(false);
        txtHeader.setText("");
        txtHeader.setWrapText(true);

        txtHeader.appendText("Connecting to 555-BL4CK-NETWORK BBS.");

        lblVersion.setText("// For your eyes only");
        edtUsername.requestFocus();

        btnLogin.setOnAction(event -> {
            new Thread(() -> {
                try {
                    Platform.runLater(() -> prgLogin.setVisible(true));
                    Thread.sleep(1000);
                    Platform.runLater(() -> prgLogin.setVisible(false));

                    Platform.runLater(() -> checkLogin(edtUsername.getText(), edtPassword.getText()));


                } catch (Exception ex) {

                }
            }).start();
        });

        btnCreatenew.setOnAction(event -> {
            if (!Strings.isNullOrEmpty(edtUsername.getText()) && !Strings.isNullOrEmpty(edtPassword.getText())) {
                SessionManager.getEngine().getDatabaseManager().getDatabase().getUsers().add(User.newUser(edtUsername.getText(), edtUsername.getText() + "@" + "blackdump.onion", edtPassword.getText()));
                SessionManager.getEngine().getDatabaseManager().saveDatabase();
                checkLogin(edtUsername.getText(), edtPassword.getText());
            }
        });


        if (!AppInfo.DEBUG)

        {
            try {
                Media sound = new Media(getClass().getResource("/sounds/56k_dialup2.wav").toURI().toString());

                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.setOnReady(() -> {
                    m56KLenght = sound.getDuration().toMillis();
                    mediaPlayer.play();

                    startDotThread();
                    startLoginThread();
                });


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else {
            m56KLenght = 1000;
            startDotThread();
            startLoginThread();
        }



    }

    private void startDotThread() {
        Thread thread = new Thread(() -> {
            try {
                while (mDotThread) {
                    Thread.sleep(500);
                    Platform.runLater(() -> txtHeader.appendText("."));
                }
            } catch (Exception ex) {

            }
        });
        thread.start();
    }

    private void startLoginThread()
    {
        Thread thread = new Thread(() -> {

            try {
                Thread.sleep((long) m56KLenght);
                mDotThread = false;
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
                        //     Thread.sleep(new Random().nextInt(10) + 1);
                    }
                    Platform.runLater(() -> txtHeader.appendText("\n"));
                    Thread.sleep(new Random().nextInt(50) + 1);
                }
                catch (Exception ex)
                {

                }
            }

            Platform.runLater(() -> {
                edtUsername.setEditable(true);
                edtPassword.setEditable(true);
                edtUsername.requestFocus();
                pnlLogin.setVisible(true);
            });


        });
        thread.start();
    }

    private void checkLogin(String username, String password) {

        Platform.runLater(() -> txtHeader.appendText("Checking credentials...\n"));

        BDDatabase database = SessionManager.getEngine().getDatabaseManager().getDatabase();

        Optional<User> mUser = database.getUsers().stream().filter(user -> user.getNickname().equals(username) && user.getPassword().equals(password)).findFirst();

        if (!mUser.isPresent()) {
            Platform.runLater(() -> txtHeader.appendText("Username or password invalid!\n"));
        } else {
            SessionManager.setCurrentUser(mUser.get());
            getEngine().getUiManager().createWindow(String.format("%s:", SessionManager.getCurrentUser().getEmail()), "/windows/terminalWindow.fxml", true, true, true);
            getParentWindow().close();
            getEngine().getUiManager().notifyAfterLogin();

        }
    }


}
