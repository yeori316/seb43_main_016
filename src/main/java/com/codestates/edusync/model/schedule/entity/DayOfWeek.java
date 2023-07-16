package com.codestates.edusync.model.schedule.entity;

import com.codestates.edusync.model.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
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
}
