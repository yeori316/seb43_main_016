package com.codestates.edusync.model.study.tag.service;

import com.codestates.edusync.model.study.study.entity.Study;

public interface TagRefServiceInterface {

    /**
     * 태그 삭제
     * @param study
     */
    void delete(Study study);
}
