package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByScheduleId(Long id);
}
