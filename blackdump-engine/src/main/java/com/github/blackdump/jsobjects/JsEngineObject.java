package com.github.blackdump.jsobjects;


import com.github.blackdump.annotations.ADBJavascriptWrap;
import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.jsengine.IJsObject;
import com.github.blackdump.interfaces.managers.IUiManager;


@ADBJavascriptWrap("engine")
public class JsEngineObject implements IJsObject {



    public IBlackdumpEngine blackdumpEngine;

    public IUiManager uiManager;

    @Override
    public void init(IBlackdumpEngine engine) throws Exception {

        this.blackdumpEngine = engine;
        uiManager =  engine.getUiManager();


    }
}
