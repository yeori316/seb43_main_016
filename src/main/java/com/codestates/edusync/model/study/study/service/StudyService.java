package com.codestates.edusync.model.study.study.service;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.common.service.AwsS3Service;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.service.MemberService;
import com.codestates.edusync.model.schedule.entity.DayOfWeek;
import com.codestates.edusync.model.schedule.entity.StudySchedule;
import com.codestates.edusync.model.study.likes.entity.Likes;
import com.codestates.edusync.model.study.likes.repository.LikesRepository;
import com.codestates.edusync.model.study.study.dto.StudyDto;
import com.codestates.edusync.model.study.study.dto.StudyPageDto;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.study.mapper.StudyDtoMapper;
import com.codestates.edusync.model.study.study.repository.StudyRepository;
import com.codestates.edusync.model.study.study.utils.SortOrder;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import com.codestates.edusync.model.study.studyjoin.repository.StudyJoinRepository;
import com.codestates.edusync.model.study.tag.service.TagRefService;
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
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class StudyService {
    private final StudyRepository repository;
    private final StudyJoinRepository joinRepository;
    private final LikesRepository likesRepository;
    private final MemberService memberService;
    private final TagRefService tagRefService;
    private final AwsS3Service awsS3Service;
    private final StudyDtoMapper dtoMapper;


    /**
     * 스터디 등록
     * @param study Study
     */
    public void create(Study study) {

        Optional<Study> optionalStudy = repository.findByStudyName(study.getStudyName());

        if (optionalStudy.isPresent())
            throw new BusinessLogicException(ExceptionCode.STUDY_NAME_EXISTS);

        repository.save(study);
    }

    /**
     * 스터디 수정
     * @param study Study
     * @param email String
     */
    public void update(Study study, String email) {

        Study findStudy = verifyLeader(study.getId(), email);

        Optional.ofNullable(study.getStudyName()).ifPresent(findStudy::setStudyName);
        Optional.ofNullable(study.getMemberMin()).ifPresent(findStudy::setMemberMin);
        Optional.ofNullable(study.getMemberMax()).ifPresent(findStudy::setMemberMax);
        Optional.ofNullable(study.getPlatform()).ifPresent(findStudy::setPlatform);
        Optional.ofNullable(study.getIntroduction()).ifPresent(findStudy::setIntroduction);
        Optional.ofNullable(study.getStudySchedule()).ifPresent(e -> {
            StudySchedule findStudySchedule = findStudy.getStudySchedule();

            Optional.ofNullable(e.getTitle()).ifPresent(findStudySchedule::setTitle);
            Optional.ofNullable(e.getDescription()).ifPresent(findStudySchedule::setDescription);
            Optional.ofNullable(e.getStartDate()).ifPresent(findStudySchedule::setStartDate);
            Optional.ofNullable(e.getEndDate()).ifPresent(findStudySchedule::setEndDate);
            Optional.ofNullable(e.getStartTime()).ifPresent(findStudySchedule::setStartTime);
            Optional.ofNullable(e.getEndTime()).ifPresent(findStudySchedule::setEndTime);
            Optional.ofNullable(e.getColor()).ifPresent(findStudySchedule::setColor);
            Optional.ofNullable(e.getStudyDayOfWeek()).ifPresent(el -> {
                DayOfWeek dayOfWeek = findStudy.getStudySchedule().getStudyDayOfWeek();

                Optional.ofNullable(el.getSunday()).ifPresent(dayOfWeek::setSunday);
                Optional.ofNullable(el.getMonday()).ifPresent(dayOfWeek::setMonday);
                Optional.ofNullable(el.getTuesday()).ifPresent(dayOfWeek::setTuesday);
                Optional.ofNullable(el.getWednesday()).ifPresent(dayOfWeek::setWednesday);
                Optional.ofNullable(el.getThursday()).ifPresent(dayOfWeek::setThursday);
                Optional.ofNullable(el.getFriday()).ifPresent(dayOfWeek::setFriday);
                Optional.ofNullable(el.getSaturday()).ifPresent(dayOfWeek::setSaturday);
            });
        });

        Optional.ofNullable(study.getTagRefs()).ifPresent(e -> {
            tagRefService.delete(findStudy); // TODO Tag 삭제 말고 수정하는 방법은 없나?
            findStudy.setTagRefs(e);
        });

        repository.save(findStudy);
    }

    /**
     * 스터디 이미지 수정
     * @param email Email
     * @param studyId Study ID
     * @param image Image File
     */
    public void updateImage(Long studyId, String email, MultipartFile image) {

        Study findStudy = verifyLeader(studyId, email);
        String imageAddress = awsS3Service.uploadImage(image, "/study");
        findStudy.setImage(imageAddress);

        repository.save(findStudy);
    }

    /**
     * 스터디 모집 상태 수정
     * @param email Email
     * @param studyId Study ID
     */
    public Boolean updateStatus(Long studyId, String email) {

        Study findStudy = verifyLeader(studyId, email);
        findStudy.setIsRecruited(!findStudy.getIsRecruited());

        return repository.save(findStudy).getIsRecruited();
    }

    /**
     * 스터디 리더 수정
     * @param studyId Study ID
     * @param email Email
     * @param newLeaderNickName new Leaader NickName
     */
    public void updateLeader(Long studyId, String email, String newLeaderNickName) {

        getMember(email);
        Study findStudy = verifyLeader(studyId, email);
        Member newLeader = memberService.getNickName(newLeaderNickName);

        joinRepository.findByStudyIdAndMemberIdAndIsApprovedTrue(studyId, newLeader.getId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        findStudy.setLeader(newLeader);
        repository.save(findStudy);
    }

    /**
     * 스터디 조회
     * @param studyId Study ID
     * @return Study
     */
    @Transactional(readOnly = true)
    public Study get(Long studyId) {

        return repository.findById(studyId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STUDY_NOT_FOUND));
    }

    /**
     * 스터디 조회 DTO 반환
     * @param studyId Study ID
     * @param email Email
     * @return Study
     */
    @Transactional(readOnly = true)
    public StudyDto.Response getDto(Long studyId, String email) {

        // 멤버 확인용
        Member member = getMember(email);
        Study study = get(studyId);

        study.setViews(study.getViews() + 1);
        repository.save(study);

        Optional<Likes> likes = likesRepository.findByMemberAndStudy(member, study);

        int cnt = joinRepository.countByStudyAndIsApprovedIsTrue(study);
        boolean isLeader = study.getLeader().getEmail().equals(email);
        boolean isLike = likes.isPresent();

        return dtoMapper.studyToResponse(study, cnt, isLeader, isLike);
    }

    /**
     * 스터디 리스트 조회 DTO 반환
     * @param page Page Number
     * @param size Page Size
     * @param sort Page Sort Order
     * @return Study Page
     */
    @Transactional(readOnly = true)
    public StudyPageDto.ResponsePage<List<StudyDto.Summary>> getPageDto(Integer page, Integer size, String sort) {

        Page<Study> studyPage = repository.findAll(
                PageRequest.of(page, size, Sort.by(SortOrder.getString(sort)).descending())
        );

        List<StudyDto.Summary> responseList =
                dtoMapper.studyListToResponseList(studyPage.getContent());

        return new StudyPageDto.ResponsePage<>(responseList, studyPage);
    }

    /**
     * 리더인 스터디 리스트 조회 DTO 반환
     * @param email Email
     * @return Study List
     */
    @Transactional(readOnly = true)
    public StudyPageDto.ResponseList<List<StudyDto.Summary>> getLeaderStudyListDto(String email) {

        Member member = getMember(email);
        List<Study> studyList = repository.findAllByLeader(member);
        List<StudyDto.Summary> studyResponseList = dtoMapper.studyListToResponseList(studyList);

        return new StudyPageDto.ResponseList<>(studyResponseList);
    }

    /**
     * 가입 신청된 | 가입된 스터디 리스트 조회 DTO 반환
     * @param email Email
     * @return Study List
     */
    @Transactional(readOnly = true)
    public StudyPageDto.ResponseList<List<StudyDto.Summary>> getJoinListDto(String email, Boolean isMember) {

        List<Study> studyList;
        Member member = getMember(email);

        if (Boolean.TRUE.equals(isMember)) {
            studyList = joinRepository.findAllByMemberAndIsApprovedIsTrue(member)
                    .stream()
                    .map(StudyJoin::getStudy)
                    .collect(Collectors.toList());
        } else {
            studyList = joinRepository.findAllByMemberAndIsApprovedIsFalse(member)
                    .stream()
                    .map(StudyJoin::getStudy)
                    .collect(Collectors.toList());
        }

        List<StudyDto.Summary> StudyResponseList = dtoMapper.studyListToResponseList(studyList);

        return new StudyPageDto.ResponseList<>(StudyResponseList);
    }


    /**
     * 스터디 삭제
     * @param studyId Study ID
     * @param email Email
     */
    public void delete(Long studyId, String email) {
        verifyLeader(studyId, email);
        repository.deleteById(studyId);
    }

    /**
     * 스터디 조회 및 리더 권한 확인
     * @param studyId Study ID
     * @param email Email
     * @return Study
     */
    private Study verifyLeader(Long studyId, String email) {
        Study findStudy = get(studyId);

        if (!findStudy.getLeader().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
        }

        return findStudy;
    }

    /**
     * Member 조회
     * @param email Email
     * @return Member
     */
    private Member getMember(String email) {
        return memberService.get(email);
    }
}
