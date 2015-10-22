package com.github.blackdump.utils;



import org.reflections.Reflections;
import org.xeustechnologies.jcl.context.JclContext;

import java.util.Set;

/**
 * Utility class for get reflection
 */
public class ReflectionUtils {

    public static Set<Class<?>> getAnnotation(Class type) {
        return new Reflections("com").getTypesAnnotatedWith(type);
    }

    public static Set<Class<?>> getAnnotation(Class type, boolean scanJcl)
    {
        if (!scanJcl)
            return getAnnotation(type);
        else
            return new Reflections(JclContext.get(), "com").getTypesAnnotatedWith(type);
    }
}
