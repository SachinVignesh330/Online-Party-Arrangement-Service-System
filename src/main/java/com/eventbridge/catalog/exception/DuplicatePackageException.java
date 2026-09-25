package com.eventbridge.catalog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when admin tries to create a package with a name that already exists.
 * Maps to HTTP 409 Conflict.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatePackageException extends RuntimeException {

    public DuplicatePackageException(String packageName) {
        super("A package with the name '" + packageName + "' already exists.");
    }
}
