package com.bytes.ms_accounts.enums;

public enum AccountStatus {
    ACTIVE("Active Account"),
    INACTIVE("Inactive Account"),
    FROZEN("Frozen Account"),
    CLOSED("Closed Account");

    private final String description;

    AccountStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
