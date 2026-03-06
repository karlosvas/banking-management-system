package com.bytes.ms_accounts.enums;

/**
 * Represents the lifecycle state of an account.
 */
public enum AccountStatus {
    ACTIVE("Active Account"),
    INACTIVE("Inactive Account"),
    FROZEN("Frozen Account"),
    CLOSED("Closed Account");

    private final String description;

    AccountStatus(String description) {
        this.description = description;
    }

    /**
     * Returns the display description for the status.
     *
     * @return status description
     */
    public String getDescription() {
        return description;
    }
}
