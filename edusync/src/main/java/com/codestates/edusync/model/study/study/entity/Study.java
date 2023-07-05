package com.codestates.edusync.model.study.study.entity;

import com.codestates.edusync.model.common.entity.AuditEntity;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.comment.entity.Comment;
import com.codestates.edusync.model.study.schedule.entity.Schedule;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import com.codestates.edusync.model.study.tagref.entity.Tagref;
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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member leader;

    @OneToMany(mappedBy = "study", cascade = {PERSIST, REFRESH, REMOVE})
    private List<StudyJoin> studyJoins;

    @OneToOne(mappedBy = "study", cascade = {PERSIST, MERGE, REFRESH, REMOVE})
    private Schedule schedule;

    @OneToMany(mappedBy = "study", cascade = {REFRESH, REMOVE})
    private List<Comment> comments;

    @OneToMany(mappedBy = "study", cascade = {PERSIST, MERGE, REFRESH, REMOVE})
    private List<Tagref> tagrefs;
}