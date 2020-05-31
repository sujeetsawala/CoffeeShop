package com.coffeeshop.Pojos;

public enum Menus {
    HOT_MILK("HOT_MILK"),
    HOT_COFFEE("HOT_COFFEE"),
    GINGER_TEA("GINGER_TEA"),
    HOT_TEA("HOT_TEA");

    private final String stringValue;

    Menus(final String s) {
        stringValue = s;
    }

    public String toString() {
        return stringValue;
    }
}
