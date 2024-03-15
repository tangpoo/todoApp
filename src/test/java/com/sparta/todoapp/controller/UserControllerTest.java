//package com.sparta.todoapp.controller;
//
//import static org.mockito.BDDMockito.given;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sparta.todoapp.common.UserTest;
//import com.sparta.todoapp.dto.user.SignupRequestDto;
//import com.sparta.todoapp.dto.user.SignupResponseDto;
//import com.sparta.todoapp.entity.Member;
//import com.sparta.todoapp.entity.UserRoleEnum;
//import com.sparta.todoapp.service.UserService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//
//@WebMvcTest(UserController.class)
//public class UserControllerTest implements UserTest {
//
//    @MockBean
//    UserService userService;
//
//    @Autowired
//    MockMvc mvc;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("회원가입 테스트")
//    @WithMockUser
//    void test1() throws Exception {
//        //given
//        SignupRequestDto requestDto = new SignupRequestDto();
//        requestDto.setUsername(TEST_USER_NAME);
//        requestDto.setPassword(TEST_USER_PASSWORD);
//        requestDto.setEmail(TEST_USER_EMAIL);
//
//        Member member = Member.builder()
//                .username(TEST_USER_NAME)
//                .password(TEST_USER_PASSWORD)
//                .email(TEST_USER_EMAIL)
//                .role(UserRoleEnum.USER)
//                .build();
//        SignupResponseDto responseDto = new SignupResponseDto(member);
//
//        given(userService.signup(requestDto)).willReturn(responseDto);
//
//
//        //when + then
//        mvc.perform(post("/api/users/signup")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(jsonPath("$.message").value("회원 가입 성공"));
//    }
//}
