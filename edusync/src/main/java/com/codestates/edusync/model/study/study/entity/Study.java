package com.codestates.edusync.model.study.study.entity;

import com.codestates.edusync.model.common.entity.AuditEntity;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.comment.entity.Comment;
import com.codestates.edusync.model.study.dayofweek.entity.Dayofweek;
import com.codestates.edusync.model.study.schedule.entity.Schedule;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import com.codestates.edusync.model.study.tagref.entity.Tagref;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Study extends AuditEntity {

    @Column(length = 50, nullable = false)
    private String studyName;

    @Column
    private String image;

    @Column
    private Integer memberMin;

    @Column
    private Integer memberMax;

    @Column(length = 50, nullable = false)
    private String platform;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String introduction;

    @Column
    private Boolean isRecruited = false;


    @ManyToOne(cascade = {PERSIST, MERGE}, fetch = EAGER)
    @JoinColumn(name = "member_id")
    private Member leader;

    @OneToMany(mappedBy = "study", cascade = {PERSIST, MERGE, REMOVE})
    private List<StudyJoin> studyJoins;

    @OneToOne(mappedBy = "study", cascade = {PERSIST, MERGE, REMOVE})
    private Schedule schedules;

    @OneToOne(mappedBy = "study", cascade = {PERSIST, MERGE, REMOVE})
    private Dayofweek dayofweek;

    @OneToMany(mappedBy = "study", cascade = {REMOVE})
    private List<Comment> comments;

    @OneToMany(mappedBy = "study", cascade = ALL)
    private List<Tagref> tagrefs;

}