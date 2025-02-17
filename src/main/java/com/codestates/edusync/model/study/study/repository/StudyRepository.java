package com.codestates.edusync.model.study.study.repository;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.study.entity.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {

    Optional<Study> findById(Long studyId);

    Optional<Study> findByStudyName(String studyName);

    Page<Study> findAll(Pageable pageable);

    /**
     * 본인이 리더인 스터디 리스트 조회
     * @param memberId
     * @return
     */
    List<Study> findAllByLeader(Member member);
}
