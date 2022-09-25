package com.nav.ChatApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception{
    public UnauthorizedException() { super("Unauthorized access!"); }
    public UnauthorizedException(String message) { super(message); }
}