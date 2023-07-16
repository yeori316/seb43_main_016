package com.codestates.edusync.model.study.comment.controller;

import com.codestates.edusync.model.common.dto.CommonDto;
import com.codestates.edusync.model.member.service.MemberService;
import com.codestates.edusync.model.study.comment.dto.CommentDto;
import com.codestates.edusync.model.study.comment.mapper.CommentMapper;
import com.codestates.edusync.model.study.comment.service.CommentService;
import com.codestates.edusync.model.study.study.service.StudyService;
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
    private final CommentMapper mapper;
    private final CommentService service;
    private final MemberService memberService;
    private final StudyService studyService;

    /**
     * 댓글 등록
     * @param studyId
     * @param postDto
     * @return
     */
    @PostMapping("/{study-id}")
    public ResponseEntity<String> post(Authentication authentication,
                                       @PathVariable("study-id") @Positive Long studyId,
                                       @Valid @RequestBody CommentDto.Post postDto) {

        service.create(
                memberService.get(authentication.getName()),
                studyService.get(studyId),
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
    @PatchMapping("/{study-id}")
    public ResponseEntity<String> patch(Authentication authentication,
                                        @PathVariable("study-id") @Positive Long studyId,
                                        @Valid @RequestBody CommentDto.Patch patchDto) {

        service.update(
                memberService.get(authentication.getName()),
                studyService.get(studyId),
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
    public ResponseEntity<CommonDto.ResponsePage<List<CommentDto.Response>>> getList(
            Authentication authentication,
            @PathVariable("study-id") @Positive Long studyId,
            @RequestParam("page") @Positive Integer page,
            @RequestParam("size") @Positive Integer size) {

        return ResponseEntity.ok(
                service.getListDto(
                        studyService.get(studyId),
                        memberService.get(authentication.getName()),
                        page-1,
                        size)
        );
    }

    /**
     * 댓글 삭제
     * @param studyId
     * @param commentId
     * @return
     */
    @DeleteMapping("/{study-id}/{comment-id}")
    public ResponseEntity<String> delete(Authentication authentication,
                                         @PathVariable("study-id") @Positive Long studyId,
                                         @PathVariable("comment-id") @Positive Long commentId) {

        service.delete(
                memberService.get(authentication.getName()),
                studyService.get(studyId),
                commentId
        );

        return ResponseEntity.ok().build();
    }
}
