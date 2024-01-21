package com.example.booking_ma_tim21.util;

public class AppConfig {

    private static String OS = System.getProperty("os.name").toLowerCase();
    public static boolean IS_MAC = (OS.indexOf("mac") >= 0);
    public static  Long UNIX_DAY_VAL = (IS_MAC) ? 86400L : 86400000L;
    public static int UNIX_DIFF=(IS_MAC) ? 1000 : 1;
}