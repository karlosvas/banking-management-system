package com.bytes.ms_accounts.enums;

/**
 * Generic status type used by account-related flows.
 */
public enum StatusType {
    ACTIVE("Activo"),
    INACTIVE("Inactivo"),
    SUSPENDED("Suspendido");

    private final String description;

    StatusType(String description) {
        this.description = description;
    }

    /**
     * Returns the status description.
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Parses a status type from text.
     *
     * @param status status text
     * @return parsed status type
     */
    public static StatusType fromString(String status) {
        for (StatusType type : StatusType.values()) {
            if (type.name().equalsIgnoreCase(status)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
