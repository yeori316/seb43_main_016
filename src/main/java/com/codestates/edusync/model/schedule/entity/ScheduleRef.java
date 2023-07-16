package com.codestates.edusync.model.schedule.entity;

import com.codestates.edusync.model.common.entity.BaseEntity;
import com.codestates.edusync.model.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class ScheduleRef extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studyScheduleId")
    private StudySchedule studySchedule;
}
