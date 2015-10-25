package com.github.blackdump.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotazione per creare l'albero dei menu nella MenuBar
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ABDMenuItem {
    String fxml();

    WindowType type() default WindowType.WINDOW;

    String position() default "Utility->";

    String name();

    boolean isInstalled() default false;

}
