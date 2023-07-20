package com.codestates.edusync.model.study.likes.repository;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.likes.entity.Likes;
import com.codestates.edusync.model.study.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByMemberAndStudy(Member member, Study study);
}
