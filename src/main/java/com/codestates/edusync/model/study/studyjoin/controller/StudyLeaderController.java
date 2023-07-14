package com.codestates.edusync.model.study.studyjoin.controller;

import com.codestates.edusync.model.common.dto.CommonDto;
import com.codestates.edusync.model.study.study.dto.StudyDto;
import com.codestates.edusync.model.study.study.mapper.StudyMapper;
import com.codestates.edusync.model.study.study.service.StudyService;
import com.codestates.edusync.model.study.studyjoin.dto.StudyJoinDto;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import com.codestates.edusync.model.study.studyjoin.mapper.StudyJoinMapper;
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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/leader")
@Validated
public class StudyLeaderController {
    private final StudyMapper studyMapper;
    private final StudyJoinMapper mapper;
    private final StudyJoinService service;
    private final StudyService studyService;

    /**
     * 스터디 리더 수정
     * @param authentication
     * @param studyId
     * @param patchLeader
     * @return
     */
    @PatchMapping("/{study-id}")
    public ResponseEntity<String> patchLeader(Authentication authentication,
                                              @PathVariable("study-id") @Positive Long studyId,
                                              @RequestBody StudyDto.PatchLeader patchLeader) {

        // TODO 멤버가 아닌데도 권한 수정이 됨
        studyService.updateLeader(
                studyId,
                service.getMember(authentication.getName()).getEmail(),
                patchLeader.getNickName()
        );

        return ResponseEntity.ok().build();
    }

    /**
     * 리더로 운영 중인 스터디 리스트 조회
     * @param authentication
     * @return
     */
    @GetMapping
    public ResponseEntity getLeaderList(Authentication authentication) {
        return ResponseEntity.ok(
                new CommonDto.ResponseList<>(
                        studyMapper.studyListToResponseList(
                                service.getLeaderList(authentication.getName())
                        )
                )
        );
    }


    /**
     * 스터디 가입 신청자 리스트 조회
     * @param studyId
     * @param authentication
     * @return
     */
    @GetMapping("/{study-id}/wait")
    public ResponseEntity getJoin(Authentication authentication,
                                  @PathVariable("study-id") @Positive Long studyId) {

        List<StudyJoin> studyJoinList =
                service.getAllCandidateList(studyId, authentication.getName()); // 대기 리스트

        StudyJoinDto.Response studyJoinDtos =
                mapper.studygroupJoinToStudygroupJoinDtos(studyJoinList);

        return ResponseEntity.ok(studyJoinDtos);
    }

    /**
     * 스터디 멤버 리스트 조회
     * @param studyId
     * @param authentication
     * @return
     */
    @GetMapping("/{study-id}/members")
    public ResponseEntity getStudygroupJoins(Authentication authentication,
                                             @PathVariable("study-id") @Positive Long studyId) {

        List<StudyJoin> studyJoinList =
                service.getAllMemberList(studyId, authentication.getName()); // 멤버 리스트

        StudyJoinDto.Response studyJoinDtos =
                mapper.studygroupJoinToStudygroupJoinDtos(studyJoinList);

        return ResponseEntity.ok(studyJoinDtos);
    }

    /**
     * 스터디 가입 승인
     * @param studygroupId
     * @param studygroupJoinDto
     * @param authentication
     * @return
     */
    @PatchMapping("/{studygroup-id}/apply")
    public ResponseEntity postStudygroupJoinApprove(Authentication authentication,
                                                    @PathVariable("studygroup-id") @Positive Long studygroupId,
                                                    @Valid @RequestBody StudyJoinDto.Dto studygroupJoinDto) {

        service.approveCandidateByNickName(
                studygroupId,
                studygroupJoinDto.getNickName(),
                authentication.getName()
        );

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
    public ResponseEntity deleteStudygroupJoinReject(Authentication authentication,
                                                     @PathVariable("study-id") @Positive Long studyId,
                                                     @Valid @RequestBody StudyJoinDto.Dto studygroupJoinDto) {

        service.rejectCandidateByNickName(
                studyId,
                studygroupJoinDto.getNickName(),
                authentication.getName()
        );

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * 스터디 멤버 강퇴
     * @param studyId
     * @param studygroupJoinDto
     * @param authentication
     * @return
     */
    @DeleteMapping("/{studygroup-id}/kick")
    public ResponseEntity deleteStudygroupJoinKick(Authentication authentication,
                                                   @PathVariable("studygroup-id") @Positive Long studyId,
                                                   @Valid @RequestBody StudyJoinDto.Dto studygroupJoinDto) {

        service.kickOutMemberByNickName(
                studyId,
                studygroupJoinDto.getNickName(),
                authentication.getName()
        );

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
