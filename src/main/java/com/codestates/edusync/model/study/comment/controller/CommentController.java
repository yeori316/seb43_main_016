package com.codestates.edusync.model.study.comment.controller;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.common.util.ObfuscationUtil;
import com.codestates.edusync.model.member.service.MemberService;
import com.codestates.edusync.model.study.comment.dto.CommentDto;
import com.codestates.edusync.model.study.comment.dto.CommentPageDto;
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
    private final ObfuscationUtil obfuscationUtil;

    /**
     * 댓글 등록
     * @param studyId
     * @param postDto
     * @return
     */
    @PostMapping("/{study-id}")
    public ResponseEntity<String> post(Authentication authentication,
                                       @PathVariable("study-id") String enStudyId,
                                       @Valid @RequestBody CommentDto.Post postDto) {

        Long studyId = verifyId(enStudyId);

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
                                        @PathVariable("study-id") String enStudyId,
                                        @Valid @RequestBody CommentDto.Patch patchDto) {

        Long studyId = verifyId(enStudyId);

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
    public ResponseEntity<CommentPageDto.ResponsePage<List<CommentDto.Response>>> getList(
            Authentication authentication,
            @PathVariable("study-id") String enStudyId,
            @RequestParam("p") String enPage,
            @RequestParam("s") String enSize) {

        Long studyId = verifyId(enStudyId);
        Integer page = Integer.parseInt(getDecoded(enPage));
        Integer size = Integer.parseInt(getDecoded(enSize));

        return ResponseEntity.ok(
                service.getListDto(
                        studyService.get(studyId),
                        memberService.get(authentication.getName()),
                        page-1,
                        size
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
    public ResponseEntity<String> delete(Authentication authentication,
                                         @PathVariable("study-id") String enStudyId,
                                         @PathVariable("comment-id") String enCommentId) {

        Long studyId = verifyId(enStudyId);
        Long commentId = verifyId(enCommentId);

        service.delete(
                memberService.get(authentication.getName()),
                studyService.get(studyId),
                commentId
        );

        return ResponseEntity.ok().build();
    }

    public String getDecoded(String message) {
        return obfuscationUtil.getDecoded(message);
    }

    private Long verifyId(String enStudyId) {
        Long studyId = Long.parseLong(getDecoded(enStudyId));

        if (studyId < 1) {
            throw new BusinessLogicException(ExceptionCode.STUDY_NOT_FOUND);
        }
        return studyId;
    }
}
