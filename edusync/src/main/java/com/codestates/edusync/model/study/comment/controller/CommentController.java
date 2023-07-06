package com.codestates.edusync.model.study.comment.controller;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.comment.dto.CommentPostDto;
import com.codestates.edusync.model.study.comment.entity.Comment;
import com.codestates.edusync.model.study.comment.mapper.CommentMapper;
import com.codestates.edusync.model.study.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
@Validated
public class CommentController {
    private final CommentService service;
    private final CommentMapper mapper;

    /**
     * 댓글 등록
     * @param studyId
     * @param postDto
     * @return
     */
    @PostMapping("/{study-id}")
    public ResponseEntity post(Authentication authentication,
                               @PathVariable("study-id") @Positive Long studyId,
                               @Valid @RequestBody CommentPostDto.Post postDto) {

        service.create(
                authentication.getName(),
                studyId,
                mapper.studygroupPostCommentPostDtoToStudygroupPostComment(postDto)
        );

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 댓글 수정
     * @param studyId
     * @param commentId
     * @param patchDto
     * @return
     */
    @PatchMapping("/{study-id}/{comment-id}")
    public ResponseEntity patch(Authentication authentication,
                                @PathVariable("study-id") @Positive Long studyId,
                                @PathVariable("comment-id") @Positive Long commentId,
                                @Valid @RequestBody CommentPostDto.Patch patchDto) {

        service.update(
                authentication.getName(),
                studyId, commentId,
                mapper.studygroupPostCommentPatchDtoToStudygroupPostComment(patchDto)
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 댓글 리스트 조회
     * @param studyId
     * @return
     */
    @GetMapping("/{study-id}")
    public ResponseEntity getStudygroupPostComment(Authentication authentication,
                                                   @PathVariable("study-id") @Positive Long studyId) {

        Member loginMember = service.getMember(authentication.getName());

        List<Comment> findComments = service.getAll(studyId);

        return new ResponseEntity<>(
                mapper.studygroupPostCommentToStudygroupPostCommentResponseDtos(findComments, loginMember.getEmail()),
                HttpStatus.OK
        );
    }

    /**
     * 댓글 삭제
     * @param studyId
     * @param commentId
     * @return
     */
    @DeleteMapping("/{study-id}/{comment-id}")
    public ResponseEntity deleteStudygroupPostComment(Authentication authentication,
                                                      @PathVariable("study-id") @Positive Long studyId,
                                                      @PathVariable("comment-id") @Positive Long commentId) {

        service.delete(
                authentication.getName(),
                studyId, commentId
        );

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
