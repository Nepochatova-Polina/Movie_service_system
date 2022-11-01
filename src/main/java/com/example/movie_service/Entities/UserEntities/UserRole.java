package com.example.movie_service.Entities.UserEntities;

public enum UserRole {
    CLIENT("client"),
    ADMINISTRATOR("administrator");

    private final String text;

    UserRole(String text) {
        this.text = text;
    }

    public static UserRole fromString(String text) {
        for (UserRole b : UserRole.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

    public String toString() {
        return text;
    }
}
