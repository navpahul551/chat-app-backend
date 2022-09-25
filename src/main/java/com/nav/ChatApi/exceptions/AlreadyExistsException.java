package com.nav.ChatApi.exceptions;

public class AlreadyExistsException extends Exception {
    public AlreadyExistsException() { }

    public AlreadyExistsException(String message) { super(message); }
}
