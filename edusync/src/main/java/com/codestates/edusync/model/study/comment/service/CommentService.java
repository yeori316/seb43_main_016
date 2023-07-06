package com.codestates.edusync.model.study.comment.service;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.model.common.utils.VerifyStudygroupUtils;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.service.MemberService;
import com.codestates.edusync.model.study.comment.entity.Comment;
import com.codestates.edusync.model.study.comment.repository.CommentRepository;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.codestates.edusync.exception.ExceptionCode.*;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository repository;
    private final MemberService memberService;
    private final StudyService studyService;

    /**
     * 댓글 등록
     * @param studyId
     * @param comment
     * @param email
     */
    public void create(String email, Long studyId, Comment comment) {

        comment.setMember(getMember(email));
        comment.setStudy(getStudy(studyId));

        repository.save(comment);
    }

    /**
     * 댓글 수정
     * @param email
     * @param studyId
     * @param commentId
     * @param patchComment
     * @return
     */
    public Comment update(String email, Long studyId, Long commentId, Comment patchComment) {

        Comment findComment = repository.findById(commentId).orElseThrow( () ->
                        new BusinessLogicException(STUDYGROUP_POST_COMMENT_NOT_FOUND));


        if( !findComment.getStudy().getId().equals(studyId) ) {
            throw new BusinessLogicException(STUDYGROUP_POST_COMMENT_NOT_MATCHED);
        }

        if( !findComment.getStudy().getLeader().getId().equals(getMember(email).getId()) &&
                !findComment.getMember().getId().equals(getMember(email).getId()) ) {
            throw new BusinessLogicException(STUDYGROUP_POST_COMMENT_NOT_ALLOWED);
        }

        Optional.ofNullable(patchComment.getContent())
                .ifPresent(findComment::setContent);
        
        return repository.save(findComment);
    }


    /**
     * 댓글 리스트 조회
     * @param studygroupId
     * @return
     */
    @Transactional(readOnly = true)
    public List<Comment> getAll(Long studygroupId) {
        return repository.findAllByStudygroupId(studygroupId);
    }


    /**
     * 댓글 삭제
     * @param studyId
     * @param commentId
     * @param loginMember
     */
    public void delete(String email, Long studyId, Long commentId) {

        Comment findComment = repository.findById(commentId).orElseThrow( () ->
                        new BusinessLogicException(STUDYGROUP_POST_COMMENT_NOT_FOUND));


        if( !findComment.getStudy().getId().equals(studyId) ) {
            throw new BusinessLogicException(STUDYGROUP_POST_COMMENT_NOT_MATCHED);
        }

        if( !findComment.getStudy().getLeader().getId().equals(getMember(email).getId()) &&
                !findComment.getMember().getId().equals(getMember(email).getId()) ) {
            throw new BusinessLogicException(STUDYGROUP_POST_COMMENT_NOT_ALLOWED);
        }

        repository.delete(findComment);
    }








    /**
     * 회원 조회
     * @param email
     * @return
     */
    public Member getMember(String email) {
        return memberService.get(email);
    }

    /**
     * 스터디 조회
     * @param studyId
     * @return
     */
    public Study getStudy(Long studyId) {
        return studyService.get(studyId);
    }
}
