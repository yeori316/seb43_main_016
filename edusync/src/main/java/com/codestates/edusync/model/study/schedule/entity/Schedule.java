package com.codestates.edusync.model.study.schedule.entity;

import com.codestates.edusync.model.common.entity.BaseEntity;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.study.entity.Study;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.EAGER;

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

    @Column
    private String color;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDay;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endDay;

    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startTime;

    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endTime;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "study_id")
    private Study study;

}
