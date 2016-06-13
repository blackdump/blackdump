package com.github.blackdump.utils;



import com.github.blackdump.data.ui.NotificationUiData;
import com.github.blackdump.eventbus.EventBusMessages;
import com.github.blackdump.interfaces.windows.dialogs.DialogTypeEnum;
import com.github.blackdump.session.SessionManager;

public class NotificationUtil {

    public static void showNotification(DialogTypeEnum typeEnum, String title, String text, Object ... args)
    {
        NotificationUiData data = new NotificationUiData();
        data.setTitle(title);
        data.setType(typeEnum);
        data.setText(String.format(text, args));

        SessionManager.getEngine().broadcastEvent(EventBusMessages.NOTIFICATION_SHOW_MESSAGE, data);
    }
}
