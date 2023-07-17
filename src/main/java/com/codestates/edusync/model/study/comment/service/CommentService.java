package com.codestates.edusync.model.study.comment.service;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.comment.dto.CommentDto;
import com.codestates.edusync.model.study.comment.dto.CommentPageDto;
import com.codestates.edusync.model.study.comment.entity.Comment;
import com.codestates.edusync.model.study.comment.mapper.CommentDtoMapper;
import com.codestates.edusync.model.study.comment.repository.CommentRepository;
import com.codestates.edusync.model.study.study.entity.Study;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.codestates.edusync.exception.ExceptionCode.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository repository;
    private final CommentDtoMapper dtoMapper;

    /**
     * 댓글 등록
     * @param studyId
     * @param comment
     * @param email
     */
    public void create(Member member, Study study, Comment comment) {

        comment.setMember(member);
        comment.setStudy(study);

        repository.save(comment);
    }

    /**
     * 댓글 수정
     * @param email
     * @param studyId
     * @param comment
     * @return
     */
    public Comment update(Member member, Study study, Comment comment) {

        Comment findComment = repository.findById(comment.getId()).orElseThrow( () ->
                        new BusinessLogicException(STUDYGROUP_POST_COMMENT_NOT_FOUND));

        if(!findComment.getStudy().getId().equals(study.getId())) {
            throw new BusinessLogicException(STUDYGROUP_POST_COMMENT_NOT_MATCHED);
        }

        if(!findComment.getStudy().getLeader().getId().equals(member.getId()) &&
                !findComment.getMember().getId().equals(member.getId()) ) {
            throw new BusinessLogicException(STUDYGROUP_POST_COMMENT_NOT_ALLOWED);
        }

        Optional.ofNullable(comment.getContent()).ifPresent(findComment::setContent);
        
        return repository.save(findComment);
    }

    /**
     * 댓글 리스트 조회
     * @param studyId
     * @return
     */
    @Transactional(readOnly = true)
    public CommentPageDto.ResponsePage<List<CommentDto.Response>> getListDto(Study study, Member member, Integer page, Integer size) {

        Page<Comment> commentPages =
                repository.findAllByStudyId(study.getId(), PageRequest.of(page, size, Sort.by("id").descending()));

        return new CommentPageDto.ResponsePage<>(
                dtoMapper.commentsToResponesList(commentPages.getContent(), member.getEmail()),
                commentPages);
    }


    /**
     * 댓글 삭제
     * @param studyId
     * @param commentId
     * @param email
     */
    public void delete(Member member, Study study, Long commentId) {

        Comment findComment = repository.findById(commentId).orElseThrow( () ->
                        new BusinessLogicException(STUDYGROUP_POST_COMMENT_NOT_FOUND));

        if( !findComment.getStudy().getId().equals(study.getId()) ) {
            throw new BusinessLogicException(STUDYGROUP_POST_COMMENT_NOT_MATCHED);
        }

        if( !findComment.getStudy().getLeader().getId().equals(member.getId()) &&
                !findComment.getMember().getId().equals(member.getId()) ) {
            throw new BusinessLogicException(STUDYGROUP_POST_COMMENT_NOT_ALLOWED);
        }

        repository.delete(findComment);
    }
}
