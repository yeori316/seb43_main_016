package com.codestates.edusync.model.schedule.controller;

import com.codestates.edusync.model.schedule.dto.ScheduleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface ScheduleControllerInterface {

    /**
     * 스케쥴 등록
     * @param postDto SchedulePostDto
     * @param authentication Authentication
     * @return String
     */
    @PostMapping
    ResponseEntity<String> post(Authentication authentication,
                                @Valid @RequestBody ScheduleDto.Post postDto);

    /**
     * 스케쥴 수정
     * @param authentication Authentication
     * @param enScheduleId Encoding Schedule ID
     * @param patchDto SchedulePatchDto
     * @return String
     */
    @PatchMapping("/{schedule-id}")
    ResponseEntity<String> patch(Authentication authentication,
                                 @PathVariable("schedule-id") String enScheduleId,
                                 @Valid @RequestBody ScheduleDto.Patch patchDto);

    /**
     * 스케쥴 상세 정보 조회
     * @param authentication Authentication
     * @param enScheduleId Encoded Schedule ID
     * @return Schedule
     */
    @GetMapping("/{schedule-id}")
    ResponseEntity<ScheduleDto.Response> get(Authentication authentication,
                                             @PathVariable("schedule-id") String enScheduleId);

    /**
     * 스케쥴 리스트 조회
     * @param authentication Authentication
     * @return Schedule List
     */
    @GetMapping("list")
    ResponseEntity<ScheduleDto.ResponseList<List<ScheduleDto.Response>>> getList(Authentication authentication);

    /**
     * 스케쥴 삭제
     * @param authentication Authentication
     * @param enScheduleId Encoded Schedule Id
     * @return String
     */
    @DeleteMapping("/{schedule-id}")
    ResponseEntity<String> delete(Authentication authentication,
                                  @PathVariable("schedule-id") String enScheduleId);
}
