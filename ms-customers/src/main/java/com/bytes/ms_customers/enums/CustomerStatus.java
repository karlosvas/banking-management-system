package com.bytes.ms_customers.enums;

/**
 * Defines possible customer statuses.
 */
public enum CustomerStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BLOCKED("Blocked"),
    PENDING_VERIFICATION("Pending verification");

    private final String name;

    CustomerStatus(String name) {
        this.name = name;
    }

    /**
     * Returns the status display name.
     *
     * @return status name
     */
    public String getName() {
        return name;
    }

    /**
     * Parses a customer status from text.
     *
     * @param status status text
     * @return parsed status
     */
    public static CustomerStatus fromString(String status) {
        for (CustomerStatus s : CustomerStatus.values()) {
            if (s.name.equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown customer status: " + status);
    }
}