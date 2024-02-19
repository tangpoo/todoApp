package com.sparta.todoapp.dto.user;

import com.sparta.todoapp.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponseDto {

    private Long id;
    private String userName;

    public SignupResponseDto(User user){
        this.id = user.getId();
        this.userName = user.getUsername();
    }
}
