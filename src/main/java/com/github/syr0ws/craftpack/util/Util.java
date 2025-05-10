package com.github.syr0ws.craftpack.util;

public class Util {

    public static String removeCharsDoubleSlash(String str) {
        return str.replace("\\\\", "\\");
    }
}
