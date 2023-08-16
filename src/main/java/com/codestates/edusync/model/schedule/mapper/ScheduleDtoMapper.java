package com.codestates.edusync.model.schedule.mapper;

import com.codestates.edusync.model.schedule.dto.ScheduleDto;
import com.codestates.edusync.model.schedule.entity.MemberDayOfWeek;
import com.codestates.edusync.model.schedule.entity.MemberSchedule;
import com.codestates.edusync.model.schedule.entity.StudyDayOfWeek;
import com.codestates.edusync.model.schedule.entity.StudySchedule;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleDtoMapper {

    default ScheduleDto.Response scheduleToResponse(MemberSchedule schedule) {
        ScheduleDto.Response response = new ScheduleDto.Response();

        response.setId(schedule.getId());
        response.setTitle(schedule.getTitle());
        response.setDescription(schedule.getDescription());
        response.setStartDate(schedule.getStartDate());
        response.setEndDate(schedule.getEndDate());
        response.setStartTime(schedule.getStartTime());
        response.setEndTime(schedule.getEndTime());
        response.setColor(schedule.getColor());
        response.setDayOfWeek(this.responseDayOfWeek(schedule.getMemberDayOfWeek()));

        return response;
    }

    default ScheduleDto.Response scheduleToResponse(StudySchedule schedule) {
        ScheduleDto.Response response = new ScheduleDto.Response();

        response.setId(schedule.getId());
        response.setTitle(schedule.getTitle());
        response.setDescription(schedule.getDescription());
        response.setStartDate(schedule.getStartDate());
        response.setEndDate(schedule.getEndDate());
        response.setStartTime(schedule.getStartTime());
        response.setColor(schedule.getColor());
        response.setDayOfWeek(this.responseDayOfWeek(schedule.getStudyDayOfWeek()));

        return response;
    }

    /**
     * 멤버 스켜쥴 조회 시
     * @param studyDayOfWeek
     * @return
     */
    default List<Integer> responseDayOfWeek(MemberDayOfWeek studyDayOfWeek) {
        List<Integer> response = new ArrayList<>();

        response.add(Boolean.TRUE.equals(studyDayOfWeek.getSunday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getMonday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getTuesday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getWednesday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getThursday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getFriday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getSaturday()) ? 1 : 0);

        return response;
    }

    /**
     * 멤버 스켜쥴 조회 시
     * @param studyDayOfWeek
     * @return
     */
    default List<Integer> responseDayOfWeek(StudyDayOfWeek studyDayOfWeek) {
        List<Integer> response = new ArrayList<>();

        response.add(Boolean.TRUE.equals(studyDayOfWeek.getSunday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getMonday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getTuesday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getWednesday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getThursday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getFriday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getSaturday()) ? 1 : 0);

        return response;
    }


    default List<ScheduleDto.Response> memberSchedulesToResponseList(List<MemberSchedule> schedules) {
        return schedules.stream().map(this::scheduleToResponse).collect(Collectors.toList());
    }

    default List<ScheduleDto.Response> studySchedulesToResponseList(List<StudySchedule> schedules) {
        return schedules.stream().map(this::scheduleToResponse).collect(Collectors.toList());
    }
}
