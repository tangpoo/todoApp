package com.sparta.todoapp.exceptionHandler;

public class UnAuthorizationException extends RuntimeException {
    public UnAuthorizationException(String s) {
        super(s);
    }
}
