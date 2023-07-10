package com.codestates.edusync.model.study.study.service;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.common.service.AwsS3Service;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.service.MemberManager;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.study.repository.StudyRepository;
import com.codestates.edusync.model.study.study.utils.SortOrder;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import com.codestates.edusync.model.study.studyjoin.repository.StudyJoinRepository;
import com.codestates.edusync.model.study.tag.entity.Tag;
import com.codestates.edusync.model.study.tag.service.TagService;
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
public class StudyService implements StudyManager{
    private final StudyRepository repository;
    private final StudyJoinRepository studyJoinRepository;
    private final MemberManager memberManager;
    private final TagService tagService;
    private final AwsS3Service awsS3Service;

    /**
     * 스터디 등록
     * @param study Study
     * @return Study
     */
    public Study create(Study study) {
//        List<Tag> tagList = tagService.getList(tags);
//
//        study.setTagRefs(tagList.stream().map(e -> {
//            TagRef tagRef = new TagRef();
//            tagRef.setStudy(study);
//            tagRef.setTag(e);
//            return tagRef;
//        }).collect(Collectors.toList()));

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

        if (!findStudy.getMember().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        Optional.ofNullable(study.getStudyName()).ifPresent(findStudy::setStudyName);
        Optional.ofNullable(study.getMemberMin()).ifPresent(findStudy::setMemberMin);
        Optional.ofNullable(study.getMemberMax()).ifPresent(findStudy::setMemberMax);
        Optional.ofNullable(study.getPlatform()).ifPresent(findStudy::setPlatform);
        Optional.ofNullable(study.getIntroduction()).ifPresent(findStudy::setIntroduction);
        Optional.ofNullable(study.getSchedule()).ifPresent(findStudy::setSchedule);
        Optional.ofNullable(study.getTagRefs()).ifPresent(findStudy::setTagRefs);

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

        if (!findStudy.getMember().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        findStudy.setImage(awsS3Service.uploadImage(image, "/study"));

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

        if (!findStudy.getMember().getEmail().equals(email)) {
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

        if (!findStudy.getMember().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        findStudy.setMember(
                this.find(
                        studyId,
                        memberManager.getNickName(newLeaderNickName).getId()
                ).getMember()
        );

        repository.save(findStudy);
    }

    /**
     * 스터디 리더 수정 시
     * @param studyId
     * @param memberId
     * @return
     */
    @Transactional(readOnly = true)
    public StudyJoin find(Long studyId, Long memberId) {
        Optional<StudyJoin> optionalStudyJoin = studyJoinRepository.findByStudyIdAndMemberId(studyId, memberId);

        return optionalStudyJoin.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.STUDYGROUP_PRIVILEGES_MEMBER_NOT_FOUND));
    }

    /**
     * 스터디 조회
     * @param studyId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Study get(Long studyId) {

        return repository.findById(studyId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STUDYGROUP_NOT_FOUND));
    }

    /**
     * 스터디 리스트 조회
     * @param page
     * @param size
     * @param sort
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Study> getList(Integer page, Integer size, String sort) {
        return repository.findAll(
                PageRequest.of(page, size, Sort.by(SortOrder.getString(sort)).descending())
        );
    }

    /**
     * 스터디 삭제
     * @param studyId
     * @param email
     */
    public void delete(Long studyId, String email) {

        Study findStudy = repository.findById(studyId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STUDYGROUP_NOT_FOUND));

        if (!findStudy.getMember().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        repository.deleteById(studyId);
    }

    /**
     * 회원 조회
     * @param email
     * @return
     */
    @Transactional(readOnly = true)
    public Member getMember(String email) {
        return memberManager.get(email);
    }

    /**
     * 스터디 멤버 수 조회
     * @param studyId
     * @return
     */
    @Transactional(readOnly = true)
    public int getStudyMemberCount(Long studyId) {
        return studyJoinRepository.countByStudyIdAndIsApprovedIsTrue(studyId);
    }

    /**
     * 리더인 스터디 리스트 조회
     * @param email
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Study> getLeaderStudyList(String email) {
        return repository.findAllByMemberId(getMember(email));
    }

    /**
     * 태그 리스트 조회
     * @param tagList
     * @return
     */
    public List<Tag> getTagList(List<String> tagList) {
        return tagService.getList(tagList);
    }
}
