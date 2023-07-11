package com.codestates.edusync.model.study.comment.controller;

import com.codestates.edusync.model.common.dto.CommonDto;
import com.codestates.edusync.model.study.comment.dto.CommentDto;
import com.codestates.edusync.model.study.comment.entity.Comment;
import com.codestates.edusync.model.study.comment.mapper.CommentMapper;
import com.codestates.edusync.model.study.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
@Validated
public class CommentController {
    private final CommentMapper mapper;
    private final CommentService service;

    /**
     * 댓글 등록
     * @param studyId
     * @param postDto
     * @return
     */
    @PostMapping("/{study-id}")
    public ResponseEntity post(Authentication authentication,
                               @PathVariable("study-id") @Positive Long studyId,
                               @Valid @RequestBody CommentDto.Post postDto) {

        service.create(
                authentication.getName(),
                studyId,
                mapper.commentPostToComment(postDto)
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
                                @Valid @RequestBody CommentDto.Patch patchDto) {

        service.update(
                authentication.getName(),
                studyId, commentId,
                mapper.commentPatchToComment(patchDto)
        );

        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 리스트 조회
     * @param studyId
     * @return
     */
    @GetMapping("/{study-id}")
    public ResponseEntity getList(Authentication authentication,
                                  @PathVariable("study-id") @Positive Long studyId,
                                  @RequestParam("page") @Positive Integer page,
                                  @RequestParam("size") @Positive Integer size) {

        Page<Comment> commentPage = service.getList(studyId, page-1, size);

        return ResponseEntity.ok(
                new CommonDto.ResponsePage<>(
                        mapper.commentsToResponesList(commentPage.getContent(), authentication.getName()),
                        commentPage
                )
        );
    }

    /**
     * 댓글 삭제
     * @param studyId
     * @param commentId
     * @return
     */
    @DeleteMapping("/{study-id}/{comment-id}")
    public ResponseEntity delete(Authentication authentication,
                                 @PathVariable("study-id") @Positive Long studyId,
                                 @PathVariable("comment-id") @Positive Long commentId) {

        service.delete(
                authentication.getName(),
                studyId, commentId
        );

        return ResponseEntity.ok().build();
    }
}
