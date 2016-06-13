package com.github.blackdump.interfaces.windows.dialogs;


public interface ITimerWidgetListener {

    void onTimerStart();

    void onTimerHeartbeat(long seconds);

    void onTimerExpire();
}
