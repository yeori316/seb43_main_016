package com.codestates.edusync.model.study.comment.repository;

import com.codestates.edusync.model.study.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByStudygroupId(Long studygroupId);

    void deleteAllByStudygroupId(Long studygroupId);
}
