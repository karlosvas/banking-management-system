package com.bytes.ms_accounts.enums;

public enum TransferStatus {
    COMPLETED("Completed"),
    PENDING("Pending"),
    FAILED("Failed"),
    REVERSED("Reversed");

    private final String description;

    TransferStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
