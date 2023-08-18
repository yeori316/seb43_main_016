package com.codestates.edusync.model.study.studyjoin.service;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.study.entity.Study;

public interface StudyLeaderServiceInterface {

        /**
         * 스터디 가입 승인
         * @param study Study
         * @param leader Member Leader
         * @param newMember Member NewLeader
         */
        void update(Study study, Member leader, Member newMember);

        /**
         * 스터디 가입 거부
         * @param study Study
         * @param leader Member Leader
         * @param newMember Member NewLeader
         */
        void reject(Study study, Member leader, Member newMember);

        /**
         * 스터디 멤버 강퇴
         * @param study Study
         * @param leader Member Leader
         * @param member Member NewLeader
         */
        void kick(Study study, Member leader, Member member);
}
