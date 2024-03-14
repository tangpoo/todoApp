package com.sparta.todoapp.dto.user;

import com.sparta.todoapp.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponseDto {

    private Long id;
    private String userName;

    public SignupResponseDto(Member member) {
        this.id = member.getId();
        this.userName = member.getUsername();
    }
}
