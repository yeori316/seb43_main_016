package com.codestates.edusync.model.schedule.entity;

import com.codestates.edusync.model.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class MemberSchedule extends Schedule {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToOne(cascade = ALL)
    @JoinColumn(name = "memberDayOfWeekId")
    private MemberDayOfWeek memberDayOfWeek;
}
