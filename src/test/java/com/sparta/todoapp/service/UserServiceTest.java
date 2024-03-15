//package com.sparta.todoapp.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//import com.sparta.todoapp.common.UserTest;
//import com.sparta.todoapp.dto.user.SignupRequestDto;
//import com.sparta.todoapp.dto.user.SignupResponseDto;
//import com.sparta.todoapp.entity.Member;
//import com.sparta.todoapp.entity.UserRoleEnum;
//import com.sparta.todoapp.jwt.JwtUtil;
//import com.sparta.todoapp.repository.MemberRepository;
//import java.util.Optional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest implements UserTest {
//
//    @InjectMocks
//    UserService userService;
//
//    @Mock
//    MemberRepository memberRepository;
//
//    @Mock
//    PasswordEncoder passwordEncoder;
//
//    @Mock
//    JwtUtil jwtUtil;
//
//    @Test
//    @Transactional
//    @DisplayName("회원가입 실패, 유저 중복")
//    void test1(){
//        //given
//        SignupRequestDto requestDto = new SignupRequestDto();
//        requestDto.setUsername(TEST_USER_NAME);
//        requestDto.setPassword(TEST_USER_PASSWORD);
//        requestDto.setEmail(TEST_USER_EMAIL);
//
//        String username = requestDto.getUsername();
//        String password = passwordEncoder.encode(requestDto.getPassword());
//
//        given(memberRepository.findByUsername(username)).willReturn(Optional.of(new Member()));
//
//        //when + then
//        assertThrows(DataIntegrityViolationException.class, () -> userService.signup(requestDto)) ;
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("회원가입 실패, email 중복")
//    void test2(){
//        //given
//        SignupRequestDto requestDto = new SignupRequestDto();
//        requestDto.setUsername(TEST_USER_NAME);
//        requestDto.setPassword(TEST_USER_PASSWORD);
//        requestDto.setEmail(TEST_USER_EMAIL);
//
//        String username = requestDto.getUsername();
//        String password = passwordEncoder.encode(requestDto.getPassword());
//
//        given(memberRepository.findByUsername(username)).willReturn(Optional.empty());
//        given(memberRepository.findByEmail(requestDto.getEmail())).willReturn(Optional.of(new Member()));
//
//        //when + then
//        assertThrows(DataIntegrityViolationException.class, () -> userService.signup(requestDto)) ;
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("회원가입 실패, 관리자 암호 실패")
//    void test3(){
//        //given
//        SignupRequestDto requestDto = new SignupRequestDto();
//        requestDto.setUsername(TEST_USER_NAME);
//        requestDto.setPassword(TEST_USER_PASSWORD);
//        requestDto.setEmail(TEST_USER_EMAIL);
//        requestDto.setAdmin(true);
//        requestDto.setAdminToken(TEST_FAIL_TOKEN);
//
//        String username = requestDto.getUsername();
//        String password = passwordEncoder.encode(requestDto.getPassword());
//
//        given(memberRepository.findByUsername(username)).willReturn(Optional.empty());
//        given(memberRepository.findByEmail(requestDto.getEmail())).willReturn(Optional.empty());
//
//        //when + then
//        assertThrows(AccessDeniedException.class, () -> userService.signup(requestDto));
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("회원가입 성공")
//    void test4(){
//        //given
//        SignupRequestDto requestDto = new SignupRequestDto();
//        requestDto.setUsername(TEST_USER_NAME);
//        requestDto.setPassword(TEST_USER_PASSWORD);
//        requestDto.setEmail(TEST_USER_EMAIL);
//        requestDto.setAdmin(true);
//        requestDto.setAdminToken(TEST_TOKEN);
//
//        String username = requestDto.getUsername();
//        String password = passwordEncoder.encode(requestDto.getPassword());
//
//        Member member = Member.builder()
//                .username(username)
//                .email("test_email")
//                .password(password)
//                .role(UserRoleEnum.USER)
//                .build();
//
//        given(memberRepository.findByUsername(username)).willReturn(Optional.empty());
//        given(memberRepository.findByEmail(requestDto.getEmail())).willReturn(Optional.empty());
//        given(memberRepository.save(any(Member.class))).willReturn(member);
//
//        //when
//        SignupResponseDto responseDto = userService.signup(requestDto);
//
//        //then
//        assertEquals(requestDto.getUsername(), responseDto.getUserName());
//        assertEquals(member.getUsername(), responseDto.getUserName());
//    }
//
////    @Test
////    @Transactional
////    @DisplayName("로그인 회원 찾기 실패")
////    void test5() {
////        //given
////        LoginRequestDto requestDto = new LoginRequestDto();
////        requestDto.setUsername(TEST_USER_NAME);
////        requestDto.setPassword(TEST_USER_PASSWORD);
////
////        Member member = Member.builder()
////                .username(requestDto.getUsername())
////                .email(TEST_USER_EMAIL)
////                .password(requestDto.getPassword())
////                .role(UserRoleEnum.USER)
////                .build();
////
////        given(memberRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.empty());
////
////        //when + then
////        assertThrows(NoSuchElementException.class, () -> userService.login(requestDto));
////    }
////
////    @Test
////    @Transactional
////    @DisplayName("로그인 비밀번호 틀림")
////    void test6() {
////        //given
////        LoginRequestDto requestDto = new LoginRequestDto();
////        requestDto.setUsername(TEST_USER_NAME);
////        requestDto.setPassword(TEST_USER_PASSWORD);
////
////        Member member = Member.builder()
////                .username(requestDto.getUsername())
////                .email(TEST_USER_EMAIL)
////                .password(ANOTHER_PREFIX + TEST_USER_PASSWORD)
////                .role(UserRoleEnum.USER)
////                .build();
////
////        given(memberRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.of(member));
////
////        //when + then
////        assertThrows(AccessDeniedException.class, () -> userService.login(requestDto));
////    }
//
////    @Test
////    @Transactional
////    @DisplayName("로그인 성공")
////    void test7() {
////        //given
////        LoginRequestDto requestDto = new LoginRequestDto();
////        requestDto.setUsername(TEST_USER_NAME);
////        requestDto.setPassword(TEST_USER_PASSWORD);
////
////        User user = User.builder()
////                .username(requestDto.getUsername())
////                .email(TEST_USER_EMAIL)
////                .password(passwordEncoder.encode(requestDto.getPassword()))
////                .role(UserRoleEnum.USER)
////                .build();
////
////        given(userRepository.findByUsername(requestDto.getUsername())).willReturn(Optional.of(user));
////        given(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).willReturn(true);
////        //when
////        String token = userService.login(requestDto);
////
////        //then
////
////    }
//}
