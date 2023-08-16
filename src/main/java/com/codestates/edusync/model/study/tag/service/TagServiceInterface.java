package com.codestates.edusync.model.study.tag.service;

import com.codestates.edusync.model.study.study.dto.StudyDto;
import com.codestates.edusync.model.study.study.dto.StudyPageDto;
import com.codestates.edusync.model.study.tag.entity.Tag;

import java.util.List;

public interface TagServiceInterface {
    /**
     * 태그 등록
     * @param value Tag Value
     * @return Tag Value
     */
    Tag create(String value);

    /**
     * 태그 조회
     * @param value Tag Value
     * @return Tag Value
     */
    Tag get(String value);

    /**
     * 태그 리스트 조회
     * @param tags Tag Value List
     * @return Tag List
     */
    List<Tag> getList(List<String> tags);

    /**
     * 태그로 스터디 검색
     * @param value Tag Value
     * @return Study List
     */
    StudyPageDto.ResponseList<List<StudyDto.Summary>> search(String value);
}
