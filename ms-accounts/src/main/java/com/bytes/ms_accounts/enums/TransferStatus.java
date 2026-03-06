package com.bytes.ms_accounts.enums;

/**
 * Represents the processing state of a transfer.
 */
public enum TransferStatus {
    COMPLETED("Completed"),
    PENDING("Pending"),
    FAILED("Failed"),
    REVERSED("Reversed");

    private final String description;

    TransferStatus(String description) {
        this.description = description;
    }

    /**
     * Returns the display description for the transfer status.
     *
     * @return transfer status description
     */
    public String getDescription() {
        return description;
    }
}
