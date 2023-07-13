package com.codestates.edusync.model.schedule.memberSchedule.repository;

import com.codestates.edusync.model.schedule.common.entity.Schedule;
import com.codestates.edusync.model.schedule.memberSchedule.entity.MemberSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberScheduleRepository extends JpaRepository<MemberSchedule, Long> {
    List<MemberSchedule> findAllByMemberId(Long memberId);
}
