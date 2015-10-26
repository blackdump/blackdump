package com.github.blackdump.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Raccolta di funzioni utili
 */
public class FuncsUtility {

    public static List<File> getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {


        List<File> result = new ArrayList<>();

        URL url = clazz.getResource(path);


        if (url == null) {
            // error - missing folder
        } else {
            File dir = new File(url.getPath());
            for (File nextFile : dir.listFiles()) {
                result.add(nextFile);
            }
        }
        return result;
    }

    public static String getCurrentDateTime() {
        return LocalDateTime.now().minusYears(20).format(DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss "));
    }
}
