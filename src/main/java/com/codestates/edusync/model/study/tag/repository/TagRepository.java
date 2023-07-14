package com.codestates.edusync.model.study.tag.repository;

import com.codestates.edusync.model.study.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByTagValue(String value);
}
