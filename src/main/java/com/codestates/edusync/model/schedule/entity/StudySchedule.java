package com.codestates.edusync.model.schedule.entity;

import com.codestates.edusync.model.study.study.entity.Study;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class StudySchedule extends Schedule {
    @OneToOne(cascade = ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "studyDayOfWeekId")
    private StudyDayOfWeek studyDayOfWeek;

    @OneToMany(mappedBy = "studySchedule", cascade = {PERSIST, REMOVE})
    private List<ScheduleRef> ScheduleRefs;

    @OneToOne(mappedBy = "studySchedule", fetch = FetchType.LAZY)
    private Study study;
}
