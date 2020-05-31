package com.coffeeshop.Pojos;

public enum OutletName {
    OUTLET1("OUTLET1"),
    OUTLET2("OUTLET2"),
    OUTLET3("OUTLET3"),
    OUTLET4("OUTLET4"),
    OUTLET5("OUTLET5"),
    OUTLET6("OUTLET6");

    private final String stringValue;

    OutletName(final String s) {
        stringValue = s;
    }

    public String toString() {
        return stringValue;
    }
}
