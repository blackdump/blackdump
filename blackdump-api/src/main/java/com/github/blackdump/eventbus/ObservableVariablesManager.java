package com.github.blackdump.eventbus;

import rx.functions.Action1;
import rx.subjects.PublishSubject;

import java.util.HashMap;

/**
 * Class for observe variables
 */
public class ObservableVariablesManager {


    private static HashMap<String, PublishSubject<Object>> mVars = new HashMap<>();



    public static void updateVariable(String varName, Object value) {
        addVariable(varName);
        mVars.get(varName).onNext(value);

    }

    /**
     * Registra una variabile
     * @param name
     */
    private static void addVariable(String name) {
        if (!mVars.containsKey(name))
            mVars.put(name, PublishSubject.create());
    }

    /**
     * Registra una variabile l'observer
     * @param name
     * @param onNext
     */
    public static void subscribe(String name, Action1<? super Object> onNext) {
        addVariable(name);
        mVars.get(name).subscribe(onNext);
    }
}
