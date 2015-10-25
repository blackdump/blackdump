package com.github.blackdump.interfaces.windows;

import com.github.blackdump.annotations.ABDMenuItem;

import java.util.List;

/**
 * Listener per la creazione delle finestre,
 * per controller della menuBar, per poter aggiungere
 * e rimuovere nel task
 */
public interface IWindowListener {

    void onWindowCreated(IBDWindow window);

    void onWindowClosed(IBDWindow window);

    void onBuildMenuTree(List<ABDMenuItem> items);
}
