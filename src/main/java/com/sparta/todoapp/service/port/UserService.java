package com.sparta.todoapp.service.port;

import com.sparta.todoapp.entity.Member;

public interface UserService {
    Member getMemberByUsername(String username);
    void validateDuplicateUsername(String username);
    void validateDuplicateEmail(String email);
    Member save(Member member);
    String findById(Long memberId);
}
