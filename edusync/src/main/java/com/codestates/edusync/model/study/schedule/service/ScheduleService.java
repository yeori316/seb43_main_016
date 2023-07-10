package com.codestates.edusync.model.study.schedule.service;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.service.MemberManager;
import com.codestates.edusync.model.study.schedule.entity.Schedule;
import com.codestates.edusync.model.study.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class ScheduleService {
    private final ScheduleRepository repository;
    private final MemberManager memberManager;

    /**
     * 스케쥴 등록
     * @param schedule
     */
    public void create(Schedule schedule, String email) {
        schedule.setMember(getMember(email));
        repository.save(schedule);
    }

    /**
     * 스케쥴 수정
     * @param scheduleId
     * @param schedule
     * @param email
     */
    public void update(Long scheduleId, String email, Schedule schedule) {

        Schedule findSchedule =
                repository.findById(scheduleId)
                        .orElseThrow(() -> new BusinessLogicException(ExceptionCode.TIME_SCHEDULE_NOT_FOUND));

        if (findSchedule.getMember().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.TIME_SCHEDULE_NOT_FOUND);
        }

        Optional.ofNullable(schedule.getTitle()).ifPresent(findSchedule::setTitle);
        Optional.ofNullable(schedule.getDescription()).ifPresent(findSchedule::setDescription);
        Optional.ofNullable(schedule.getStartDate()).ifPresent(findSchedule::setStartDate);
        Optional.ofNullable(schedule.getEndDate()).ifPresent(findSchedule::setEndDate);
        Optional.ofNullable(schedule.getStartTime()).ifPresent(findSchedule::setStartTime);
        Optional.ofNullable(schedule.getEndTime()).ifPresent(findSchedule::setEndTime);
        Optional.ofNullable(schedule.getColor()).ifPresent(findSchedule::setColor);

        repository.save(findSchedule);
    }

    /**
     * 스케쥴 상세 정보 조회
     * @param scheduleId
     * @param email
     * @return
     */
    @Transactional(readOnly = true)
    public Schedule get(Long scheduleId, String email) {

        Schedule findSchedule =
                repository.findById(scheduleId)
                        .orElseThrow(() -> new BusinessLogicException(ExceptionCode.TIME_SCHEDULE_NOT_FOUND));

        if (findSchedule.getMember().getEmail().equals(email)) {
            throw new BusinessLogicException(ExceptionCode.TIME_SCHEDULE_NOT_FOUND);
        }

        return findSchedule;
    }

    /**
     * 스케쥴 리스트 조회
     * @param email
     * @return
     */
    @Transactional(readOnly = true)
    public List<Schedule> getList(String email) {
        return repository.findAllByMemberId(getMember(email).getId());
    }


    /**
     * 스케쥴 삭제
     * @param scheduleId
     * @param email
     */
    @Transactional(readOnly = true)
    public void delete(Long scheduleId, String email) {

        Schedule findSchedule = get(scheduleId, email);

        repository.delete(findSchedule);
    }


    /**
     * 회원 조회
     * @param email
     * @return
     */
    public Member getMember(String email) {
        return memberManager.get(email);
    }
}
