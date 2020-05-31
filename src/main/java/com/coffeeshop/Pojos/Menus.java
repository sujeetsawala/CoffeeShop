package com.coffeeshop.Pojos;

public enum Menus {
    HOT_MILK("HOT_MILK"),
    HOT_WATER("HOT_WATER");

    private final String stringValue;

    Menus(final String s) {
        stringValue = s;
    }

    public String toString() {
        return stringValue;
    }
}
