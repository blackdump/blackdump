package com.github.blackdump.ui.widgets;

import com.github.blackdump.annotations.ABDDesktopWidget;
import com.github.blackdump.base.BaseDesktopWidget;
import com.github.blackdump.interfaces.windows.IProgressDialogListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;



/**
 * Dialogo per simulare un caricamento / download
 */
@ABDDesktopWidget(fxmlFilename = "/windows/progressDialog.fxml", loadAfterLogin = false, name = "progressdialog")
public class ProgressDialogWidget extends BaseDesktopWidget {


    @FXML
    private Label lblTitle;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblRemainingTime;

    @FXML
    private Label lblFileStatus;

    @FXML
    private ProgressBar prgStatus;

    private String[] files;

    private Thread mRunningThread;

    private IProgressDialogListener listener;

    private boolean isFinished = false;

    private double currentTime = 0;
    private double taskSeconds;
    private long everyupdates = 0;


    @Override
    protected void ready() {
        super.ready();
    }

    public void startProgress(String title, String status,long everyupdates, int taskSeconds, IProgressDialogListener listener,  String ... files)
    {
        lblTitle.setText(title);
        lblStatus.setText(status);
        this.files = files;
        this.listener = listener;
        prgStatus.setProgress(0);
        this.taskSeconds = taskSeconds;
        this.everyupdates = everyupdates;

        startThread();

    }

    private void startThread()
    {
        mRunningThread = new Thread(this::runThread);
        mRunningThread.start();
    }

    private void runThread()
    {
        try {
            while (!isFinished) {
                Thread.sleep(everyupdates);
                currentTime += 1;

                if (currentTime < taskSeconds)
                {
                    Platform.runLater(() -> {


                        double percentageToShow =    (currentTime * 100/ taskSeconds);
                        double progressPercentage = (currentTime * 1.0/ taskSeconds);

                        lblStatus.setText(String.format("%s %%",Double.toString(percentageToShow)));
                        prgStatus.setProgress(progressPercentage);
                        lblRemainingTime.setText(String.format("Remaining time: %s seconds", taskSeconds - currentTime));
                    });

                }
                else
                {
                    isFinished = true;
                }

            }

            listener.onProgressFinished();
        }
        catch (Exception ex)
        {

        }
    }


}
