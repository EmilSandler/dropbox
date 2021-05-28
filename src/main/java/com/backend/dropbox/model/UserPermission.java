package com.backend.dropbox.model;

public enum UserPermission {
    ADMIN_DELETE("admin:delete"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    USER_DELETE("user:delete"),
    USER_READ("user:read"),
    USER_WRITE("user:write");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
