package com.codestates.edusync.model.study.studyjoin.controller;

import com.codestates.edusync.model.common.dto.CommonDto;
import com.codestates.edusync.model.member.entity.Member;
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
@RequestMapping("/study-join")
@Validated
public class StudyJoinController {
    private final StudyJoinMapper mapper;
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
                        mapper.studyListToResponseList(
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
                        mapper.studyListToResponseList(
                                service.getJoinList(authentication.getName())
                        )
                )
        );

    }

    /**
     * 리더로 운영 중인 스터디 리스트 조회
     * @param authentication
     * @return
     */
    @GetMapping("/leader")
    public ResponseEntity getLeaderList(Authentication authentication) {
        return ResponseEntity.ok(
                new CommonDto.ResponseList<>(
                        mapper.studyListToResponseList(
                                service.getLeaderList(authentication.getName())
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






    /**
     * 스터디 가입 신청자 리스트 조회
     * @param studyId
     * @param authentication
     * @return
     */
    @GetMapping("/{study-id}/apply")
    public ResponseEntity getJoin(Authentication authentication,
                                  @PathVariable("study-id") @Positive Long studyId) {

        List<StudyJoin> studyJoinList =
                service.getAllCandidateList(studyId, authentication.getName()); // 대기 리스트

        StudyJoinDto.Response studygroupJoinDtos =
                mapper.studygroupJoinToStudygroupJoinDtos(studyJoinList);

        return ResponseEntity.ok(studygroupJoinDtos);
    }

    /**
     * 스터디 멤버 리스트 조회
     * @param studyId
     * @param authentication
     * @return
     */
    @GetMapping("/{study-id}/member")
    public ResponseEntity getStudygroupJoins(Authentication authentication,
                                             @PathVariable("study-id") @Positive Long studyId) {

        List<StudyJoin> studyJoinList;
        studyJoinList = service.getAllMemberList(studyId, authentication.getName()); // 멤버 리스트

        StudyJoinDto.Response studygroupJoinDtos =
                mapper.studygroupJoinToStudygroupJoinDtos(studyJoinList);

        return ResponseEntity.ok(studygroupJoinDtos);
    }

    /**
     * 스터디 가입 승인
     * @param studygroupId
     * @param studygroupJoinDto
     * @param authentication
     * @return
     */
    @PatchMapping("/{studygroup-id}")
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
    @DeleteMapping("/{study-id}")
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
