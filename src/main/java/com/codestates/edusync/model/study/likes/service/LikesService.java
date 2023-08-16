package com.codestates.edusync.model.study.likes.service;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.likes.entity.Likes;
import com.codestates.edusync.model.study.likes.repository.LikesRepository;
import com.codestates.edusync.model.study.study.entity.Study;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class LikesService implements LikesServiceInterface {
    private final LikesRepository repository;

    public Long patch(Member member, Study study) {

        Optional<Likes> optionalLikes = repository.findByMemberAndStudy(member, study);

        if (optionalLikes.isEmpty()) {
            repository.save(newLikes(member, study));
        } else {
            repository.delete(optionalLikes.get());
        }

        return optionalLikes.isEmpty() ? 1L : 0L;
    }

    /**
     * 좋아요 추가
     *
     * @param member Member
     * @param study  Study
     * @return Like
     */
    public Likes newLikes(Member member, Study study) {
        Likes likes = new Likes();
        likes.setMember(member);
        likes.setStudy(study);
        return likes;
    }
}
