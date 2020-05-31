package com.coffeeshop.Pojos;

public enum OutletName {
    OUTLET1("OUTLET1"),
    OUTLET2("OUTLET2");

    private final String stringValue;

    OutletName(final String s) {
        stringValue = s;
    }

    public String toString() {
        return stringValue;
    }
}
