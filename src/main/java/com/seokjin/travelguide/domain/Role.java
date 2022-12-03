package com.seokjin.travelguide.domain;

public enum Role {
    MEMBER("ROLE_MEMBER"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
