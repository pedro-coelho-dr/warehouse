package com.warehouse.warehouse.scrap;

public class StringUtil {
    private StringUtil() {}
    public static boolean ehNuloOuBranco(String str) {
        return str == null || str.trim().equals("");
    }
}