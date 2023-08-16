package com.codestates.edusync.model.schedule.service;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.service.MemberServiceInterface;
import com.codestates.edusync.model.schedule.dto.ScheduleDto;
import com.codestates.edusync.model.schedule.entity.MemberDayOfWeek;
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
public class MemberScheduleService implements ScheduleServiceInterface {
    private final MemberScheduleRepository repository;
    private final MemberServiceInterface memberService;
    private final ScheduleDtoMapper dtoMapper;

    public void create(MemberSchedule schedule) {
        repository.save(schedule);
    }

    public void update(Long scheduleId, Member member, MemberSchedule schedule) {

        MemberSchedule findSchedule = repository.findById(scheduleId)
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
            MemberDayOfWeek memberDayOfWeek = findSchedule.getMemberDayOfWeek();

            Optional.ofNullable(e.getSunday()).ifPresent(memberDayOfWeek::setSunday);
            Optional.ofNullable(e.getMonday()).ifPresent(memberDayOfWeek::setMonday);
            Optional.ofNullable(e.getTuesday()).ifPresent(memberDayOfWeek::setTuesday);
            Optional.ofNullable(e.getWednesday()).ifPresent(memberDayOfWeek::setWednesday);
            Optional.ofNullable(e.getThursday()).ifPresent(memberDayOfWeek::setThursday);
            Optional.ofNullable(e.getFriday()).ifPresent(memberDayOfWeek::setFriday);
            Optional.ofNullable(e.getSaturday()).ifPresent(memberDayOfWeek::setSaturday);
        });

        repository.save(findSchedule);
    }

    @Transactional(readOnly = true)
    public MemberSchedule get(Long scheduleId, Member member) {

        MemberSchedule findSchedule = repository.findById(scheduleId)
                        .orElseThrow(() -> new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND));

        if (!findSchedule.getMember().getEmail().equals(member.getEmail())) {
            throw new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND);
        }

        return findSchedule;
    }

    @Transactional(readOnly = true)
    public ScheduleDto.Response getDto(Long scheduleId, Member member) {
        MemberSchedule findSchedule = get(scheduleId, member);
        return dtoMapper.scheduleToResponse(findSchedule);
    }

    public ScheduleDto.ResponseList<List<ScheduleDto.Response>> getListDto(String email) {

        Member member = memberService.get(email);

        List<MemberSchedule> memberScheduleList = repository.findAllByMember(member);
        List<ScheduleDto.Response> memberScheduleResponseList = dtoMapper.memberSchedulesToResponseList(memberScheduleList);

        List<StudySchedule> studyScheduleList =
                member.getScheduleRefs().stream().map(ScheduleRef::getStudySchedule).collect(Collectors.toList());

        List<ScheduleDto.Response> studyScheduleResponseList = dtoMapper.studySchedulesToResponseList(studyScheduleList);

        return new ScheduleDto.ResponseList<>(memberScheduleResponseList, studyScheduleResponseList);
    }

    public void delete(Long scheduleId, Member member) {
        MemberSchedule findSchedule = get(scheduleId, member);
        repository.delete(findSchedule);
    }
}
