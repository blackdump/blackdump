package com.github.blackdump.interfaces.windows.dialogs;

/**
 * Interfaccia per prendere il risultato di un dialogo input
 */
public interface IInputStringDialogListener {

    void onOK(String result);

    void onNo();
}
