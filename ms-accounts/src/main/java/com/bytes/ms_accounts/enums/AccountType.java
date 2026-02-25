package com.bytes.ms_accounts.enums;

public enum AccountType {
    CHECKING("Checking Account"),
    SAVINGS("Savings Account");

    private final String description;

    AccountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
