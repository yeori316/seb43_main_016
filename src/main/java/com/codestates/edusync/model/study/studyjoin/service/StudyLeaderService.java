package com.codestates.edusync.model.study.studyjoin.service;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import com.codestates.edusync.model.study.studyjoin.repository.StudyJoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class StudyLeaderService implements StudyLeaderServiceInterface {
    private final StudyJoinRepository repository;

    public void update(Study study, Member leader, Member newMember) {

        if (!study.getLeader().getEmail().equals(leader.getEmail())) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        Optional<StudyJoin> findJoin = repository.findByStudyIdAndMemberIdAndIsApprovedFalse(study.getId(), newMember.getId());

        if (findJoin.isEmpty()) throw new BusinessLogicException(ExceptionCode.STUDY_JOIN_CANDIDATE_NOT_FOUND);

        findJoin.get().setIsApproved(true);
        repository.save(findJoin.get());
    }

    public void reject(Study study, Member leader, Member newMember) {

        if (!study.getLeader().getEmail().equals(leader.getEmail())) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        Optional<StudyJoin> findJoin = repository.findByStudyIdAndMemberIdAndIsApprovedFalse(study.getId(), newMember.getId());

        if (findJoin.isEmpty()) throw new BusinessLogicException(ExceptionCode.STUDY_JOIN_CANDIDATE_NOT_FOUND);

        repository.delete(findJoin.get());
    }

    public void kick(Study study, Member leader, Member member) {

        if (!study.getLeader().getEmail().equals(leader.getEmail())) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        Optional<StudyJoin> findJoin = repository.findByStudyIdAndMemberIdAndIsApprovedTrue(study.getId(), member.getId());

        if (findJoin.isEmpty()) throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);

        repository.delete(findJoin.get());
    }
}
