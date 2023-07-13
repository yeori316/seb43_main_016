package com.codestates.edusync.model.study.tag.entity;

import com.codestates.edusync.model.common.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Tag extends BaseEntity {
    @Column(nullable = false)
    private String tagValue;

    @OneToMany(mappedBy = "tag" , cascade = {REMOVE})
    private List<TagRef> tagRefs;
}
