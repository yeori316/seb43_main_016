package com.codestates.edusync.model.common.utils;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Component
public class VerifyStudygroupUtils {
    private final StudyRepository studyRepository;

    // studyService.get()
    public Study findVerifyStudygroup(Long studygroupId) {
        return studyRepository.findById(studygroupId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STUDYGROUP_NOT_FOUND));
    }

    public boolean isMemberLeaderOfStudygroup(String email, Long studygroupId) {
        Study findStudy = studyRepository.findById(studygroupId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STUDYGROUP_NOT_FOUND));

        return findStudy.getLeader().getEmail().equals(email);
    }
}