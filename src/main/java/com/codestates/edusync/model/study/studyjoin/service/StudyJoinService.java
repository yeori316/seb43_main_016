package com.codestates.edusync.model.study.studyjoin.service;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.service.MemberService;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.study.service.StudyService;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import com.codestates.edusync.model.study.studyjoin.repository.StudyJoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class StudyJoinService {
    private final StudyJoinRepository repository;
    private final MemberService memberService;
    private final StudyService studyService;

    /**
     * 스터디 가입 신청
     * @param studyId
     * @param email
     */
    public void create(Long studyId, String email) {

        Member member = memberService.get(email);
        Study study = studyService.get(studyId);

        if (Boolean.FALSE.equals(study.getIsRecruited())) {
            throw new BusinessLogicException(ExceptionCode.STUDYGROUP_RECRUITED_NOT_MODIFIED);
        }

        // 가입 이력 확인
        Optional<StudyJoin> findJoin = repository.findByStudyAndMember(study, member);

        if (findJoin.isPresent()) {
            if (Boolean.TRUE.equals(findJoin.get().getIsApproved())) {
                throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_EXISTS);
            } else {
                throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_CANDIDATE_EXISTS);
            }
        }

        StudyJoin studyJoin = new StudyJoin();
        studyJoin.setMember(member);
        studyJoin.setStudy(study);

        repository.save(studyJoin);
    }

    /**
     * 스터디 가입 신청 취소
     * @param studyId
     * @param email
     */
    public void delete(Long studyId, String email) {

        Member member = memberService.get(email);

        Optional<StudyJoin> findJoin = repository.findByStudyIdAndMemberIdAndIsApprovedFalse(studyId, member.getId());

        if (findJoin.isEmpty()) throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_CANDIDATE_NOT_FOUND);

        repository.delete(findJoin.get());
    }

    /**
     * 스터디 탈퇴
     * @param studyId
     * @param email
     */
    public void deleteJoin(Long studyId, String email) {

        Member member = memberService.get(email);

        Optional<StudyJoin> findJoin = repository.findByStudyIdAndMemberIdAndIsApprovedTrue(studyId, member.getId());

        if (findJoin.isEmpty()) throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_NOT_FOUND);

        repository.delete(findJoin.get());
    }


    /**
     * 스터디 가입 승인
     * @param studyId
     * @param nickName
     * @param email
     */
    public void update(Long studyId, String email, String nickName) {

        memberService.get(email);
        Member newMember = memberService.getNickName(nickName);

        if (!studyService.get(studyId).getLeader().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        Optional<StudyJoin> findJoin = repository.findByStudyIdAndMemberIdAndIsApprovedFalse(studyId, newMember.getId());

        if (findJoin.isEmpty()) throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_CANDIDATE_NOT_FOUND);

        findJoin.get().setIsApproved(true);
        repository.save(findJoin.get());
    }

    /**
     * 스터디 가입 거부
     * @param studyId
     * @param nickName
     * @param email
     */
    public void reject(Long studyId, String email, String nickName) {

        memberService.get(email);
        Member newMember = memberService.getNickName(nickName);

        if (!studyService.get(studyId).getLeader().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        Optional<StudyJoin> findJoin = repository.findByStudyIdAndMemberIdAndIsApprovedFalse(studyId, newMember.getId());

        if (findJoin.isEmpty()) throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_CANDIDATE_NOT_FOUND);

        repository.delete(findJoin.get());
    }

    /**
     * 스터디 멤버 강퇴
     * @param studyId
     * @param nickName
     * @param email
     */
    public void kick(Long studyId, String email, String nickName) {

        memberService.get(email);
        Member newMember = memberService.getNickName(nickName);

        if (!studyService.get(studyId).getLeader().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        Optional<StudyJoin> findJoin = repository.findByStudyIdAndMemberIdAndIsApprovedTrue(studyId, newMember.getId());

        if (findJoin.isEmpty()) throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);

        repository.delete(findJoin.get());
    }
}
