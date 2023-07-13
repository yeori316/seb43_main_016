package com.codestates.edusync.model.schedule.memberSchedule.controller;

import com.codestates.edusync.model.schedule.common.mapper.ScheduleMapper;
import com.codestates.edusync.model.schedule.memberSchedule.service.MemberScheduleService;
import com.codestates.edusync.model.schedule.common.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/schedule")
@Validated
public class MemberScheduleController {
    private final ScheduleMapper mapper;
    private final MemberScheduleService service;

    /**
     * 스케쥴 추가
     * @param postDto
     * @param authentication
     * @return
     */
    @PostMapping
    public ResponseEntity post(Authentication authentication,
                               @Valid @RequestBody ScheduleDto.Post postDto) {

        service.create(mapper.schedulePostToSchedule(postDto), authentication.getName());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 스케쥴 수정
     * @param scheduleId
     * @param patchDto
     * @param authentication
     * @return
     */
    @PatchMapping("/{schedule-id}")
    public ResponseEntity patch(Authentication authentication,
                                @PathVariable("schedule-id") @Positive Long scheduleId,
                                @Valid @RequestBody ScheduleDto.Patch patchDto) {

        service.update(scheduleId, authentication.getName(),
                mapper.schedulePatchToSchedule(patchDto)
        );

        return ResponseEntity.ok().build();
    }

    /**
     * 스케쥴 상세 정보 조회
     * @param scheduleId
     * @return
     */
    @GetMapping("/{schedule-id}")
    public ResponseEntity get(Authentication authentication,
                              @PathVariable("schedule-id") @Positive Long scheduleId) {

        return ResponseEntity.ok(
                mapper.scheduleToResponse(
                        service.get(scheduleId, authentication.getName())));
    }


    /**
     * 스케쥴 리스트 조회
     * @param authentication
     * @return
     */
    @GetMapping("list")
    public ResponseEntity getList(Authentication authentication) {

        return ResponseEntity.ok(
                mapper.schedulesToResponseList(
                        service.getList(authentication.getName())));
    }



    /**
     * 스케쥴 삭제
     * @param timeScheduleId
     * @param authentication
     * @return
     */
    @DeleteMapping("/{schedule-id}")
    public ResponseEntity delete(Authentication authentication,
                                 @PathVariable("schedule-id") @Positive Long timeScheduleId) {

        service.delete(timeScheduleId, authentication.getName());

        return ResponseEntity.ok().build();
    }
}
