package com.codestates.edusync.model.study.study.controller;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.common.util.ObfuscationUtil;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.service.MemberServiceInterface;
import com.codestates.edusync.model.study.likes.service.LikesServiceInterface;
import com.codestates.edusync.model.study.study.dto.StudyDto;
import com.codestates.edusync.model.study.study.dto.StudyPageDto;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.study.mapper.StudyMapper;
import com.codestates.edusync.model.study.study.service.StudyServiceInterface;
import com.codestates.edusync.model.study.tag.entity.Tag;
import com.codestates.edusync.model.study.tag.service.TagServiceInterface;
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
import java.util.List;

@Transactional
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/study")
@Validated
public class StudyController implements StudyControllerInterface {
    private final StudyMapper mapper;
    private final StudyServiceInterface service;
    private final MemberServiceInterface memberService;
    private final TagServiceInterface tagService;
    private final LikesServiceInterface likesService;
    private final ObfuscationUtil obfuscationUtil;

    @PostMapping
    public ResponseEntity<String> post(Authentication authentication,
                                       @Valid @RequestBody StudyDto.Post postDto) {
        Member member = getMember(authentication);
        List<Tag> tagList = getTagList(postDto.getTags());
        Study study = mapper.studyPostToStudy(postDto, member, tagList);
        service.create(study);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{study-id}")
    public ResponseEntity<String> patch(Authentication authentication,
                                        @PathVariable("study-id") String enStudyId,
                                        @Valid @RequestBody StudyDto.Patch patchDto) {
        Long studyId = verifyId(enStudyId);
        Member member = getMember(authentication);
        List<Tag> tagList = getTagList(patchDto.getTags());
        Study study = mapper.studyPatchToStudy(patchDto, studyId, tagList);
        service.update(study, member.getEmail());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{study-id}/image")
    public ResponseEntity<String> patchImage(Authentication authentication,
                                             @PathVariable("study-id") String enStudyId,
                                             @RequestPart(value="image") MultipartFile image) {
        Long studyId = verifyId(enStudyId);
        Member member = getMember(authentication);
        service.updateImage(studyId, member.getEmail(), image);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{study-id}/status")
    public ResponseEntity<StudyDto.ResponseStatus> patchStatus(Authentication authentication,
                                                               @PathVariable("study-id") String enStudyId) {
        Long studyId = verifyId(enStudyId);
        Member member = getMember(authentication);
        Boolean recruited = service.updateStatus(studyId, member.getEmail());
        StudyDto.ResponseStatus studyStatus = mapper.studyStatus(recruited);

        return ResponseEntity.ok(studyStatus);
    }

    @PatchMapping("/{study-id}/leader")
    public ResponseEntity<String> patchLeader(Authentication authentication,
                                              @PathVariable("study-id") String enStudyId,
                                              @RequestBody StudyDto.PatchLeader patchLeaderDto) {
        Long studyId = verifyId(enStudyId);
        Member member = getMember(authentication);
        String newLeaderNickName = patchLeaderDto.getNickName();
        service.updateLeader(studyId, member.getEmail(), newLeaderNickName);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{study-id}")
    public ResponseEntity<StudyDto.Response> get(Authentication authentication,
                                                 @PathVariable("study-id") String enStudyId) {
        Long studyId = verifyId(enStudyId);
        String email = authentication.getName();
        StudyDto.Response studyResponse = service.getDto(studyId, email);

        return ResponseEntity.ok(studyResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<StudyPageDto.ResponsePage<List<StudyDto.Summary>>> getList(
            @RequestParam("p") String enPage,
            @RequestParam("s") String enSize,
            @RequestParam(value = "sort", required = false) String sort){

        int page = Integer.parseInt(getDecoded(enPage));
        int size = Integer.parseInt(getDecoded(enSize));

        StudyPageDto.ResponsePage<List<StudyDto.Summary>> studyPage = service.getPageDto(page-1, size, sort);

        return ResponseEntity.ok(studyPage);
    }

    @GetMapping("/leader/list")
    public ResponseEntity<StudyPageDto.ResponseList<List<StudyDto.Summary>>> getLeaderList(
            Authentication authentication) {

        String email = authentication.getName();
        StudyPageDto.ResponseList<List<StudyDto.Summary>> studyPage = service.getLeaderStudyListDto(email);

        return ResponseEntity.ok(studyPage);
    }

    @GetMapping("/join/list")
    public ResponseEntity<StudyPageDto.ResponseList<List<StudyDto.Summary>>> getJoinList(
            Authentication authentication,
            @RequestParam("m") String enIsMember) {

        Boolean isMember = Boolean.parseBoolean(getDecoded(enIsMember));
        String email = authentication.getName();
        StudyPageDto.ResponseList<List<StudyDto.Summary>> studyPage = service.getJoinListDto(email, isMember);

        return ResponseEntity.ok(studyPage);
    }

    @PatchMapping("/{study-id}/likes")
    public Long patchLikes(Authentication authentication,
                          @PathVariable("study-id") String enStudyId) {

        Long studyId = verifyId(enStudyId);
        Member member = getMember(authentication);
        Study study = service.get(studyId);

        return likesService.patch(member, study);
    }

    @GetMapping("/search")
    public ResponseEntity<StudyPageDto.ResponseList<List<StudyDto.Summary>>> get(
            @RequestParam("t") String enTag) {

        String tag = String.valueOf(getDecoded(enTag));
        StudyPageDto.ResponseList<List<StudyDto.Summary>> studyPage = tagService.search(tag);

        return ResponseEntity.ok(studyPage);
    }

    @DeleteMapping("/{study-id}")
    public ResponseEntity<String> delete(Authentication authentication,
                                         @PathVariable("study-id") String enStudyId) {
        Long studyId = verifyId(enStudyId);
        Member member = getMember(authentication);
        service.delete(studyId, member.getEmail());

        return ResponseEntity.ok().build();
    }

    /**
     * Base64 Decoder
     * @param message Encoded Message
     * @return Decoded Message
     */
    public String getDecoded(String message) {
        return obfuscationUtil.getDecoded(message);
    }

    /**
     * Study ID 검증
     * @param enStudyId Encoded Study ID
     * @return Long
     */
    private Long verifyId(String enStudyId) {
        long studyId = Long.parseLong(getDecoded(enStudyId));

        if (studyId < 1) {
            throw new BusinessLogicException(ExceptionCode.STUDY_NOT_FOUND);
        }
        return studyId;
    }

    /**
     * Member 조회
     * @param authentication Authentication
     * @return Member
     */
    private Member getMember(Authentication authentication) {
        return memberService.get(authentication.getName());
    }

    /**
     * Tag 추가 및 조회
     * @param tagValueList List<String>
     * @return List<Tag>
     */
    private List<Tag> getTagList(List<String> tagValueList) {
        return tagService.getList(tagValueList);
    }
}