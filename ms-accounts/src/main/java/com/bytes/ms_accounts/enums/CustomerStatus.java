package com.bytes.ms_accounts.enums;

public enum CustomerStatus {
    ACTIVE("Activo"),
    INACTIVE("Inactivo"),
    BLOCKED("Bloqueado"),
    PENDING_VERIFICATION("Pendiente de verificación");

    private final String name;

    CustomerStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CustomerStatus fromString(String status) {
        for (CustomerStatus s : CustomerStatus.values()) {
            if (s.name.equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Estado de cliente desconocido: " + status);
    }
}
