package com.bytes.ms_customers.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
    private final String resource;
    private final String field;
    private final String value;

    public ResourceAlreadyExistsException(String resource, String field, Object value) {
        super(resource + " already exists with " + field + ": " + value);
        this.resource = resource;
        this.field = field;
        this.value = String.valueOf(value);
    }

    public String getResource() {
        return resource;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}