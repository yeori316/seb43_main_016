package com.codestates.edusync.model.study.comment.service;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.comment.dto.CommentDto;
import com.codestates.edusync.model.study.comment.dto.CommentPageDto;
import com.codestates.edusync.model.study.comment.entity.Comment;
import com.codestates.edusync.model.study.study.entity.Study;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentServiceInterface {

    /**
     * 댓글 등록
     * @param comment Comment
     */
    void create(Comment comment);

    /**
     * 댓글 수정
     * @param member  Member
     * @param study   Study
     * @param comment Comment
     */
    void update(Member member, Study study, Comment comment);

    /**
     * 댓글 리스트 조회
     * @param study  Study
     * @param member Member
     * @param page   Page
     * @param size   Size
     * @return Commnet Page
     */
    @Transactional(readOnly = true)
    CommentPageDto.ResponsePage<List<CommentDto.Response>> getListDto(
            Study study,
            Member member,
            Integer page,
            Integer size);

    /**
     * 댓글 삭제
     * @param member    Member
     * @param study     Study
     * @param commentId Comment ID
     */
    void delete(Member member, Study study, Long commentId);
}
