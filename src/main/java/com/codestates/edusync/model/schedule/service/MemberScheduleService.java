package com.codestates.edusync.model.schedule.service;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.service.MemberService;
import com.codestates.edusync.model.schedule.dto.ScheduleDto;
import com.codestates.edusync.model.schedule.entity.MemberSchedule;
import com.codestates.edusync.model.schedule.entity.ScheduleRef;
import com.codestates.edusync.model.schedule.entity.StudySchedule;
import com.codestates.edusync.model.schedule.mapper.ScheduleDtoMapper;
import com.codestates.edusync.model.schedule.repository.MemberScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MemberScheduleService {
    private final MemberScheduleRepository repository;
    private final MemberService memberService;
    private final ScheduleDtoMapper dtoMapper;

    /**
     * 스케쥴 등록
     * @param schedule
     */
    public void create(MemberSchedule schedule) {
        repository.save(schedule);
    }

    /**
     * 스케쥴 수정
     * @param scheduleId
     * @param member
     * @param schedule
     */
    public void update(Long scheduleId, Member member, MemberSchedule schedule) {

        MemberSchedule findSchedule =
                repository.findById(scheduleId)
                        .orElseThrow(() -> new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND));

        if (!findSchedule.getMember().getEmail().equals(member.getEmail())) {
            throw new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND);
        }

        Optional.ofNullable(schedule.getTitle()).ifPresent(findSchedule::setTitle);
        Optional.ofNullable(schedule.getDescription()).ifPresent(findSchedule::setDescription);
        Optional.ofNullable(schedule.getStartDate()).ifPresent(findSchedule::setStartDate);
        Optional.ofNullable(schedule.getEndDate()).ifPresent(findSchedule::setEndDate);
        Optional.ofNullable(schedule.getStartTime()).ifPresent(findSchedule::setStartTime);
        Optional.ofNullable(schedule.getEndTime()).ifPresent(findSchedule::setEndTime);
        Optional.ofNullable(schedule.getColor()).ifPresent(findSchedule::setColor);
        Optional.ofNullable(schedule.getMemberDayOfWeek()).ifPresent(e -> {
            if (e.getSunday() != null) findSchedule.getMemberDayOfWeek().setSunday(e.getSunday());
            if (e.getMonday() != null) findSchedule.getMemberDayOfWeek().setMonday(e.getMonday());
            if (e.getTuesday() != null) findSchedule.getMemberDayOfWeek().setTuesday(e.getTuesday());
            if (e.getWednesday() != null) findSchedule.getMemberDayOfWeek().setWednesday(e.getWednesday());
            if (e.getThursday() != null) findSchedule.getMemberDayOfWeek().setThursday(e.getThursday());
            if (e.getFriday() != null) findSchedule.getMemberDayOfWeek().setFriday(e.getFriday());
            if (e.getSaturday() != null) findSchedule.getMemberDayOfWeek().setSaturday(e.getSaturday());
        });

        repository.save(findSchedule);
    }


    /**
     * 스케쥴 상세 정보 조회
     * @param scheduleId
     * @param member
     * @return
     */
    @Transactional(readOnly = true)
    public MemberSchedule get(Long scheduleId, Member member) {

        MemberSchedule findSchedule =
                repository.findById(scheduleId)
                        .orElseThrow(() -> new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND));

        if (!findSchedule.getMember().getEmail().equals(member.getEmail())) {
            throw new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND);
        }

        return findSchedule;
    }

    /**
     * 스케쥴 상세 정보 조회
     * @param scheduleId
     * @param member
     * @return
     */
    @Transactional(readOnly = true)
    public ScheduleDto.Response getDto(Long scheduleId, Member member) {
        return dtoMapper.scheduleToResponse(get(scheduleId, member));
    }

    /**
     * 스케쥴 리스트 조회
     * @param member
     * @return
     */
    //@Transactional(readOnly = true)
    public ScheduleDto.ResponseList<List<ScheduleDto.Response>> getListDto(String email) {

        Member member = memberService.get(email);

        List<StudySchedule> studyScheduleList =
                member.getScheduleRefs().stream().map(ScheduleRef::getStudySchedule).collect(Collectors.toList());

        List<ScheduleDto.Response> scheduleDtoList = new ArrayList<>();
        scheduleDtoList.addAll(dtoMapper.memberSchedulesToResponseList(repository.findAllByMember(member)));
        scheduleDtoList.addAll(dtoMapper.studySchedulesToResponseList(studyScheduleList));

        return new ScheduleDto.ResponseList<>(scheduleDtoList);
    }

    /**
     * 스케쥴 삭제
     * @param scheduleId
     * @param member
     */
    public void delete(Long scheduleId, Member member) {

        MemberSchedule findSchedule = get(scheduleId, member);

        repository.delete(findSchedule);
    }
}
