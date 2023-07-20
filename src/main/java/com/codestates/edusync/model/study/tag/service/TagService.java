package com.codestates.edusync.model.study.tag.service;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.study.study.dto.StudyDto;
import com.codestates.edusync.model.study.study.dto.StudyPageDto;
import com.codestates.edusync.model.study.study.mapper.StudyDtoMapper;
import com.codestates.edusync.model.study.tag.entity.Tag;
import com.codestates.edusync.model.study.tag.entity.TagRef;
import com.codestates.edusync.model.study.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class TagService {
    private final TagRepository repository;
    private final StudyDtoMapper studyDtoMapper;

    public Tag create(String value) {
        Tag findTag = get(value);
        if (findTag == null) {
            Tag tag = new Tag();
            tag.setTagValue(value);
            return repository.save(tag);
        }
        return findTag;
    }

    public Tag get(String value) {
        return repository.findByTagValue(value);
    }

    public List<Tag> getList(List<String> tags) {
        return tags.stream().map(this::create).collect(Collectors.toList());
    }

    public StudyPageDto.ResponseList<List<StudyDto.Summary>> search(String value) {

        Tag tag = get(value);
        if (tag == null) throw new BusinessLogicException(ExceptionCode.STUDY_NOT_FOUND);

        List<TagRef> tagList = tag.getTagRefs();

        return new StudyPageDto.ResponseList<>(
                studyDtoMapper.studyListToResponseList(
                        tagList.stream().map(TagRef::getStudy).collect(Collectors.toList())
                )
        );
    }
}
