package com.warehouse.warehouse.util;

public class StringUtil {
    private StringUtil() {}
    public static boolean ehNuloOuBranco(String str) {
        return str == null || str.trim().equals("");
    }
}