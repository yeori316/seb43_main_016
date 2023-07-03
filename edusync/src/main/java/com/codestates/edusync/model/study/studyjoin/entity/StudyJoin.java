package com.codestates.edusync.model.study.studyjoin.entity;

import com.codestates.edusync.model.common.entity.AuditEntity;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.study.entity.Study;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.MERGE;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class StudyJoin extends AuditEntity {

    @Column
    private Boolean isApproved = false;


    @ManyToOne(cascade = {MERGE})
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(cascade = {MERGE})
    @JoinColumn(name = "study_id")
    private Study study;

}
