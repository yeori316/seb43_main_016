package com.codestates.edusync.model.schedule.controller;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.common.util.ObfuscationUtil;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.service.MemberServiceInterface;
import com.codestates.edusync.model.schedule.dto.ScheduleDto;
import com.codestates.edusync.model.schedule.entity.MemberSchedule;
import com.codestates.edusync.model.schedule.mapper.ScheduleMapper;
import com.codestates.edusync.model.schedule.service.ScheduleServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/schedule")
@Validated
public class MemberScheduleController implements ScheduleControllerInterface {
    private final ScheduleMapper mapper;
    private final ScheduleServiceInterface service;
    private final MemberServiceInterface memberService;
    private final ObfuscationUtil obfuscationUtil;

    @PostMapping
    public ResponseEntity<String> post(Authentication authentication,
                                       @Valid @RequestBody ScheduleDto.Post postDto) {
        Member member = getMember(authentication);
        MemberSchedule schedule = mapper.schedulePostToSchedule(postDto, member);
        service.create(schedule);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{schedule-id}")
    public ResponseEntity<String> patch(Authentication authentication,
                                        @PathVariable("schedule-id") String enScheduleId,
                                        @Valid @RequestBody ScheduleDto.Patch patchDto) {
        Long scheduleId = verifySchedule(enScheduleId);
        Member member = getMember(authentication);
        MemberSchedule memberSchedule = mapper.schedulePatchToSchedule(patchDto);
        service.update(scheduleId, member, memberSchedule);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{schedule-id}")
    public ResponseEntity<ScheduleDto.Response> get(Authentication authentication,
                                                    @PathVariable("schedule-id") String enScheduleId) {
        Long scheduleId = verifySchedule(enScheduleId);
        Member member = getMember(authentication);
        ScheduleDto.Response response = service.getDto(scheduleId, member);
        return ResponseEntity.ok(response);
    }

    @GetMapping("list")
    public ResponseEntity<ScheduleDto.ResponseList<List<ScheduleDto.Response>>> getList(Authentication authentication) {

        String email = authentication.getName();
        ScheduleDto.ResponseList<List<ScheduleDto.Response>> responseList = service.getListDto(email);
        return ResponseEntity.ok(responseList);
    }

    @DeleteMapping("/{schedule-id}")
    public ResponseEntity<String> delete(Authentication authentication,
                                         @PathVariable("schedule-id") String enScheduleId) {
        Long scheduleId = verifySchedule(enScheduleId);
        Member member = getMember(authentication);
        service.delete(scheduleId, member);
        return ResponseEntity.ok().build();
    }

    /**
     * Base64 Decoder
     * @param message Encoded Message
     * @return Decoded Message
     */
    private String getDecoded(String message) {
        return obfuscationUtil.getDecoded(message);
    }

    /**
     * Schedule ID Verify
     * @param enScheduleId Encoded Schedule ID
     * @return Schedule ID
     */
    private Long verifySchedule(String enScheduleId) {

        long scheduleId = Long.parseLong(getDecoded(enScheduleId));

        if (scheduleId < 1) {
            throw new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND);
        }
        return scheduleId;
    }

    /**
     * Member 조회
     * @param authentication Authentication
     * @return Member
     */
    private Member getMember(Authentication authentication) {
        return memberService.get(authentication.getName());
    }
}
