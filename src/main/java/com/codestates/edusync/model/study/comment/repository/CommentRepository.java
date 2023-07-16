package com.codestates.edusync.model.study.comment.repository;

import com.codestates.edusync.model.study.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByStudyId(Long studyId, PageRequest pageRequest);
}
