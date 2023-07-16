package com.codestates.edusync.model.schedule.repository;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.schedule.entity.MemberSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberScheduleRepository extends JpaRepository<MemberSchedule, Long> {
    List<MemberSchedule> findAllByMember(Member member);
}
