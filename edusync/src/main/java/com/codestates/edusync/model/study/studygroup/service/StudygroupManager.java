package com.codestates.edusync.model.study.studygroup.service;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.studygroup.entity.Studygroup;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudygroupManager {

    /**
     * 스터디 등록
     * @param studygroup
     * @param loginMember
     * @return
     */
    Studygroup create(Studygroup studygroup, Member loginMember);

    /**
     * 스터디 정보 수정
     * @param email
     * @param studygroup
     * @return
     */
    Studygroup update(String email, Studygroup studygroup);

    /**
     * 스터디 모집 상태 수정
     * @param email
     * @param studygroupId
     */
    boolean updateStatus(String email, Long studygroupId);

    /**
     * 스터디 조회
     * @param studygroupId
     * @return
     */
    Studygroup get(Long studygroupId);

    /**
     * 스터디 리스트 조회
     * @param page
     * @param size
     * @return
     */
    Page<Studygroup> getWithPaging(Integer page, Integer size);

    /**
     * 본인이 스터디 리더인 그룹 리스트
     * @param email
     * @return
     */
    List<Studygroup> getLeaderStudygroupList(Member loginMember);

    /**
     * 스터디 삭제
     * @param email
     * @param studygroupId
     */
    void delete(String email, Long studygroupId);

    /**
     * 스터디 리더 권한 이전
     * @param email
     * @param studygroupId
     * @param newLeader
     */
    void patchLeader(String email, Long studygroupId, String newLeader);
}
