package com.codestates.edusync.model.study.studyjoin.service;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.study.entity.Study;

public interface StudyLeaderServiceInterface {

        /**
         * 스터디 가입 승인
         * @param study
         * @param leader
         * @param newMember
         */
        void update(Study study, Member leader, Member newMember);

        /**
         * 스터디 가입 거부
         * @param study
         * @param leader
         * @param newMember
         */
        void reject(Study study, Member leader, Member newMember);

        /**
         * 스터디 멤버 강퇴
         * @param study
         * @param leader
         * @param member
         */
        void kick(Study study, Member leader, Member member);
}
