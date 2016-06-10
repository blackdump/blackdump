package com.github.blackdump.jsobjects;

import com.github.blackdump.annotations.ADBJavascriptWrap;
import com.github.blackdump.interfaces.engine.IBlackdumpEngine;
import com.github.blackdump.interfaces.jsengine.IJsObject;

@ADBJavascriptWrap("testclass")
public class JsTestClass implements IJsObject {

    private IBlackdumpEngine engine;

    @Override
    public void init(IBlackdumpEngine engine) throws Exception {
        this.engine = engine;
    }

    public void exit()
    {
        engine.quit(true);

    }
}
