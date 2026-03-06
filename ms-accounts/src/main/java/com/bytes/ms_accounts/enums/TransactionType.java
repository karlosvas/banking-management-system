package com.bytes.ms_accounts.enums;

public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER_IN("Incoming transfer"),
    TRANSFER_OUT("Outgoing transfer"),
    TRANSFER("Transfer"),
    PAYMENT("Payment");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
