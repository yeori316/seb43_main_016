package com.codestates.edusync.model.study.schedule.mapper;

import com.codestates.edusync.model.study.schedule.dto.ScheduleDto;
import com.codestates.edusync.model.study.schedule.entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleMapper {

    default Schedule schedulePostToSchedule(ScheduleDto.Post schedulePostDto) {
        Schedule schedule = new Schedule();

        schedule.setTitle(schedulePostDto.getTitle());
        schedule.setDescription(schedulePostDto.getDescription());
        schedule.setStartDate(schedulePostDto.getStartDate());
        schedule.setEndDate(schedulePostDto.getEndDate());
        schedule.setStartTime(schedulePostDto.getStartTime());
        schedule.setEndTime(schedulePostDto.getEndTime());
        schedule.setColor(schedulePostDto.getColor());

        return schedule;
    }

    default Schedule schedulePatchToSchedule(ScheduleDto.Patch schedulePatchDto) {
        Schedule schedule = new Schedule();

        schedule.setTitle(schedulePatchDto.getTitle());
        schedule.setDescription(schedulePatchDto.getDescription());
        schedule.setStartDate(schedulePatchDto.getStartDate());
        schedule.setEndDate(schedulePatchDto.getEndDate());
        schedule.setStartTime(schedulePatchDto.getStartTime());
        schedule.setEndTime(schedulePatchDto.getEndTime());
        schedule.setColor(schedulePatchDto.getColor());

        return schedule;
    }

    default ScheduleDto.Response scheduleToResponse(Schedule schedule) {
        ScheduleDto.Response response = new ScheduleDto.Response();

        response.setTitle(schedule.getTitle());
        response.setDescription(schedule.getDescription());
        response.setStartDate(schedule.getStartDate());
        response.setEndDate(schedule.getEndDate());
        response.setStartTime(schedule.getStartTime());
        response.setEndTime(schedule.getEndTime());
        response.setColor(schedule.getColor());

        return response;
    }

    default List<ScheduleDto.Response> schedulesToResponseList(List<Schedule> schedules) {
        return schedules.stream().map(this::scheduleToResponse).collect(Collectors.toList());
    }
}
