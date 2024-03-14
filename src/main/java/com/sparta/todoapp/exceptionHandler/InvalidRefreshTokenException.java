package com.sparta.todoapp.exceptionHandler;

public class InvalidRefreshTokenException extends RuntimeException {

    public InvalidRefreshTokenException() {
        super("Refresh_Token 이 유효하지 않습니다.");
    }
}
