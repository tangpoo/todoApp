package com.sparta.todoapp.service;

import com.sparta.todoapp.entity.Reply;
import com.sparta.todoapp.repository.ReplyRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    @Transactional
    public Reply createReply(Reply reply) {
        return replyRepository.save(reply);
    }

    @Transactional
    public void deleteReply(Reply reply) {
        replyRepository.delete(reply);
    }

    public Reply findById(Long id) {
        return replyRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다."));
    }

    public List<Reply> findByScheduleId(Long scheduleId) {
        return replyRepository.findAllByScheduleId(scheduleId);
    }

    public Map<Long, String> getReplyList(List<Reply> replies) {
        Map<Long, String> replyList = new LinkedHashMap<>();
        for (Reply reply : replies) {
            replyList.put(reply.getId(), reply.getContent());
        }

        return replyList;
    }
}
