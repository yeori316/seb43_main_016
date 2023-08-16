package com.codestates.edusync.model.study.study.service;

import com.codestates.edusync.model.study.study.dto.StudyDto;
import com.codestates.edusync.model.study.study.dto.StudyPageDto;
import com.codestates.edusync.model.study.study.entity.Study;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudyServiceInterface {

    /**
     * 스터디 등록
     * @param study Study
     */
    void create(Study study);

    /**
     * 스터디 수정
     * @param study Study
     * @param email String
     */
    void update(Study study, String email);

    /**
     * 스터디 이미지 수정
     * @param email Email
     * @param studyId Study ID
     * @param image Image File
     */
    void updateImage(Long studyId, String email, MultipartFile image);

    /**
     * 스터디 모집 상태 수정
     * @param email Email
     * @param studyId Study ID
     */
    Boolean updateStatus(Long studyId, String email);

    /**
     * 스터디 리더 수정
     * @param studyId Study ID
     * @param email Email
     * @param newLeaderNickName new Leaader NickName
     */
    void updateLeader(Long studyId, String email, String newLeaderNickName);

    /**
     * 스터디 조회
     * @param studyId Study ID
     * @return Study
     */
    @Transactional(readOnly = true)
    Study get(Long studyId);

    /**
     * 스터디 조회 DTO 반환
     * @param studyId Study ID
     * @param email Email
     * @return Study
     */
    @Transactional(readOnly = true)
    StudyDto.Response getDto(Long studyId, String email);

    /**
     * 스터디 리스트 조회 DTO 반환
     * @param page Page Number
     * @param size Page Size
     * @param sort Page Sort Order
     * @return Study Page
     */
    @Transactional(readOnly = true)
    StudyPageDto.ResponsePage<List<StudyDto.Summary>> getPageDto(Integer page, Integer size, String sort);

    /**
     * 리더인 스터디 리스트 조회 DTO 반환
     * @param email Email
     * @return Study List
     */
    @Transactional(readOnly = true)
    StudyPageDto.ResponseList<List<StudyDto.Summary>> getLeaderStudyListDto(String email);

    /**
     * 가입 신청된 | 가입된 스터디 리스트 조회 DTO 반환
     * @param email Email
     * @return Study List
     */
    @Transactional(readOnly = true)
    StudyPageDto.ResponseList<List<StudyDto.Summary>> getJoinListDto(String email, Boolean isMember);

    /**
     * 스터디 삭제
     * @param studyId Study ID
     * @param email Email
     */
    void delete(Long studyId, String email);
}
