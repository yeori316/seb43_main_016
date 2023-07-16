package com.codestates.edusync.model.study.studyjoin.repository;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyJoinRepository extends JpaRepository<StudyJoin, Long> {

    /**
     * 가입 이력 조회 시
     * @param study
     * @param member
     * @return
     */
    Optional<StudyJoin> findByStudyAndMember(Study study, Member member);

    /**
     * 스터디 멤버 조회
     * @param studyId
     * @param memberId
     * @return
     */
    Optional<StudyJoin> findByStudyIdAndMemberIdAndIsApprovedTrue(Long studyId, Long memberId);

    /**
     * 가입 신청 조회
     * @param studyId
     * @param memberId
     * @return
     */
    Optional<StudyJoin> findByStudyIdAndMemberIdAndIsApprovedFalse(Long studyId, Long memberId);

    /**
     * 가입 신청한 스터디 리스트
     * @param member
     * @return
     */
    List<StudyJoin> findAllByMemberAndIsApprovedIsFalse(Member member);

    /**
     * 가입된 스터디 리스트
     * @param member
     * @return
     */
    List<StudyJoin> findAllByMemberAndIsApprovedIsTrue(Member member);

    /**
     * 가입 대기 중인 멤버 리스트
     * @param study
     * @return
     */
    List<StudyJoin> findAllByStudyAndIsApprovedIsFalse(Study study);

    /**
     * 스터디 멤버 리스트
     * @param study
     * @return
     */
    List<StudyJoin> findAllByStudyAndIsApprovedIsTrue(Study study);

    /**
     * 스터디 가입된 멤버 수
     * @param study
     * @return
     */
    Integer countByStudyAndIsApprovedIsTrue(Study study);
}
