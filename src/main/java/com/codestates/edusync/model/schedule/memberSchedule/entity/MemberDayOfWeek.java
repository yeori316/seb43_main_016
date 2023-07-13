package com.codestates.edusync.model.schedule.memberSchedule.entity;

import com.codestates.edusync.model.schedule.common.entity.DayOfWeek;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class MemberDayOfWeek extends DayOfWeek {
}
