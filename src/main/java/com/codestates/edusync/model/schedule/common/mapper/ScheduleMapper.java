package com.codestates.edusync.model.schedule.common.mapper;

import com.codestates.edusync.model.schedule.common.dto.ScheduleDto;
import com.codestates.edusync.model.schedule.common.entity.Schedule;
import com.codestates.edusync.model.schedule.memberSchedule.entity.MemberSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleMapper {

    default MemberSchedule schedulePostToSchedule(ScheduleDto.Post schedulePostDto) {
        MemberSchedule schedule = new MemberSchedule();

        schedule.setTitle(schedulePostDto.getTitle());
        schedule.setDescription(schedulePostDto.getDescription());
        schedule.setStartDate(schedulePostDto.getStartDate());
        schedule.setEndDate(schedulePostDto.getEndDate());
        schedule.setStartTime(schedulePostDto.getStartTime());
        schedule.setEndTime(schedulePostDto.getEndTime());
        schedule.setColor(schedulePostDto.getColor());

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

        return schedule;
    }

    default ScheduleDto.Response scheduleToResponse(MemberSchedule schedule) {
        ScheduleDto.Response response = new ScheduleDto.Response();

//        response.setTitle(schedule.getTitle());
//        response.setDescription(schedule.getDescription());
//        response.setStartDate(schedule.getStartDate());
//        response.setEndDate(schedule.getEndDate());
//        response.setStartTime(schedule.getStartTime());
//        response.setEndTime(schedule.getEndTime());
//        response.setColor(schedule.getColor());

        return response;
    }

    default List<ScheduleDto.Response> schedulesToResponseList(List<MemberSchedule> schedules) {
        return schedules.stream().map(this::scheduleToResponse).collect(Collectors.toList());
    }
}
