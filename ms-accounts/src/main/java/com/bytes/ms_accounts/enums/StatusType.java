package com.bytes.ms_accounts.enums;

public enum StatusType {
    ACTIVE("Activo"),
    INACTIVE("Inactivo"),
    SUSPENDED("Suspendido");

    private final String description;

    StatusType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static StatusType fromString(String status) {
        for (StatusType type : StatusType.values()) {
            if (type.name().equalsIgnoreCase(status)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
