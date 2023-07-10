package com.codestates.edusync.model.study.schedule.entity;

import com.codestates.edusync.model.common.entity.BaseEntity;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.study.entity.Study;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Schedule extends BaseEntity {

    @Column(length = 100, nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Column
    private String color;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "study_id")
    private Study study;
}
