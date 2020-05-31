package com.coffeeshop.Pojos;

public enum Ingredients {
    HOT_MILK("HOT_MILK"),
    HOT_WATER("HOT_WATER"),
    TEA_LEAVES("TEA_LEAVES");

    private final String stringValue;

    Ingredients(final String s) {
        stringValue = s;
    }

    public String toString() {
        return stringValue;
    }
}
