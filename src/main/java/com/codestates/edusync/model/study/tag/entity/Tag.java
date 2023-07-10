package com.codestates.edusync.model.study.tag.entity;

import com.codestates.edusync.model.common.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Tag extends BaseEntity {
    @Column(nullable = false)
    private String value;
}
