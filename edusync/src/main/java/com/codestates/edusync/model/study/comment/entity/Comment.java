package com.codestates.edusync.model.study.comment.entity;

import com.codestates.edusync.model.common.entity.AuditEntity;
import com.codestates.edusync.model.member.entity.Member;
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
public class Comment extends AuditEntity {
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
