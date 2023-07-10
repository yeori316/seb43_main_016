package com.codestates.edusync.model.study.studyjoin.repository;

import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyJoinRepository extends JpaRepository<StudyJoin, Long> {

    /**
     * 사용자가 가입 신청한 스터디 리스트
     * @param memberId
     * @return
     */
    //@EntityGraph(attributePaths = "studygroup.searchTags")
    List<StudyJoin> findAllByMemberIdAndIsApprovedIsFalse(Long memberId);

    /**
     * 사용자가 가입된 스터디 리스트
     * @param memberId
     * @return
     */
    //@EntityGraph(attributePaths = "studygroup.searchTags")
    List<StudyJoin> findAllByMemberIdAndIsApprovedIsTrue(Long memberId);

    /**
     * 스터디에 가입 대기 중인 사용자 리스트
     * @param studygroupId
     * @return
     */
    List<StudyJoin> findAllByStudyIdAndIsApprovedIsFalse(Long studyId);

    /**
     * 스터디 멤버 리스트
     * @param studygroupId
     * @return
     */
    List<StudyJoin> findAllByStudyIdAndIsApprovedIsTrue(Long studyId);

    /**
     * 스터디에 가입된 멤버 수
     * @param studygroupId
     * @return
     */
    Integer countByStudyIdAndIsApprovedIsTrue(Long studyId);

    Optional<StudyJoin> findByStudyIdAndMemberId(Long studyId, Long memberId);
}
