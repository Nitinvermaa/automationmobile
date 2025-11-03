package com.gl.testngfw.enums;


public enum Authority {
    ROLE_USER,
    ROLE_ADMIN,
    ANONYMOUS;

    public String getAuthority() {
        return this.name();
    }
}
