package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Reply;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findAllByScheduleId(Long id);
}
