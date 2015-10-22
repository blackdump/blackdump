package com.github.blackdump.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotazione per creare i comandi della shell
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ABDShellCommand {
    String[] commands();

    String help();

    boolean loadAtStartup() default false;
}
