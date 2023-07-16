package com.codestates.edusync.model.schedule.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class StudyDayOfWeek extends DayOfWeek {

    @OneToOne(mappedBy="studyDayOfWeek", fetch = FetchType.LAZY)
    private StudySchedule studySchedule;
}


