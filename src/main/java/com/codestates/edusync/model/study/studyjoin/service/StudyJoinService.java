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
public class StudyJoinService {
    private final StudyJoinRepository repository;

    /**
     * 스터디 가입 신청
     * @param study
     * @param member
     */
    public void create(Study study, Member member) {

        if (Boolean.FALSE.equals(study.getIsRecruited())) {
            throw new BusinessLogicException(ExceptionCode.STUDY_RECRUITED_NOT_MODIFIED);
        }

        // 가입 이력 확인
        Optional<StudyJoin> findJoin = repository.findByStudyAndMember(study, member);

        if (findJoin.isPresent()) {
            if (Boolean.TRUE.equals(findJoin.get().getIsApproved())) {
                throw new BusinessLogicException(ExceptionCode.STUDY_JOIN_EXISTS);
            } else {
                throw new BusinessLogicException(ExceptionCode.STUDY_JOIN_CANDIDATE_EXISTS);
            }
        }

        StudyJoin studyJoin = new StudyJoin();
        studyJoin.setMember(member);
        studyJoin.setStudy(study);

        repository.save(studyJoin);
    }

    /**
     * 스터디 가입 신청 취소
     * @param study
     * @param member
     */
    public void delete(Study study, Member member) {

        Optional<StudyJoin> findJoin = repository.findByStudyIdAndMemberIdAndIsApprovedFalse(study.getId(), member.getId());

        if (findJoin.isEmpty()) throw new BusinessLogicException(ExceptionCode.STUDY_JOIN_CANDIDATE_NOT_FOUND);

        repository.delete(findJoin.get());
    }

    /**
     * 스터디 탈퇴
     * @param study
     * @param member
     */
    public void deleteJoin(Study study, Member member) {

        if (study.getLeader() == member) throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);

        Optional<StudyJoin> findJoin = repository.findByStudyIdAndMemberIdAndIsApprovedTrue(study.getId(), member.getId());

        if (findJoin.isEmpty()) throw new BusinessLogicException(ExceptionCode.STUDY_JOIN_NOT_FOUND);

        repository.delete(findJoin.get());
    }


    /**
     * 스터디 가입 승인
     * @param study
     * @param leader
     * @param newMember
     */
    public void update(Study study, Member leader, Member newMember) {

        if (!study.getLeader().getEmail().equals(leader.getEmail())) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        Optional<StudyJoin> findJoin = repository.findByStudyIdAndMemberIdAndIsApprovedFalse(study.getId(), newMember.getId());

        if (findJoin.isEmpty()) throw new BusinessLogicException(ExceptionCode.STUDY_JOIN_CANDIDATE_NOT_FOUND);

        findJoin.get().setIsApproved(true);
        repository.save(findJoin.get());
    }

    /**
     * 스터디 가입 거부
     * @param study
     * @param leader
     * @param newMember
     */
    public void reject(Study study, Member leader, Member newMember) {

        if (!study.getLeader().getEmail().equals(leader.getEmail())) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        Optional<StudyJoin> findJoin = repository.findByStudyIdAndMemberIdAndIsApprovedFalse(study.getId(), newMember.getId());

        if (findJoin.isEmpty()) throw new BusinessLogicException(ExceptionCode.STUDY_JOIN_CANDIDATE_NOT_FOUND);

        repository.delete(findJoin.get());
    }

    /**
     * 스터디 멤버 강퇴
     * @param study
     * @param leader
     * @param member
     */
    public void kick(Study study, Member leader, Member member) {

        if (!study.getLeader().getEmail().equals(leader.getEmail())) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        Optional<StudyJoin> findJoin = repository.findByStudyIdAndMemberIdAndIsApprovedTrue(study.getId(), member.getId());

        if (findJoin.isEmpty()) throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);

        repository.delete(findJoin.get());
    }
}
