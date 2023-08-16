package com.codestates.edusync.model.study.comment.controller;

import com.codestates.edusync.model.study.comment.dto.CommentDto;
import com.codestates.edusync.model.study.comment.dto.CommentPageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface CommentControllerInterface {

    /**
     * 댓글 등록
     * @param enStudyId Encoded Study ID
     * @param postDto CommentPostDto
     * @return String
     */
    @PostMapping("/{study-id}")
    ResponseEntity<String> post(Authentication authentication,
                                @PathVariable("study-id") String enStudyId,
                                @Valid @RequestBody CommentDto.Post postDto);

    /**
     * 댓글 수정
     * @param authentication Authentication
     * @param enStudyId Encoded Study ID
     * @param patchDto CommentPatchDto
     * @return String
     */
    @PatchMapping("/{study-id}")
    ResponseEntity<String> patch(Authentication authentication,
                                 @PathVariable("study-id") String enStudyId,
                                 @Valid @RequestBody CommentDto.Patch patchDto);

    /**
     * 댓글 리스트 조회
     * @param authentication Authentication
     * @param enStudyId Encoded Study ID
     * @param enPage Encoded Page Number
     * @param enSize Encoded Page Size
     * @return CommentPage
     */
    @GetMapping("/{study-id}")
    ResponseEntity<CommentPageDto.ResponsePage<List<CommentDto.Response>>> getList(
            Authentication authentication,
            @PathVariable("study-id") String enStudyId,
            @RequestParam("p") String enPage,
            @RequestParam("s") String enSize);

    /**
     * 댓글 삭제
     * @param authentication Authentication
     * @param enStudyId Encoded Study ID
     * @param enCommentId Encoded Commnet ID
     * @return String
     */
    @DeleteMapping("/{study-id}/{comment-id}")
    ResponseEntity<String> delete(Authentication authentication,
                                  @PathVariable("study-id") String enStudyId,
                                  @PathVariable("comment-id") String enCommentId);
}
