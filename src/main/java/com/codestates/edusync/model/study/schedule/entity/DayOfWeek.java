package com.codestates.edusync.model.study.schedule.entity;

import com.codestates.edusync.model.common.entity.BaseEntity;
import com.codestates.edusync.model.study.study.entity.Study;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class DayOfWeek extends BaseEntity {
    @Column
    private Boolean sunday;

    @Column
    private Boolean monday;

    @Column
    private Boolean tuesday;

    @Column
    private Boolean wednesday;

    @Column
    private Boolean thursday;

    @Column
    private Boolean friday;

    @Column
    private Boolean saturday;

    @OneToOne
    @JoinColumn(name = "study_id")
    private Study study;
}
