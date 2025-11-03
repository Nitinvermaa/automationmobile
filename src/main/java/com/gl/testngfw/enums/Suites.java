package com.gl.testngfw.enums;

public enum Suites {
    REGRESSION,
    SANITY,
    P1,
    P2,
    P3,
    NON_FUNCTIONAL,
    PACT;

    public String getSuite() {
        return this.name();
    }
}
