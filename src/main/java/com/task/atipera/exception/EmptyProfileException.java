package com.task.atipera.exception;

public class EmptyProfileException extends RuntimeException {
    public EmptyProfileException(String username) {
        super("Profile {} has no public repositories");
    }
}
