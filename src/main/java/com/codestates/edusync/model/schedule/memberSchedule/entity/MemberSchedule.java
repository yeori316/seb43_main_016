package com.codestates.edusync.model.schedule.memberSchedule.entity;

import com.codestates.edusync.model.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class MemberSchedule extends com.codestates.edusync.model.schedule.common.entity.Schedule {
    @Column(length = 100, nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToOne(cascade = {PERSIST, REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "memberDayOfWeekId")
    private MemberDayOfWeek memberDayOfWeek;
}
