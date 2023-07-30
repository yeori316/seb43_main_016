package com.codestates.edusync.model.study.study.controller;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.common.util.ObfuscationUtil;
import com.codestates.edusync.model.member.service.MemberService;
import com.codestates.edusync.model.study.likes.service.LikesService;
import com.codestates.edusync.model.study.study.dto.StudyDto;
import com.codestates.edusync.model.study.study.dto.StudyPageDto;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.study.mapper.StudyMapper;
import com.codestates.edusync.model.study.study.service.StudyService;
import com.codestates.edusync.model.study.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Transactional
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/study")
@Validated
public class StudyController {
    private final StudyMapper mapper;
    private final StudyService service;
    private final MemberService memberService;
    private final TagService tagService;
    private final LikesService likesService;
    private final ObfuscationUtil obfuscationUtil;

    /**
     * 스터디 등록
     * @param authentication Authentication
     * @param postDto StudyPostDto
     * @return URI
     */
    @PostMapping
    public ResponseEntity<String> post(Authentication authentication,
                                       @Valid @RequestBody StudyDto.Post postDto) {

        Study study = mapper.studyPostToStudy(
                postDto,
                memberService.get(authentication.getName()),
                tagService.getList(postDto.getTags())
        );

        service.create(study);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 스터디 수정
     * @param authentication 토큰
     * @param studyId Long
     * @param patchDto StudyPatchDto
     * @return String
     */
    @PatchMapping("/{study-id}")
    public ResponseEntity<String> patch(Authentication authentication,
                                        @PathVariable("study-id") String enStudyId,
                                        @Valid @RequestBody StudyDto.Patch patchDto) {

        Long studyId = verifyId(enStudyId);

        service.update(
                mapper.studyPatchToStudy(patchDto, studyId, tagService.getList(patchDto.getTags())),
                memberService.get(authentication.getName()).getEmail()
        );

        return ResponseEntity.ok().build();
    }

    /**
     * 스터디 이미지 수정
     * @param authentication
     * @param studyId
     * @param image
     * @return
     */
    @PatchMapping("/{study-id}/image")
    public ResponseEntity<String> patchImage(Authentication authentication,
                                             @PathVariable("study-id") String enStudyId,
                                             @RequestPart(value="image") MultipartFile image) {

        Long studyId = verifyId(enStudyId);

        service.updateImage(
                studyId,
                memberService.get(authentication.getName()).getEmail(),
                image
        );

        return ResponseEntity.ok().build();
    }

    /**
     * 스터디 모집 상태 수정
     * @param authentication
     * @param studyId
     * @return
     */
    @PatchMapping("/{study-id}/status")
    public ResponseEntity<StudyDto.ResponseStatus> patchStatus(Authentication authentication,
                                                               @PathVariable("study-id") String enStudyId) {

        Long studyId = verifyId(enStudyId);

        return ResponseEntity.ok(
                mapper.studyStatus(
                        service.updateStatus(
                                studyId,
                                memberService.get(authentication.getName()).getEmail()
                        )
                )
        );
    }

    /**
     * 스터디 리더 수정
     * @param authentication
     * @param studyId
     * @param patchLeader
     * @return
     */
    @PatchMapping("/{study-id}/leader")
    public ResponseEntity<String> patchLeader(Authentication authentication,
                                              @PathVariable("study-id") String enStudyId,
                                              @RequestBody StudyDto.PatchLeader patchLeaderDto) {

        Long studyId = verifyId(enStudyId);

        service.updateLeader(
                studyId,
                memberService.get(authentication.getName()).getEmail(),
                patchLeaderDto.getNickName()
        );

        return ResponseEntity.ok().build();
    }

    /**
     * 스터디 조회
     * @param authentication
     * @param studyId
     * @return
     */
    @GetMapping("/{study-id}")
    public ResponseEntity<StudyDto.Response> get(Authentication authentication,
                                                 @PathVariable("study-id") String enStudyId) {

        Long studyId = verifyId(enStudyId);

        return ResponseEntity.ok(
                service.getDto(
                        studyId,
                        authentication.getName()
                )
        );
    }

    /**
     * 스터디 리스트
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<StudyPageDto.ResponsePage<List<StudyDto.Summary>>> getList(
            @RequestParam("p") String enPage,
            @RequestParam("s") String enSize,
            @RequestParam(value = "sort", required = false) String sort){

        Integer page = Integer.parseInt(getDecoded(enPage));
        Integer size = Integer.parseInt(getDecoded(enSize));

        return ResponseEntity.ok(
                service.getPageDto(page-1, size, sort)
        );
    }

    /**
     * 리더로 운영 중인 스터디 리스트 조회
     * @param authentication
     * @return
     */
    @GetMapping("/leader/list")
    public ResponseEntity<StudyPageDto.ResponseList<List<StudyDto.Summary>>> getLeaderList(
            Authentication authentication) {

        return ResponseEntity.ok(
                service.getLeaderStudyListDto(authentication.getName())
        );
    }

    /**
     * 가입 신청된 | 가입된 스터디 리스트 조회
     * @param authentication
     * @return
     */
    @GetMapping("/join/list")
    public ResponseEntity<StudyPageDto.ResponseList<List<StudyDto.Summary>>> getJoinList(
            Authentication authentication,
            @RequestParam("m") String enIsMember) {

        Boolean isMember = Boolean.parseBoolean(getDecoded(enIsMember));

        return ResponseEntity.ok(
                service.getJoinListDto(authentication.getName(), isMember)
        );
    }

    /**
     * 스터디 좋아요
     * @param authentication
     * @param studyId
     * @return
     */
    @PatchMapping("/{study-id}/likes")
    public Long patchLikes(Authentication authentication,
                          @PathVariable("study-id") String enStudyId) {

        Long studyId = verifyId(enStudyId);

        return likesService.patch(
                memberService.get(authentication.getName()),
                service.get(studyId)
        );
    }

    /**
     * 태그 스터디 검색
     * @param tag
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<StudyPageDto.ResponseList<List<StudyDto.Summary>>> get(
            @RequestParam("t") String enTag) {

        String tag = String.valueOf(getDecoded(enTag));

        return ResponseEntity.ok(
                tagService.search(tag)
        );
    }

    /**
     * 스터디 삭제
     * @param authentication
     * @param studyId
     * @return
     */
    @DeleteMapping("/{study-id}")
    public ResponseEntity<String> delete(Authentication authentication,
                                         @PathVariable("study-id") String enStudyId) {

        Long studyId = verifyId(enStudyId);

        service.delete(
                studyId,
                memberService.get(authentication.getName()).getEmail()
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