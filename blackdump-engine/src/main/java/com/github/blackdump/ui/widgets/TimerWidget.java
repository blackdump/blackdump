package com.github.blackdump.ui.widgets;

import com.github.blackdump.annotations.ABDDesktopWidget;
import com.github.blackdump.base.BaseDesktopWidget;

import com.github.blackdump.interfaces.windows.dialogs.ITimerWidgetListener;
import com.github.blackdump.utils.TimeFormat;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@ABDDesktopWidget(fxmlFilename = "/windows/timerWidget.fxml", loadAfterLogin = false, name = "timerwidget")
public class TimerWidget extends BaseDesktopWidget {


    private Timer tmrCountdown;
    private long _timer_latch = 100;
    private long _timer_heathbeat_update = 50;
    private long _timer_countdown = 3600;
    private long _timer_real = 0;
    private long _timer_watch_one_sec = 0;

    private long startTime = System.currentTimeMillis();

    private int _timer_lapse = 1;

    @FXML
    private Label lblTimer;

    @FXML
    private Label lblTimerInfo;

    @Getter @Setter
    private ITimerWidgetListener listener;

    @Override
    protected void ready() {
        super.ready();

    }

    public void start(long countdown, String title)
    {
        startTime = System.currentTimeMillis();

        _timer_countdown = countdown;

        lblTimerInfo.setText(title);

        tmrCountdown = new Timer("countdown timer");
        tmrCountdown.schedule(new TimerTask() {
            @Override
            public void run() {

                try
                {
                    listener.onTimerStart();

                    Thread.currentThread().sleep(50);
                } catch (Exception ex) {
                }

                long current = System.currentTimeMillis() - startTime;
                if (current >= _timer_heathbeat_update) {
                    startTime = System.currentTimeMillis();
                    _timer_real -= current * _timer_lapse;


                    Platform.runLater(() -> lblTimer.setText(TimeFormat.format("HH:MM:ss.Z", _timer_real)));


                    if (_timer_real <= 0) {
                        Platform.runLater(() -> lblTimer.setText(TimeFormat.format("HH:MM:ss.Z", 0)));

                        listener.onTimerExpire();
                        stopTimer();
                    }

                    if (_timer_real <= TimeUnit.SECONDS.toMillis(10))
                    {

                        lblTimer.getStyleClass().setAll("timerLabel-alert");
                    }
                }

                if (_timer_watch_one_sec >= 1000) {
                    listener.onTimerHeartbeat(TimeUnit.MILLISECONDS.toSeconds(_timer_real));
                    _timer_watch_one_sec = 0;
                } else {
                    _timer_watch_one_sec += current;
                }

            }
        }, _timer_latch, _timer_latch);

        startTime = System.currentTimeMillis();
        _timer_real = TimeUnit.SECONDS.toMillis(_timer_countdown);

    }

    private void stopTimer() {
        tmrCountdown.cancel();
        tmrCountdown.purge();
     //   ObservableVariables.UpdateVariable(VarNames.CODE_BOMB_STATUS_SET, false);
    }
}
