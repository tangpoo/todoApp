package com.sparta.todoapp.entity;

import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleEntityTest {
    Schedule schedule;

    @BeforeEach
    void setup(){
        schedule = Schedule.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .user(new User())
                .build();
    }

    @Test
    @Transactional
    @DisplayName("update 테스트")
    void test1(){
        //given
        String newTitle = "test_update_title";
        String newContent = "test_update_content";

        ScheduleRequestDto requestDto = new ScheduleRequestDto(newTitle, newContent);

        //when
        schedule.update(requestDto);

        //then
        assertEquals(newTitle, requestDto.getTitle());
        assertEquals(newContent, requestDto.getContent());
    }

    @Test
    @Transactional
    @DisplayName("changeIsCompleted 테스트")
    void test2(){
        //given
        boolean isCompleted = true;

        //when
        schedule.changeIsCompleted(isCompleted);

        //then
        assertTrue(schedule.isCompleted());
    }
}
