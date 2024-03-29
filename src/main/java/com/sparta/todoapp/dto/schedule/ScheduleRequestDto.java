package com.sparta.todoapp.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleRequestDto {

    //    @NotBlank
//    @Size(max = 50)
    private String title;

    //    @NotBlank
//    @Size(max = 1024)
    private String content;
}
