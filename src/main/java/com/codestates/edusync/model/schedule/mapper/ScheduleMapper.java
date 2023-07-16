package com.codestates.edusync.model.schedule.mapper;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.schedule.dto.ScheduleDto;
import com.codestates.edusync.model.schedule.entity.MemberDayOfWeek;
import com.codestates.edusync.model.schedule.entity.MemberSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleMapper {

    default MemberSchedule schedulePostToSchedule(ScheduleDto.Post schedulePostDto, Member member) {
        MemberSchedule schedule = new MemberSchedule();

        schedule.setMember(member);
        schedule.setTitle(schedulePostDto.getTitle());
        schedule.setDescription(schedulePostDto.getDescription());
        schedule.setStartDate(schedulePostDto.getStartDate());
        schedule.setEndDate(schedulePostDto.getEndDate());
        schedule.setStartTime(schedulePostDto.getStartTime());
        schedule.setEndTime(schedulePostDto.getEndTime());
        schedule.setColor(schedulePostDto.getColor());
        schedule.setMemberDayOfWeek(this.dayOfWeekMapping(schedulePostDto.getDayOfWeek()));

        return schedule;
    }

    default MemberSchedule schedulePatchToSchedule(ScheduleDto.Patch schedulePatchDto) {
        MemberSchedule schedule = new MemberSchedule();

        schedule.setTitle(schedulePatchDto.getTitle());
        schedule.setDescription(schedulePatchDto.getDescription());
        schedule.setStartDate(schedulePatchDto.getStartDate());
        schedule.setEndDate(schedulePatchDto.getEndDate());
        schedule.setStartTime(schedulePatchDto.getStartTime());
        schedule.setEndTime(schedulePatchDto.getEndTime());
        schedule.setColor(schedulePatchDto.getColor());
        schedule.setMemberDayOfWeek(this.dayOfWeekMapping(schedulePatchDto.getDayOfWeek()));

        return schedule;
    }

    default MemberDayOfWeek dayOfWeekMapping(List<Integer> week) {
        MemberDayOfWeek dayOfWeek = new MemberDayOfWeek();

        dayOfWeek.setSunday(week.get(0) == 1);
        dayOfWeek.setMonday(week.get(1) == 1);
        dayOfWeek.setTuesday(week.get(2) == 1);
        dayOfWeek.setWednesday(week.get(3) == 1);
        dayOfWeek.setThursday(week.get(4) == 1);
        dayOfWeek.setFriday(week.get(5) == 1);
        dayOfWeek.setSaturday(week.get(6) == 1);

        return dayOfWeek;
    }
}
