package com.bytes.ms_accounts.enums;

/**
 * Represents the processing state of a transaction.
 */
public enum TransactionStatus {
    PENDING("Pending"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    SCHEDULED("Scheduled"),
    CANCELLED("Cancelled");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    /**
     * Returns the display description for the transaction status.
     *
     * @return status description
     */
    public String getDescription() {
        return description;
    }
}
