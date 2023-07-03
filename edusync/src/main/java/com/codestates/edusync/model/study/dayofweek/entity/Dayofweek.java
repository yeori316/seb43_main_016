package com.codestates.edusync.model.study.dayofweek.entity;


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
public class Dayofweek extends BaseEntity {

    @Column(nullable = false)
    private Boolean sunday;

    @Column(nullable = false)
    private Boolean monday;

    @Column(nullable = false)
    private Boolean tuesday;

    @Column(nullable = false)
    private Boolean wednesday;

    @Column(nullable = false)
    private Boolean thursday;

    @Column(nullable = false)
    private Boolean friday;

    @Column(nullable = false)
    private Boolean saturday;


    @OneToOne
    @JoinColumn(name = "study_id")
    private Study study;

}
