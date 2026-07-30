package com.library.management.exception;

public class ResourceNotFoundException extends RuntimeException {

    private final String code;
    private final String resource;

    public ResourceNotFoundException(String code, String resource, String message) {
        super(message);
        this.code = code;
        this.resource = resource;
    }

    public String getCode() {
        return code;
    }

    public String getResource() {
        return resource;
    }
}
