package com.github.blackdump.serializer;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.Serializable;


/**
 * Class for handle data serialization with Json
 */
public class JsonSerializer {

    private static Logger mLogger = Logger.getLogger(JsonSerializer.class);

    private static Gson mGson = new GsonBuilder().setPrettyPrinting().create();


    /**
     * Serialize object
     *
     * @param obj
     * @return
     */

    public static String Serialize(Object obj) {
        try {

            return mGson.toJson(obj);
        } catch (Exception ex) {
            log(Level.ERROR, "Error during serialize obj => %s. Error => %s", obj.getClass().getName(), ex.getMessage());
            return null;
        }
    }

    /**
     * Deserialize object
     *
     * @param data
     * @param classz
     * @param <T>
     * @return
     */

    public static <T extends Serializable> T Deserialize(String data, Class<T> classz) {
        try {
            return mGson.fromJson(data, classz);
        } catch (Exception ex) {
            log(Level.ERROR, "Error during deserialize data => %s. Error => %s", data.substring(0, 10), ex.getMessage());
            return null;
        }
    }

    // Funzione utility di logging
    protected static void log(Level level, String text, Object... args) {
        mLogger.log(level, String.format(text, args));
    }

}
