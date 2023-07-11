package com.codestates.edusync.model.study.studyjoin.controller;

import com.codestates.edusync.model.common.dto.CommonDto;
import com.codestates.edusync.model.study.study.mapper.StudyMapper;
import com.codestates.edusync.model.study.studyjoin.service.StudyJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/study-join")
@Validated
public class StudyJoinController {
    private final StudyMapper studyMapper;
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
     * 가입 신청한 스터디 리스트 조회
     * @param authentication
     * @return
     */
    @GetMapping("/apply")
    public ResponseEntity getApplyList(Authentication authentication) {

        return ResponseEntity.ok(
                new CommonDto.ResponseList<>(
                        studyMapper.studyListToResponseList(
                                service.getApplyList(authentication.getName())
                        )
                )
        );
    }

    /**
     * 가입된 스터디 리스트 조회
     * @param authentication
     * @return
     */
    @GetMapping("/join")
    public ResponseEntity getJoinList(Authentication authentication) {

        return ResponseEntity.ok(
                new CommonDto.ResponseList<>(
                        studyMapper.studyListToResponseList(
                                service.getJoinList(authentication.getName())
                        )
                )
        );

    }

    /**
     * 스터디 가입 신청 취소
     * @param studyId
     * @param authentication
     * @return
     */
    @DeleteMapping("/apply/{study-id}")
    public ResponseEntity deleteApply(Authentication authentication,
                                      @PathVariable("study-id") @Positive Long studyId) {

        service.deleteApply(studyId, authentication.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * 스터디 탈퇴
     * @param studyId
     * @param authentication
     * @return
     */
    @DeleteMapping("/join/{study-id}")
    public ResponseEntity deleteJoin(Authentication authentication,
                                     @PathVariable("study-id") @Positive Long studyId) {

        service.deleteJoin(studyId, authentication.getName());
        return ResponseEntity.ok().build();
    }
}
