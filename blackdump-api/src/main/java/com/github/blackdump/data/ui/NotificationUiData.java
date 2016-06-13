package com.github.blackdump.data.ui;


import com.github.blackdump.interfaces.windows.dialogs.DialogTypeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class NotificationUiData  implements Serializable {

    private String title;
    private String text;
    private DialogTypeEnum type;

}
