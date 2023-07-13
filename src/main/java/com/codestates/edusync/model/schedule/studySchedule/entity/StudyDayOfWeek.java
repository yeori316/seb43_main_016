package com.codestates.edusync.model.schedule.studySchedule.entity;

import com.codestates.edusync.model.schedule.common.entity.DayOfWeek;
import com.codestates.edusync.model.study.study.entity.Study;
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


