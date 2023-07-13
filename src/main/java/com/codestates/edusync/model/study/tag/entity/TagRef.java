package com.codestates.edusync.model.study.tag.entity;

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
public class TagRef extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studyId")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tagId")
    private Tag tag;
}
