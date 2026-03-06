package com.bytes.ms_accounts.enums;

/**
 * Defines supported customer roles.
 */
public enum CustomerRole {
    CUSTOMER("Customer"),
    ADMIN("Administrator");

    private final String name;

    CustomerRole(String name) {
        this.name = name;
    }

    /**
     * Returns the role display name.
     *
     * @return role name
     */
    public String getName() {
        return name;
    }

    /**
     * Parses a role value from text.
     *
     * @param role role text
     * @return parsed role
     */
    public static CustomerRole fromString(String role) {
        for (CustomerRole r : CustomerRole.values()) {
            if (r.name.equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Unknown customer role: " + role);
    }
}
