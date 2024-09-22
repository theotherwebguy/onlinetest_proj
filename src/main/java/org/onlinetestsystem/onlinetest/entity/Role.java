package org.onlinetestsystem.onlinetest.entity;

public enum Role {
    STUDENT,
    LECTURER;

    public static Role fromString(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown role: " + role, e);
        }
    }
}
