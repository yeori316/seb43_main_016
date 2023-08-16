package com.codestates.edusync.model.schedule.service;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.schedule.dto.ScheduleDto;
import com.codestates.edusync.model.schedule.entity.MemberSchedule;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ScheduleServiceInterface {

    /**
     * 스케쥴 등록
     *
     * @param schedule schedule
     */
    void create(MemberSchedule schedule);

    /**
     * 스케쥴 수정
     *
     * @param scheduleId Schedule ID
     * @param member     Member
     * @param schedule   Schedule
     */
    void update(Long scheduleId, Member member, MemberSchedule schedule);

    /**
     * 스케쥴 상세 정보 조회
     *
     * @param scheduleId Schedule ID
     * @param member     Member
     * @return MemberSchedule
     */
    @Transactional(readOnly = true)
    MemberSchedule get(Long scheduleId, Member member);

    /**
     * 스케쥴 상세 정보 DTO 조회
     *
     * @param scheduleId Schedule ID
     * @param member     Member
     * @return Schedule
     */
    @Transactional(readOnly = true)
    ScheduleDto.Response getDto(Long scheduleId, Member member);

    /**
     * 스케쥴 리스트 조회
     *
     * @param email email
     * @return Schedule List
     */
    ScheduleDto.ResponseList<List<ScheduleDto.Response>> getListDto(String email);

    /**
     * 스케쥴 삭제
     *
     * @param scheduleId Schedule ID
     * @param member     Member
     */
    void delete(Long scheduleId, Member member);
}
