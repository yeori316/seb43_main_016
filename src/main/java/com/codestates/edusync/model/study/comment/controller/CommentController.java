package com.codestates.edusync.model.study.comment.controller;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.common.util.ObfuscationUtil;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.service.MemberService;
import com.codestates.edusync.model.study.comment.dto.CommentDto;
import com.codestates.edusync.model.study.comment.dto.CommentPageDto;
import com.codestates.edusync.model.study.comment.entity.Comment;
import com.codestates.edusync.model.study.comment.mapper.CommentMapper;
import com.codestates.edusync.model.study.comment.service.CommentService;
import com.codestates.edusync.model.study.study.entity.Study;
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
     * @param enStudyId Encoded Study ID
     * @param postDto CommentPostDto
     * @return String
     */
    @PostMapping("/{study-id}")
    public ResponseEntity<String> post(Authentication authentication,
                                       @PathVariable("study-id") String enStudyId,
                                       @Valid @RequestBody CommentDto.Post postDto) {
        Long studyId = verifyId(enStudyId);
        Member member = getMember(authentication);
        Study study = getStudy(studyId);
        Comment comment = mapper.commentPostToComment(postDto, member, study);
        service.create(comment);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 댓글 수정
     * @param authentication Authentication
     * @param enStudyId Encoded Study ID
     * @param patchDto CommentPatchDto
     * @return String
     */
    @PatchMapping("/{study-id}")
    public ResponseEntity<String> patch(Authentication authentication,
                                        @PathVariable("study-id") String enStudyId,
                                        @Valid @RequestBody CommentDto.Patch patchDto) {
        Long studyId = verifyId(enStudyId);
        Member member = getMember(authentication);
        Study study = getStudy(studyId);
        Comment comment = mapper.commentPatchToComment(patchDto);
        service.update(member, study, comment);

        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 리스트 조회
     * @param authentication Authentication
     * @param enStudyId Encoded Study ID
     * @param enPage Encoded Page Number
     * @param enSize Encoded Page Size
     * @return CommentPage
     */
    @GetMapping("/{study-id}")
    public ResponseEntity<CommentPageDto.ResponsePage<List<CommentDto.Response>>> getList(
            Authentication authentication,
            @PathVariable("study-id") String enStudyId,
            @RequestParam("p") String enPage,
            @RequestParam("s") String enSize) {

        Long studyId = verifyId(enStudyId);
        int page = Integer.parseInt(getDecoded(enPage));
        int size = Integer.parseInt(getDecoded(enSize));

        Member member = getMember(authentication);
        Study study = getStudy(studyId);

        CommentPageDto.ResponsePage<List<CommentDto.Response>> commentPage =
                service.getListDto(study, member, page-1, size);

        return ResponseEntity.ok(commentPage);
    }

    /**
     * 댓글 삭제
     * @param authentication Authentication
     * @param enStudyId Encoded Study ID
     * @param enCommentId Encoded Commnet ID
     * @return String
     */
    @DeleteMapping("/{study-id}/{comment-id}")
    public ResponseEntity<String> delete(Authentication authentication,
                                         @PathVariable("study-id") String enStudyId,
                                         @PathVariable("comment-id") String enCommentId) {
        Long studyId = verifyId(enStudyId);
        Long commentId = verifyId(enCommentId);

        Member member = getMember(authentication);
        Study study = getStudy(studyId);

        service.delete(member, study, commentId);

        return ResponseEntity.ok().build();
    }

    /**
     * Base64 Decoder
     * @param message Encoded Message
     * @return Decoded Message
     */
    private String getDecoded(String message) {
        return obfuscationUtil.getDecoded(message);
    }

    /**
     * Study ID 검증
     * @param enStudyId Encoded Study ID
     * @return Study ID
     */
    private Long verifyId(String enStudyId) {
        long studyId = Long.parseLong(getDecoded(enStudyId));

        if (studyId < 1) {
            throw new BusinessLogicException(ExceptionCode.STUDY_NOT_FOUND);
        }
        return studyId;
    }

    /**
     * 멤버 조회
     * @param authentication Authentication
     * @return Member
     */
    private Member getMember(Authentication authentication) {
        return memberService.get(authentication.getName());
    }

    /**
     * 스터디 조회
     * @param studyId Study ID
     * @return Study
     */
    private Study getStudy(Long studyId) {
        return studyService.get(studyId);
    }


}
