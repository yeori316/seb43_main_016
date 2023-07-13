package com.codestates.edusync.model.study.tag.service;

import com.codestates.edusync.model.study.tag.entity.Tag;
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
}
