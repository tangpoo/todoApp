package com.sparta.todoapp.message;

public class ScheduleMessage {

    private ScheduleMessage() {
    }

    public static final String CREATE_SCHEDULE_API = "스케줄 작성 API";
    public static final String CREATE_SCHEDULE_SUCCESS = "스케줄 작성 성공";

    public static final String GET_SCHEDULE_API = "스케줄 조회 API";
    public static final String GET_SCHEDULE_SUCCESS = "스케줄 작성 성공";

    public static final String SEARCH_SCHEDULE_API = "스케줄 목록 조회 API";
    public static final String SEARCH_SCHEDULE_SUCCESS = "스케줄 목록 조회 성공";

    public static final String PATCH_SCHEDULE_API = "스케줄 수정 API";
    public static final String PATCH_SCHEDULE_SUCCESS = "스케줄 수정 성공";

    public static final String PATCH_SCHEDULE_CHECK_API = "스케줄 체크 API";
    public static final String PATCH_SCHEDULE_CHECK_DESCRIPTION = "스케줄 완료, 비공개 여부 체크를 할 수 있습니다.";

    public static final String DELETE_SCHEDULE_API = "스케줄 삭제 API";
}
