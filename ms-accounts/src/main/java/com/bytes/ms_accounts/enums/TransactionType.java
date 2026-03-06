package com.bytes.ms_accounts.enums;

/**
 * Defines supported transaction categories.
 */
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

    /**
     * Returns the display description for the transaction type.
     *
     * @return transaction type description
     */
    public String getDescription() {
        return description;
    }
}
