package com.codestates.edusync.model.study.studyjoin.service;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.study.entity.Study;

public interface StudyJoinServiceInterface {

        /**
         * 스터디 가입 신청
         * @param study Study
         * @param member Member
         */
        void create(Study study, Member member);

        /**
         * 스터디 가입 신청 취소
         * @param study Study
         * @param member Member
         */
        void delete(Study study, Member member);

        /**
         * 스터디 탈퇴
         * @param study Study
         * @param member Member
         */
        void deleteJoin(Study study, Member member);
}
