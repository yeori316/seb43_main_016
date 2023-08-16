package com.codestates.edusync.model.study.studyjoin.controller;

import com.codestates.edusync.model.study.studyjoin.dto.StudyJoinDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface StudyLeaderControllerInterface {
    /**
         * 스터디 가입 승인
         * @param authentication
         * @param studyId
         * @param patchDto
         * @return
         */
        @PatchMapping("/{study-id}/apply")
        ResponseEntity<String> patch(Authentication authentication,
                                     @PathVariable("study-id") String enStudyId,
                                     @Valid @RequestBody StudyJoinDto.Dto patchDto);

        /**
         * 스터디 가입 거부
         * @param authentication
         * @param studyId
         * @param deleteDto
         * @return
         */
        @DeleteMapping("/{study-id}/reject")
        ResponseEntity<String> deleteReject(Authentication authentication,
                                            @PathVariable("study-id") String enStudyId,
                                            @Valid @RequestBody StudyJoinDto.Dto deleteDto);

        /**
         * 스터디 멤버 강퇴
         * @param authentication Authentication
         * @param enStudyId Encrypted StudyId
         * @param deleteDto
         * @return
         */
        @DeleteMapping("/{study-id}/kick")
        ResponseEntity<String> deleteKick(Authentication authentication,
                                          @PathVariable("study-id") String enStudyId,
                                          @Valid @RequestBody StudyJoinDto.Dto deleteDto);
}
