package com.codestates.edusync.model.study.study.service;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.common.utils.AwsS3Service;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.service.MemberService;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.study.repository.StudyRepository;
import com.codestates.edusync.model.study.study.utils.StudyGetOrder;
import com.codestates.edusync.model.study.studyjoin.service.StudyJoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class StudyService {
    private final StudyRepository repository;
    private final MemberService memberService;
    private final StudyJoinService studyJoinService;
    private final AwsS3Service awsS3Service;
    private final String s3BucketPath = "/study";

    /**
     * 스터디 등록
     * @param study Study
     * @return Study
     */
    public Study create(Study study) {
        return repository.save(study);
    }

    /**
     * 스터디 수정
     * @param study Study
     * @param email String
     * @return Study
     */
    public void update(Study study, String email) {

        Study findStudy = repository.findById(study.getId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STUDYGROUP_NOT_FOUND));

        if (!findStudy.getLeader().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        Optional.ofNullable(study.getStudyName()).ifPresent(findStudy::setStudyName);
        Optional.ofNullable(study.getMemberMin()).ifPresent(findStudy::setMemberMin);
        Optional.ofNullable(study.getMemberMax()).ifPresent(findStudy::setMemberMax);
        Optional.ofNullable(study.getPlatform()).ifPresent(findStudy::setPlatform);
        Optional.ofNullable(study.getIntroduction()).ifPresent(findStudy::setIntroduction);
        Optional.ofNullable(study.getSchedule()).ifPresent(findStudy::setSchedule);
        Optional.ofNullable(study.getTagrefs()).ifPresent(findStudy::setTagrefs);

        repository.save(findStudy);
    }

    /**
     * 스터디 이미지 수정
     * @param email
     * @param studyId
     * @param image
     * @return
     */
    public void updateImage(Long studyId, String email, MultipartFile image) {

        Study findStudy = repository.findById(studyId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STUDYGROUP_NOT_FOUND));

        if (!findStudy.getLeader().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        findStudy.setImage(awsS3Service.uploadImage(image, s3BucketPath));

        repository.save(findStudy);
    }

    /**
     * 스터디 모집상태 수정
     * @param email
     * @param studyId
     * @return
     */
    public void updateStatus(Long studyId, String email) {

        Study findStudy = repository.findById(studyId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STUDYGROUP_NOT_FOUND));

        if (!findStudy.getLeader().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        findStudy.setIsRecruited(!findStudy.getIsRecruited());
        repository.save(findStudy);
    }

    /**
     * 스터디 리더 수정
     * @param studyId
     * @param email
     * @param newLeaderNickName
     */
    public void updateLeader(Long studyId, String email, String newLeaderNickName) {

        Study findStudy = repository.findById(studyId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STUDYGROUP_NOT_FOUND));

        if (!findStudy.getLeader().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        findStudy.setLeader(
                studyJoinService.find(
                        studyId,
                        memberService.getNickName(newLeaderNickName).getId()
                ).getMember()
        );

        repository.save(findStudy);
    }

    /**
     * 스터디 조회
     * @param studyId
     * @return
     */
    @Transactional(readOnly = true)
    public Study get(Long studyId) {

        return repository.findById(studyId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STUDYGROUP_NOT_FOUND));
    }





    @Transactional(readOnly = true)
    public Page<Study> getWithPaging(Integer page, Integer size) {

        StudyGetOrder orderEnum = StudyGetOrder.valueOfOrder("기본값");
        String convertedVariable = orderEnum.getVariable();

        // 오름차순과 내림차순 구분
        Sort sort = Sort.by(convertedVariable);

        if( false )   {
            sort;
        } else {
            sort.descending();
        }

        return repository.findAll(PageRequest.of(page, size, sort));
    }


    @Transactional(readOnly = true)
    public Page<Study> getWithPagingAndOrder(Integer page, Integer size, String order, Boolean isAscending) {

        StudyGetOrder orderEnum = StudyGetOrder.valueOfOrder(order);
        String convertedVariable = orderEnum.getVariable();

        // 오름차순과 내림차순 구분
        Sort sort = Sort.by(convertedVariable);
        if( isAscending ) {
            sort;
        } else {
            sort.descending();
        }

        return repository.findAll(PageRequest.of(page, size, sort));
    }





    @Transactional(readOnly = true)
    public List<Study> getLeaderStudygroupList(Member loginMember) {
        return repository.findAllByLeaderMemberId(loginMember.getId());
    }





    /**
     * 스터디 삭제
     * @param studyId
     * @param email
     */
    public void delete(Long studyId, String email) {

        Study findStudy = repository.findById(studyId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STUDYGROUP_NOT_FOUND));

        if (!findStudy.getLeader().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        repository.deleteById(studyId);
    }

    /**
     * 회원 조회
     * @param email
     * @return
     */
    public Member getMember(String email) {
        return memberService.get(email);
    }

    /**
     * 스터디 멤버 수 조회
     * @param studyId
     * @return
     */
    public int getStudyMemberCount(Long studyId) {
        return studyJoinService.getStudyMemberCount(studyId);
    }
}
