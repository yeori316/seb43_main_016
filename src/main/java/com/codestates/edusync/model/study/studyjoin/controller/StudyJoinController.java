package com.codestates.edusync.model.study.studyjoin.controller;

import com.codestates.edusync.model.study.studyjoin.dto.StudyJoinDto;
import com.codestates.edusync.model.study.studyjoin.service.StudyJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/join")
@Validated
public class StudyJoinController {
    private final StudyJoinService service;

    /**
     * 스터디 가입 신청
     * @param studyId
     * @param authentication
     * @return
     */
    @PostMapping("/{study-id}")
    public ResponseEntity<String> post(Authentication authentication,
                                       @PathVariable("study-id") @Positive Long studyId) {

        service.create(studyId, authentication.getName());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 스터디 가입 신청 취소
     * @param studyId
     * @param authentication
     * @return
     */
    @DeleteMapping("/wait/{study-id}")
    public ResponseEntity<String> delete(Authentication authentication,
                                         @PathVariable("study-id") @Positive Long studyId) {

        service.delete(studyId, authentication.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * 스터디 탈퇴
     * @param studyId
     * @param authentication
     * @return
     */
    @DeleteMapping("/{study-id}")
    public ResponseEntity<String> deleteJoin(Authentication authentication,
                                             @PathVariable("study-id") @Positive Long studyId) {

        service.deleteJoin(studyId, authentication.getName());
        return ResponseEntity.ok().build();
    }


    /**
     * 스터디 가입 승인
     * @param studygroupId
     * @param studygroupJoinDto
     * @param authentication
     * @return
     */
    @PatchMapping("/{study-id}/apply")
    public ResponseEntity<String> patch(Authentication authentication,
                                        @PathVariable("study-id") @Positive Long studyId,
                                        @Valid @RequestBody StudyJoinDto.Dto patchDto) {

        service.update(studyId, authentication.getName(), patchDto.getNickName());
        return ResponseEntity.ok().build();
    }

    /**
     * 스터디 가입 거부
     * @param studyId
     * @param studygroupJoinDto
     * @param authentication
     * @return
     */
    @DeleteMapping("/{study-id}/reject")
    public ResponseEntity<String> deleteReject(Authentication authentication,
                                               @PathVariable("study-id") @Positive Long studyId,
                                               @Valid @RequestBody StudyJoinDto.Dto deleteDto) {

        service.reject(studyId, authentication.getName(), deleteDto.getNickName());
        return ResponseEntity.ok().build();
    }

    /**
     * 스터디 멤버 강퇴
     * @param studyId
     * @param studygroupJoinDto
     * @param authentication
     * @return
     */
    @DeleteMapping("/{study-id}/kick")
    public ResponseEntity<String> deleteKick(Authentication authentication,
                                             @PathVariable("study-id") @Positive Long studyId,
                                             @Valid @RequestBody StudyJoinDto.Dto deleteDto) {

        service.kick(studyId, authentication.getName(), deleteDto.getNickName());
        return ResponseEntity.ok().build();
    }
}
