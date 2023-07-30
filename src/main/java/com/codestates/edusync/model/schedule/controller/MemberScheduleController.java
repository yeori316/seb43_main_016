package com.codestates.edusync.model.schedule.controller;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.common.util.ObfuscationUtil;
import com.codestates.edusync.model.member.service.MemberService;
import com.codestates.edusync.model.schedule.dto.ScheduleDto;
import com.codestates.edusync.model.schedule.mapper.ScheduleMapper;
import com.codestates.edusync.model.schedule.service.MemberScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/schedule")
@Validated
public class MemberScheduleController {
    private final ScheduleMapper mapper;
    private final MemberScheduleService service;
    private final MemberService memberService;
    private final ObfuscationUtil obfuscationUtil;

    /**
     * 스케쥴 등록
     * @param postDto
     * @param authentication
     * @return
     */
    @PostMapping
    public ResponseEntity<String> post(Authentication authentication,
                                       @Valid @RequestBody ScheduleDto.Post postDto) {

        service.create(
                mapper.schedulePostToSchedule(postDto, memberService.get(authentication.getName()))
        );

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
    public ResponseEntity<String> patch(Authentication authentication,
                                        @PathVariable("schedule-id") String enScheduleId,
                                        @Valid @RequestBody ScheduleDto.Patch patchDto) {

        Long scheduleId = verifyId(enScheduleId);

        service.update(
                scheduleId,
                memberService.get(authentication.getName()),
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
    public ResponseEntity<ScheduleDto.Response> get(Authentication authentication,
                                                    @PathVariable("schedule-id") String enScheduleId) {

        Long scheduleId = verifyId(enScheduleId);

        return ResponseEntity.ok(
                service.getDto(scheduleId, memberService.get(authentication.getName()))
        );
    }


    /**
     * 스케쥴 리스트 조회
     * @param authentication
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<ScheduleDto.ResponseList<List<ScheduleDto.Response>>> getList(Authentication authentication) {

        return ResponseEntity.ok(
                service.getListDto(
                        authentication.getName()
                )
        );
    }



    /**
     * 스케쥴 삭제
     * @param authentication
     * @param scheduleId
     * @return
     */
    @DeleteMapping("/{schedule-id}")
    public ResponseEntity<String> delete(Authentication authentication,
                                         @PathVariable("schedule-id") String enScheduleId) {

        Long scheduleId = verifyId(enScheduleId);

        service.delete(scheduleId, memberService.get(authentication.getName()));

        return ResponseEntity.ok().build();
    }

    public String getDecoded(String message) {
        return obfuscationUtil.getDecoded(message);
    }

    private Long verifyId(String enScheduleId) {
        Long scheduleId = Long.parseLong(getDecoded(enScheduleId));

        if (scheduleId < 1) {
            throw new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND);
        }
        return scheduleId;
    }
}
