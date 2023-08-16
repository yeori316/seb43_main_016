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

import static com.codestates.edusync.exception.ExceptionCode.COMMENT_NOT_FOUND;
import static com.codestates.edusync.exception.ExceptionCode.INVALID_PERMISSION;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CommentService implements CommentServiceInterface {
    private final CommentRepository repository;
    private final CommentDtoMapper dtoMapper;

    public void create(Comment comment) {
        repository.save(comment);
    }

    public void update(Member member, Study study, Comment comment) {
        Comment findComment = verifyComment(member, study, comment.getId());
        Optional.ofNullable(comment.getContent()).ifPresent(findComment::setContent);
        repository.save(findComment);
    }

    @Transactional(readOnly = true)
    public CommentPageDto.ResponsePage<List<CommentDto.Response>> getListDto(
            Study study, Member member, Integer page, Integer size) {

        Long studyId = study.getId();
        String email = member.getEmail();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Comment> commentPages = repository.findAllByStudyId(studyId, pageRequest);
        List<Comment> commentList = commentPages.getContent();
        List<CommentDto.Response> commentResponseList = dtoMapper.commentsToResponesList(commentList, email);

        return new CommentPageDto.ResponsePage<>(commentResponseList, commentPages);
    }

    public void delete(Member member, Study study, Long commentId) {
        Comment findComment = verifyComment(member, study, commentId);
        repository.delete(findComment);
    }

    /**
     * Comment 검증
     * @param member Member
     * @param study Study
     * @param commentId Comment ID
     * @return Comment
     */
    private Comment verifyComment(Member member, Study study, Long commentId) {
        Comment findComment = repository.findById(commentId)
                .orElseThrow( () -> new BusinessLogicException(COMMENT_NOT_FOUND));

        if( !findComment.getStudy().getId().equals(study.getId()) ) {
            throw new BusinessLogicException(COMMENT_NOT_FOUND);
        }

        if( !findComment.getStudy().getLeader().getId().equals(member.getId()) &&
                !findComment.getMember().getId().equals(member.getId()) ) {
            throw new BusinessLogicException(INVALID_PERMISSION);
        }

        return findComment;
    }
}
