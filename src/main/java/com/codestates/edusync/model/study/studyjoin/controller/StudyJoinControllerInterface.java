package com.codestates.edusync.model.study.studyjoin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public interface StudyJoinControllerInterface {
    /**
         * 스터디 가입 신청
         * @param authentication Authentication
         * @param enStudyId Encrypted Study ID
         * @return String
         */
        @PostMapping("/{study-id}")
        ResponseEntity<String> post(Authentication authentication,
                                    @PathVariable("study-id") String enStudyId);

        /**
         * 스터디 가입 신청 취소
         * @param authentication Authentication
         * @param enStudyId Encrypted Study ID
         * @return String
         */
        @DeleteMapping("/wait/{study-id}")
        ResponseEntity<String> delete(Authentication authentication,
                                      @PathVariable("study-id") String enStudyId);

        /**
         * 스터디 탈퇴
         * @param authentication Authentication
         * @param enStudyId Encrypted Study ID
         * @return String
         */
        @DeleteMapping("/{study-id}")
        ResponseEntity<String> deleteJoin(Authentication authentication,
                                          @PathVariable("study-id") String enStudyId);
}
