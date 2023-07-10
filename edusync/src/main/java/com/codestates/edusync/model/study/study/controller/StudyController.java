package com.codestates.edusync.model.study.study.controller;

import com.codestates.edusync.model.common.dto.CommonDto;
import com.codestates.edusync.model.common.utils.UriCreator;
import com.codestates.edusync.model.study.study.dto.StudyDto;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.study.mapper.StudyMapper;
import com.codestates.edusync.model.study.study.service.StudyService;
import com.codestates.edusync.model.study.tag.entity.Tag;
import com.codestates.edusync.model.study.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/study")
@Validated
public class StudyController {
    private final StudyMapper mapper;
    private final StudyService service;

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
                service.getMember(authentication.getName()),
                service.getTagList(postDto.getTags())
        );

        URI uri = UriCreator.createUri("/study", service.create(study).getId());

        return ResponseEntity.created(uri).build();
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

        service.update( // TODO 스터디 수정 시 태그 수정 어떻게 할지....
                mapper.studyPatchToStudy(patchDto, studyId, service.getTagList(patchDto.getTags())),
                service.getMember(authentication.getName()).getEmail()
        );

        return ResponseEntity.ok().build();
    }

    /**
     * 스터디 이미지 수정
     * @param authentication
     * @param studyId
     * @param file
     * @return
     */
    @PatchMapping("/{study-id}/image")
    public ResponseEntity<String> patchImage(Authentication authentication,
                                             @Positive @PathVariable("study-id") Long studyId,
                                             @RequestPart(value="image") MultipartFile file) {

        service.updateImage(
                studyId,
                service.getMember(authentication.getName()).getEmail(),
                file
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
    public ResponseEntity<String> patchStatus(Authentication authentication,
                                              @PathVariable("study-id") @Positive Long studyId) {

        service.updateStatus(
                studyId,
                service.getMember(authentication.getName()).getEmail()
        );

        return ResponseEntity.ok().build();
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
                                              @RequestBody StudyDto.PatchLeader patchLeader) {

        service.updateLeader(
                studyId,
                service.getMember(authentication.getName()).getEmail(),
                patchLeader.getNickName()
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
                                                 @PathVariable("study-id") String studyId) throws UnsupportedEncodingException {

        String decode = URLDecoder.decode(studyId, "UTF-8");
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(decode.getBytes());
        long id = Long.valueOf(new String(decodedBytes));

        String email = service.getMember(authentication.getName()).getEmail();
        Study study = service.get(id);

        StudyDto.Response response =
                mapper.studyToResponse(
                        study,
                        service.getStudyMemberCount(id),
                        study.getMember().getEmail().equals(email)
                );

        return ResponseEntity.ok(response);
    }

    /**
     * 스터디 리스트
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<CommonDto.ResponsePage<List<StudyDto.Summary>>> getList(
            @RequestParam("page") @Positive Integer page,
            @RequestParam("size") @Positive Integer size,
            @RequestParam(value = "sort", required = false) String sort){

        Page<Study> studyPage = service.getList(page-1, size, sort);

        List<StudyDto.Summary> responseList =
                mapper.studyListToResponseList(studyPage.getContent());

        return ResponseEntity.ok(new CommonDto.ResponsePage<>(responseList, studyPage));
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
                service.getMember(authentication.getName()).getEmail()
        );

        return ResponseEntity.ok().build();
    }
}