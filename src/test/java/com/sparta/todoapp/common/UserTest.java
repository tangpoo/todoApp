package com.sparta.todoapp.common;

import com.sparta.todoapp.entity.User;

public interface UserTest {
    String ANOTHER_PREFIX = "another";
    Long TEST_USER_ID = 1L;
    Long TEST_ANOTHER_USER_ID = 2L;
    String TEST_USER_NAME = "username";
    String TEST_USER_PASSWORD = "password";
    String TEST_USER_EMAIL = "email@email.com";
    String TEST_TOKEN = "failToken";
    User TEST_USER = User.builder()
            .username(TEST_USER_NAME)
            .password(TEST_USER_PASSWORD)
            .email(TEST_USER_EMAIL)
            .build();

    User TEST_ANOTER_USER = User.builder()
            .username(ANOTHER_PREFIX + TEST_USER_NAME)
            .password(ANOTHER_PREFIX + TEST_USER_PASSWORD)
            .email(ANOTHER_PREFIX + TEST_USER_EMAIL)
            .build();
}
