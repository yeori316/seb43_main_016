package com.codestates.edusync.model.study.tag.service;

import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.tag.repository.TagRefRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class TagRefService {
    private final TagRefRepository repository;

    public void delete(Study study) {
        repository.deleteByStudy(study);
    }
}
