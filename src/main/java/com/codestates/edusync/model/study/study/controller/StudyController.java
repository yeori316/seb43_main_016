package com.codestates.edusync.model.study.study.controller;

import com.codestates.edusync.model.common.dto.CommonDto;
import com.codestates.edusync.model.member.service.MemberService;
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
import java.io.UnsupportedEncodingException;
import java.net.URI;
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

    /**
     * 스터디 등록
     * @param authentication Authentication
     * @param postDto StudyPostDto
     * @return URI
     */
    @PostMapping
    public ResponseEntity<URI> post(Authentication authentication,
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
                                        @Positive @PathVariable("study-id") Long studyId,
                                        @Valid @RequestBody StudyDto.Patch patchDto) {

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
                                             @Positive @PathVariable("study-id") Long studyId,
                                             @RequestPart(value="image") MultipartFile image) {

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
                                                               @PathVariable("study-id") @Positive Long studyId) {
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
                                              @PathVariable("study-id") @Positive Long studyId,
                                              @RequestBody StudyDto.PatchLeader patchLeaderDto) {
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
                                                 @PathVariable("study-id") Long studyId) throws UnsupportedEncodingException {

//        String decode = URLDecoder.decode(studyId, "UTF-8");
//        Base64.Decoder decoder = Base64.getDecoder();
//        byte[] decodedBytes = decoder.decode(decode.getBytes());
//        long id = Long.valueOf(new String(decodedBytes));

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
            @RequestParam("p") @Positive Integer page,
            @RequestParam("s") @Positive Integer size,
            @RequestParam(value = "sort", required = false) String sort){

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
            @RequestParam("m") Boolean isMember) {

        return ResponseEntity.ok(
                service.getJoinListDto(authentication.getName(), isMember)
        );
    }

    /**
     * 태그 스터디 검색
     * @param tag
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<StudyPageDto.ResponseList<List<StudyDto.Summary>>> get(
            @RequestParam("t") String tag) {

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
                                         @PathVariable("study-id") @Positive Long studyId) {

        service.delete(
                studyId,
                memberService.get(authentication.getName()).getEmail()
        );

        return ResponseEntity.ok().build();
    }
}