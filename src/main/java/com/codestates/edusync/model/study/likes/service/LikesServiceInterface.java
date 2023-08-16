package com.codestates.edusync.model.study.likes.service;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.study.entity.Study;

public interface LikesServiceInterface {

    /**
     * 좋아요 설정 및 해제
     * @param member Member
     * @param study Study
     * @return long
     */
    Long patch(Member member, Study study);
}
