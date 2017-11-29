package com.paincker.utils;


import java.util.IllegalFormatException;

/**
 * Created by jzj on 2017/11/22.
 */
public class L {

    public static void d(String tag, String msg, Object... args) {
        System.out.println("[" + tag + "]  " + formatMessage(msg, args));
    }

    public static void e(String tag, String msg, Object... args) {
        System.err.println("[" + tag + "]  " + formatMessage(msg, args));
    }

    public static void e(Throwable throwable) {
        throwable.printStackTrace();
    }

    private static String formatMessage(String msg, Object... args) {
        if (msg != null && msg.length() > 0 && args != null && args.length > 0) {
            try {
                return String.format(msg, args);
            } catch (IllegalFormatException e) {
                L.e(e);
            }
        }
        return msg;
    }
}
