package com.sparta.todoapp.exceptionHandler;

public class NotFindFilterException extends RuntimeException {

    public NotFindFilterException() {
        super("검색 조건을 선택해주세요");
    }
}
