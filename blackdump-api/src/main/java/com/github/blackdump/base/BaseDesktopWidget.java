package com.github.blackdump.base;

import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.windows.IBDDesktopWidget;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Classe base per la crazione dei widgets
 */
public class BaseDesktopWidget implements IBDDesktopWidget{

    private Logger mLogger;

    @Getter(AccessLevel.PROTECTED)
    private AnchorPane parentDesktop;

    @Getter(AccessLevel.PROTECTED)
    private IBlackdumpEngine engine;


    private Pane myPane;



    public BaseDesktopWidget()
    {
        mLogger = Logger.getLogger(getClass());
    }

    protected void log(Level level, String text, Object... args) {
        mLogger.log(level, String.format(text, args));
    }

    @Override
    public void setEngine(IBlackdumpEngine engine) {
        this.engine = engine;
    }

    @Override
    public void setDesktop(AnchorPane parent) {

        this.parentDesktop = parent;
        ready();
    }

    @Override
    public void setMyPane(Pane pane) {

        this.myPane = pane;
    }

    @Override
    public void center() {
        myPane.setLayoutX(getEngine().getUiManager().getDesktopWidth() / 2 - myPane.getPrefWidth() / 2);
        myPane.setLayoutY(getEngine().getUiManager().getDesktopHeight() / 2 - myPane.getPrefHeight() / 2);
    }

    @Override
    public void addToDesktop() {
        if (myPane != null)
        {
            getEngine().getUiManager().addDesktopChildren(myPane);
        }
    }

    @Override
    public void removeFromDesktop() {
        if (myPane != null)
        {
            getEngine().getUiManager().removeDesktopChildren(myPane);
        }

    }


    protected void ready()
    {

    }
}
