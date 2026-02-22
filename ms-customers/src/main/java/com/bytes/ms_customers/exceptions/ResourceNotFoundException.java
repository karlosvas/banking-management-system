package com.bytes.ms_customers.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    private final String resource;
    private final Object identifier;

    public ResourceNotFoundException(String resource, Object identifier) {
        super(resource + " no encontrado con identificador: " + identifier);
        this.resource = resource;
        this.identifier = identifier;
    }

    public String getResource() {
        return resource;
    }

    public Object getIdentifier() {
        return identifier;
    }
}
