package com.benberi.cadesim.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class SimpleFileUtils {

    public static String readStringFromFile(String path) {
        File file = new File(path);
        try {
            return FileUtils.readFileToString(file, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
