package com.codestates.edusync.model.study.study.entity;

import com.codestates.edusync.model.common.entity.AuditEntity;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.schedule.entity.StudySchedule;
import com.codestates.edusync.model.study.comment.entity.Comment;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import com.codestates.edusync.model.study.tag.entity.TagRef;
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
    private Boolean isRecruited;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member leader;

    @OneToOne(cascade = ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "studyScheduleId")
    private StudySchedule studySchedule;


    @OneToMany(mappedBy = "study", cascade = {PERSIST, REMOVE})
    private List<StudyJoin> studyJoins;

    @OneToMany(mappedBy = "study", cascade = REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "study", cascade = ALL)
    private List<TagRef> tagRefs;
}