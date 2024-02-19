package com.sparta.todoapp.dto.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
public class LoginRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
