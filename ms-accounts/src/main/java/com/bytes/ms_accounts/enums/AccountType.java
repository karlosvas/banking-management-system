package com.bytes.ms_accounts.enums;

/**
 * Defines supported account types.
 */
public enum AccountType {
    CHECKING("Checking Account"),
    SAVINGS("Savings Account");

    private final String description;

    AccountType(String description) {
        this.description = description;
    }

    /**
     * Returns the display description for the account type.
     *
     * @return account type description
     */
    public String getDescription() {
        return description;
    }
}
