//package com.sparta.todoapp.repository;
//
//import com.sparta.todoapp.dto.user.SignupRequestDto;
//import com.sparta.todoapp.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static com.sparta.todoapp.common.UserTest.TEST_USER;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("root")
//public class ScheduleRepositoryTest {
//
//    @Autowired
//    ScheduleRepository scheduleRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @BeforeEach
//    void setUp(){
//        userRepository.save(TEST_USER);
//    }
//
//    @Test
//    @DisplayName("생성일시 기준 내림차순 정렬 조회")
//    void findAll(){
//        //given
//
//    }
//}
