package com.codestates.edusync.model.study.study.service;

import com.codestates.edusync.model.study.study.entity.Study;

import java.util.List;

public interface StudyManager {
    Study get(Long studyId);

    List<Study> getLeaderStudyList(String email);
}
