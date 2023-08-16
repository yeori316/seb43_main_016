package com.codestates.edusync.model.study.study.controller;

import com.codestates.edusync.model.study.study.dto.StudyDto;
import com.codestates.edusync.model.study.study.dto.StudyPageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

public interface StudyControllerInterface {

    /**
     * 스터디 등록
     * @param authentication Authentication
     * @param postDto StudyPostDto
     * @return URI
     */
    @PostMapping
    ResponseEntity<String> post(Authentication authentication,
                                @Valid @RequestBody StudyDto.Post postDto);

    /**
     * 스터디 수정
     * @param authentication Authentication
     * @param enStudyId Encoded Study ID
     * @param patchDto StudyPatchDto
     * @return String
     */
    @PatchMapping("/{study-id}")
    ResponseEntity<String> patch(Authentication authentication,
                                 @PathVariable("study-id") String enStudyId,
                                 @Valid @RequestBody StudyDto.Patch patchDto);

    /**
     * 스터디 이미지 수정
     * @param authentication Authentication
     * @param enStudyId Encoded Study ID
     * @param image Image File
     * @return String
     */
    @PatchMapping("/{study-id}/image")
    ResponseEntity<String> patchImage(Authentication authentication,
                                      @PathVariable("study-id") String enStudyId,
                                      @RequestPart(value="image") MultipartFile image);

    /**
     * 스터디 모집 상태 수정
     * @param authentication Authentication
     * @param enStudyId Encoded Study ID
     * @return Study Status
     */
    @PatchMapping("/{study-id}/status")
    ResponseEntity<StudyDto.ResponseStatus> patchStatus(Authentication authentication,
                                                        @PathVariable("study-id") String enStudyId);

    /**
     * 스터디 리더 수정
     * @param authentication Authentication
     * @param enStudyId Encoded Study ID
     * @param patchLeaderDto new Leader NickName
     * @return String
     */
    @PatchMapping("/{study-id}/leader")
    ResponseEntity<String> patchLeader(Authentication authentication,
                                       @PathVariable("study-id") String enStudyId,
                                       @RequestBody StudyDto.PatchLeader patchLeaderDto);

    /**
     * 스터디 조회
     * @param authentication Authentication
     * @param enStudyId Encoded Study ID
     * @return Study Response
     */
    @GetMapping("/{study-id}")
    ResponseEntity<StudyDto.Response> get(Authentication authentication,
                                          @PathVariable("study-id") String enStudyId);

    /**
     * 스터디 리스트
     * @param enPage Encoded Page Number
     * @param enSize Encoded Page Size
     * @param sort Sort Order
     * @return Study Page
     */
    @GetMapping("/list")
    ResponseEntity<StudyPageDto.ResponsePage<List<StudyDto.Summary>>> getList(
            @RequestParam("p") String enPage,
            @RequestParam("s") String enSize,
            @RequestParam(value = "sort", required = false) String sort);

    /**
     * 리더로 운영 중인 스터디 리스트 조회
     * @param authentication Authentication
     * @return Study Page
     */
    @GetMapping("/leader/list")
    ResponseEntity<StudyPageDto.ResponseList<List<StudyDto.Summary>>> getLeaderList(
            Authentication authentication);

    /**
     * 가입 신청된 | 가입된 스터디 리스트 조회
     * @param authentication Authentication
     * @return Study Page
     */
    @GetMapping("/join/list")
    ResponseEntity<StudyPageDto.ResponseList<List<StudyDto.Summary>>> getJoinList(
            Authentication authentication,
            @RequestParam("m") String enIsMember);

    /**
     * 스터디 좋아요
     * @param authentication Authentication
     * @param enStudyId Encoded Study ID
     * @return Long
     */
    @PatchMapping("/{study-id}/likes")
    Long patchLikes(Authentication authentication, @PathVariable("study-id") String enStudyId);

    /**
     * 태그 스터디 검색
     * @param enTag Encoded Tag Value
     * @return Study Page
     */
    @GetMapping("/search")
    ResponseEntity<StudyPageDto.ResponseList<List<StudyDto.Summary>>> get(@RequestParam("t") String enTag);

    /**
     * 스터디 삭제
     * @param authentication Authentication
     * @param enStudyId Encoded Study ID
     * @return String
     */
    @DeleteMapping("/{study-id}")
    ResponseEntity<String> delete(Authentication authentication,
                                  @PathVariable("study-id") String enStudyId);
}
