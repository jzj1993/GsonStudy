package com.paincker.utils;

import java.io.*;

/**
 * Created by jzj on 2017/11/22.
 */
public class StringUtils {

    public static String readAsString(File file) {
        try {
            return readAsString(new FileInputStream(file), "utf-8");
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static String readAsString(InputStream stream) {
        return readAsString(stream, "utf-8");
    }

    public static String readAsString(InputStream is, String encode) {
        if (is == null) {
            return null;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, encode));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            L.e(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    L.e(e);
                }
            }
        }
        return null;
    }
}
