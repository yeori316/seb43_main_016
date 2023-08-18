package com.codestates.edusync.model.study.studyjoin.controller;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.common.util.ObfuscationUtil;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.service.MemberServiceInterface;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.study.service.StudyServiceInterface;
import com.codestates.edusync.model.study.studyjoin.dto.StudyJoinDto;
import com.codestates.edusync.model.study.studyjoin.service.StudyLeaderServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/leader")
@Validated
public class StudyLeaderController implements StudyLeaderControllerInterface {
    private final StudyLeaderServiceInterface service;
    private final MemberServiceInterface memberService;
    private final StudyServiceInterface studyService;
    private final ObfuscationUtil obfuscationUtil;

    @PatchMapping("/{study-id}/apply")
    public ResponseEntity<String> patch(Authentication authentication,
                                        @PathVariable("study-id") String enStudyId,
                                        @Valid @RequestBody StudyJoinDto.Dto patchDto) {
        Long studyId = verifyId(enStudyId);
        Study study = studyService.get(studyId);
        Member member = memberService.get(authentication.getName());
        Member nickNameMember = memberService.getNickName(patchDto.getNickName());
        service.update(study, member, nickNameMember);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{study-id}/reject")
    public ResponseEntity<String> deleteReject(Authentication authentication,
                                               @PathVariable("study-id") String enStudyId,
                                               @Valid @RequestBody StudyJoinDto.Dto deleteDto) {
        Long studyId = verifyId(enStudyId);
        Study study = studyService.get(studyId);
        Member member = memberService.get(authentication.getName());
        Member nickNameMember = memberService.getNickName(deleteDto.getNickName());
        service.reject(study, member,nickNameMember);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{study-id}/kick")
    public ResponseEntity<String> deleteKick(Authentication authentication,
                                             @PathVariable("study-id") String enStudyId,
                                             @Valid @RequestBody StudyJoinDto.Dto deleteDto) {
        Long studyId = verifyId(enStudyId);
        Study study = studyService.get(studyId);
        Member member = memberService.get(authentication.getName());
        Member nickNameMember = memberService.getNickName(deleteDto.getNickName());
        service.kick(study, member, nickNameMember);
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
