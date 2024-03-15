package com.sparta.todoapp.service.port;

import com.sparta.todoapp.entity.Reply;
import java.util.List;
import java.util.Map;

public interface ReplyService {

    Reply createReply(Reply reply);
    void deleteReply(Reply reply);
    Reply findById(Long id);
    List<Reply> findByScheduleId(Long scheduleId);
    Map<Long, String> getReplyList(List<Reply> replies);
}
