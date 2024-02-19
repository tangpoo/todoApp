package com.sparta.todoapp;

import com.sparta.todoapp.repository.UserRepository;
import com.sparta.todoapp.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
public class SignupTest {

    @Autowired
    UserRepository userRepository;



}
