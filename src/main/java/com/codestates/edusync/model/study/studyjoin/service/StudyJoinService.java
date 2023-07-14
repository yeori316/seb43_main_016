package com.codestates.edusync.model.study.studyjoin.service;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.service.MemberManager;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.study.service.StudyManager;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import com.codestates.edusync.model.study.studyjoin.repository.StudyJoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class StudyJoinService {
    private final StudyJoinRepository repository;
    private final MemberManager memberManager;
    private final StudyManager studyManager;


    /**
     * 스터디 가입 신청
     * @param studyId
     * @param email
     */
    public void create(Long studyId, String email) {

        Member member = getMember(email);
        StudyJoin studyJoin = null;

        // 이미 가입 신청 했는지 확인
        List<StudyJoin> sjs1 =
                repository.findAllByStudyIdAndIsApprovedIsFalse(studyId);
        for (StudyJoin sj : sjs1) {
            if (sj.getMember().getNickName().equals(member.getNickName())) studyJoin = sj;
        }
        if (studyJoin != null) {
            throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_CANDIDATE_EXISTS);
        }


        // 가입 되어 있는지 확인
        List<StudyJoin> sjs2 =
                repository.findAllByStudyIdAndIsApprovedIsTrue(studyId);
        for (StudyJoin sj : sjs2) {
            if (sj.getMember().getNickName().equals(member.getNickName())) studyJoin = sj;
        }
        if (studyJoin != null) {
            throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_EXISTS);
        }

        studyJoin = new StudyJoin();
        studyJoin.setMember(member);
        studyJoin.setStudy(getStudy(studyId));

        repository.save(studyJoin);
    }

    /**
     * 가입 신청한 스터디 리스트 조회
     * @param email
     * @return
     */
    @Transactional(readOnly = true)
    public List<Study> getApplyList(String email) {
        return repository.findAllByMemberIdAndIsApprovedIsFalse(getMember(email).getId())
                .stream().map(StudyJoin::getStudy).collect(Collectors.toList());
    }

    /**
     * 가입된 스터디 리스트 조회
     * @param email
     * @return
     */
    @Transactional(readOnly = true)
    public List<Study> getJoinList(String email) {
        return repository.findAllByMemberIdAndIsApprovedIsTrue(getMember(email).getId())
                .stream().map(StudyJoin::getStudy).collect(Collectors.toList());
    }

    /**
     * 리더로 운영 중인 스터디 리스트 조회
     * @param email
     * @return
     */
    @Transactional(readOnly = true)
    public List<Study> getLeaderList(String email) {
        return studyManager.getLeaderStudyList(email);
    }

    /**
     * 스터디 가입 신청 취소
     * @param studyId
     * @param email
     */
    public void deleteApply(Long studyId, String email) {

        StudyJoin studyJoin = null;
        List<StudyJoin> studyJoins = repository.findAllByStudyIdAndIsApprovedIsFalse(studyId);

        for (StudyJoin sj : studyJoins) {
            if (sj.getMember().getEmail().equals(email)) {
                studyJoin = sj;
                repository.delete(sj);
                break;
            }
        }

        if (studyJoin == null)
            throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_CANDIDATE_NOT_FOUND);
    }

    /**
     * 스터디 탈퇴
     * @param studyId
     * @param email
     */
    public void deleteJoin(Long studyId, String email) {

        if (getStudy(studyId).getLeader().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_YOU_ARE_STUDYGROUP_LEADER);
        }

        StudyJoin studyJoin = null;
        List<StudyJoin> studyJoins;

        studyJoins = repository.findAllByStudyIdAndIsApprovedIsTrue(studyId);

        for (StudyJoin sj : studyJoins) {
            if (sj.getMember().getEmail().equals(email)) {
                studyJoin = sj;
                repository.delete(sj);
                break;
            }
        }

        if (studyJoin == null)
            throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_NOT_FOUND);
    }






    /**
     * 스터디 가입 신청자 리스트 조회
     * @param studyId
     * @param email
     * @return
     */
    @Transactional(readOnly = true)
    public List<StudyJoin> getAllCandidateList(Long studyId, String email) {

        if (!getStudy(studyId).getLeader().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        return repository.findAllByStudyIdAndIsApprovedIsFalse(studyId);
    }

    /**
     * 스터디 멤버 리스트 조회
     * @param studyId
     * @param email
     * @return
     */
    @Transactional(readOnly = true)
    public List<StudyJoin> getAllMemberList(Long studyId, String email) {

        if (!getStudy(studyId).getLeader().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        return repository.findAllByStudyIdAndIsApprovedIsTrue(studyId);
    }

    /**
     * 스터디 가입 승인
     * @param studyId
     * @param nickName
     * @param email
     */
    public void approveCandidateByNickName(Long studyId, String nickName, String email) {

        if (!getStudy(studyId).getLeader().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        StudyJoin studyJoin = null;

        List<StudyJoin> sjs =
                repository.findAllByStudyIdAndIsApprovedIsFalse(studyId);

        for (StudyJoin sj : sjs) {
            if (sj.getMember().getNickName().equals(nickName)) studyJoin = sj;
        }

        if (studyJoin == null) throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_CANDIDATE_NOT_FOUND);


        if (getStudy(studyId).getLeader().getNickName().equals(nickName)) {
            throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_YOU_ARE_STUDYGROUP_LEADER);
        }

        studyJoin.setIsApproved(true);
        repository.save(studyJoin);
    }

    /**
     * 스터디 가입 거부
     * @param studyId
     * @param nickName
     * @param email
     */
    public void rejectCandidateByNickName(Long studyId, String nickName, String email) {

        if (!getStudy(studyId).getLeader().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        StudyJoin studyJoin = null;

        List<StudyJoin> sjs =
                repository.findAllByStudyIdAndIsApprovedIsFalse(studyId);

        for (StudyJoin sj : sjs) {
            if (sj.getMember().getNickName().equals(nickName)) studyJoin = sj;
        }

        if (studyJoin == null) throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_CANDIDATE_NOT_FOUND);

        if (getStudy(studyId).getLeader().getNickName().equals(nickName)) {
            throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_YOU_ARE_STUDYGROUP_LEADER);
        }

        repository.delete(studyJoin);
    }

    /**
     * 스터디 멤버 강퇴
     * @param studyId
     * @param nickName
     * @param email
     */
    public void kickOutMemberByNickName(Long studyId, String nickName, String email) {

        if (!getStudy(studyId).getLeader().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        StudyJoin studyJoin = null;

        List<StudyJoin> sjs =
                repository.findAllByStudyIdAndIsApprovedIsTrue(studyId);

        for (StudyJoin sj : sjs) {
            if (sj.getMember().getNickName().equals(nickName)) studyJoin = sj;
        }

        if (studyJoin == null) throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);


        if (getStudy(studyId).getLeader().getNickName().equals(nickName)) {
            throw new BusinessLogicException(ExceptionCode.STUDYGROUP_JOIN_YOU_ARE_STUDYGROUP_LEADER);
        }

        repository.delete(studyJoin);
    }























    /**
     * 회원 조회
     * @param email
     * @return
     */
    public Member getMember(String email) {
        return memberManager.get(email);
    }

    /**
     * 스터디 조회
     * @param studyId
     * @return
     */
    public Study getStudy(Long studyId) {
        return studyManager.get(studyId);
    }
}
