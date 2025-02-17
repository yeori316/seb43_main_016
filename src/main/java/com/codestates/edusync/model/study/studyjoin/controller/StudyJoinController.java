package com.codestates.edusync.model.study.studyjoin.controller;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.common.util.ObfuscationUtil;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.service.MemberServiceInterface;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.study.service.StudyServiceInterface;
import com.codestates.edusync.model.study.studyjoin.service.StudyJoinServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/join")
@Validated
public class StudyJoinController implements StudyJoinControllerInterface {
    private final StudyJoinServiceInterface service;
    private final MemberServiceInterface memberService;
    private final StudyServiceInterface studyService;
    private final ObfuscationUtil obfuscationUtil;

    @PostMapping("/{study-id}")
    public ResponseEntity<String> post(Authentication authentication,
                                       @PathVariable("study-id") String enStudyId) {

        Long studyId = verifyId(enStudyId);
        Study study = studyService.get(studyId);
        Member member = memberService.get(authentication.getName());
        service.create(study, member);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/wait/{study-id}")
    public ResponseEntity<String> delete(Authentication authentication,
                                         @PathVariable("study-id") String enStudyId) {

        Long studyId = verifyId(enStudyId);
        Study study = studyService.get(studyId);
        Member member = memberService.get(authentication.getName());
        service.delete(study ,member);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{study-id}")
    public ResponseEntity<String> deleteJoin(Authentication authentication,
                                             @PathVariable("study-id") String enStudyId) {

        Long studyId = verifyId(enStudyId);
        Study study = studyService.get(studyId);
        Member member = memberService.get(authentication.getName());
        service.deleteJoin(study, member);
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
