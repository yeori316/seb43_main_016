package com.codestates.edusync.model.study.tag.repository;

import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.tag.entity.TagRef;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRefRepository extends JpaRepository<TagRef, Long> {
    void deleteByStudy(Study Study);
}
