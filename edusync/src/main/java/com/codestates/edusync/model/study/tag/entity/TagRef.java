package com.codestates.edusync.model.study.tag.entity;

import com.codestates.edusync.model.common.entity.BaseEntity;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.tag.entity.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class TagRef extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
