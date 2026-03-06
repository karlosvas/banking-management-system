package com.bytes.ms_accounts.enums;

public enum CustomerRole {
    CUSTOMER("Cliente"),
    ADMIN("Administrador");

    private final String name;

    CustomerRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CustomerRole fromString(String role) {
        for (CustomerRole r : CustomerRole.values()) {
            if (r.name.equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Rol de cliente desconocido: " + role);
    }
}
