package com.codestates.edusync.model.study.study.mapper;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.schedule.entity.ScheduleRef;
import com.codestates.edusync.model.schedule.entity.StudyDayOfWeek;
import com.codestates.edusync.model.schedule.entity.StudySchedule;
import com.codestates.edusync.model.study.study.dto.StudyDto;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import com.codestates.edusync.model.study.tag.entity.Tag;
import com.codestates.edusync.model.study.tag.entity.TagRef;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudyMapper {

    /**
     * 스터디 등록
     * @param studyPostDto
     * @param member
     * @param tagList
     * @return
     */
    default Study studyPostToStudy(StudyDto.Post studyPostDto, Member member, List<Tag> tagList) {
        if (studyPostDto == null) return null;

        Study study = new Study();

        study.setStudyName(studyPostDto.getStudyName());
        study.setImage("https://i.ibb.co/K7zMNKW/icons8-team-y-Tw-Xp-LO5-HAA-unsplash.jpg");
        study.setMemberMin(studyPostDto.getMemberMin());
        study.setMemberMax(studyPostDto.getMemberMax());
        study.setPlatform(studyPostDto.getPlatform());
        study.setIntroduction(studyPostDto.getIntroduction());
        study.setIsRecruited(true);
        study.setLeader(member);
        study.setStudyJoins(this.joinMapping(member, study));
        study.setStudySchedule(this.studyScheduleMapping(studyPostDto, member));
        study.setTagRefs(this.tagMapping(study, tagList));
        study.setViews(0L);

        return study;
    }

    /**
     * 스터디 수정
     * @param studyPatchDto
     * @param studyId
     * @param tagList
     * @return
     */
    default Study studyPatchToStudy(StudyDto.Patch studyPatchDto, Long studyId, List<Tag> tagList) {
        if (studyPatchDto == null) return null;

        Study study = new Study();

        study.setId(studyId);
        study.setStudyName(studyPatchDto.getStudyName());
        study.setMemberMin(studyPatchDto.getMemberMin());
        study.setMemberMax(studyPatchDto.getMemberMax());
        study.setPlatform(studyPatchDto.getPlatform());
        study.setIntroduction(studyPatchDto.getIntroduction());
        study.setStudySchedule(this.studySchedulePatchMapping(studyPatchDto));
        study.setTagRefs(this.tagMapping(study, tagList));

        return study;
    }

    /**
     * 스터디 등록 시
     * @param member
     * @param study
     * @return
     */
    default List<StudyJoin> joinMapping(Member member, Study study) {
        StudyJoin join = new StudyJoin();

        join.setIsApproved(true);
        join.setMember(member);
        join.setStudy(study);

        return List.of(join);
    }

    /**
     * 스터디 등록 시
     * @param studyPostDto
     * @param member
     * @return
     */
    default StudySchedule studyScheduleMapping(StudyDto.Post studyPostDto, Member member) {
        StudySchedule schedule = new StudySchedule();

        schedule.setTitle(studyPostDto.getStudyName());
        schedule.setDescription(studyPostDto.getIntroduction());
        schedule.setStartDate(studyPostDto.getStartDate());
        schedule.setEndDate(studyPostDto.getEndDate());
        schedule.setStartTime(studyPostDto.getStartTime());
        schedule.setEndTime(studyPostDto.getEndTime());
        schedule.setColor(studyPostDto.getColor());
        schedule.setStudyDayOfWeek(this.dayOfWeekMapping(studyPostDto.getDayOfWeek()));
        schedule.setScheduleRefs(this.scheduleRefMapping(member, schedule));

        return schedule;
    }

    /** 스터디 수정 시
     *
     * @param studyPatchDto
     * @return
     */
    default StudySchedule studySchedulePatchMapping(StudyDto.Patch studyPatchDto) {
        StudySchedule schedule = new StudySchedule();

        schedule.setTitle(studyPatchDto.getStudyName());
        schedule.setDescription(studyPatchDto.getIntroduction());
        schedule.setStartDate(studyPatchDto.getStartDate());
        schedule.setEndDate(studyPatchDto.getEndDate());
        schedule.setStartTime(studyPatchDto.getStartTime());
        schedule.setEndTime(studyPatchDto.getEndTime());
        schedule.setColor(studyPatchDto.getColor());
        schedule.setStudyDayOfWeek(this.dayOfWeekMapping(studyPatchDto.getDayOfWeek()));

        return schedule;
    }

    /**
     * 스터디 등록 시
     * @param member
     * @param studySchedule
     * @return
     */
    default List<ScheduleRef> scheduleRefMapping(Member member, StudySchedule studySchedule) {
        ScheduleRef scheduleRef = new ScheduleRef();

        scheduleRef.setMember(member);
        scheduleRef.setStudySchedule(studySchedule);

        return List.of(scheduleRef);
    }

    /**
     * 스터디 등록 시
     * @param week
     * @return
     */
    default StudyDayOfWeek dayOfWeekMapping(List<Integer> week) {
        StudyDayOfWeek dayOfWeek = new StudyDayOfWeek();

        dayOfWeek.setSunday(week.get(0) == 1);
        dayOfWeek.setMonday(week.get(1) == 1);
        dayOfWeek.setTuesday(week.get(2) == 1);
        dayOfWeek.setWednesday(week.get(3) == 1);
        dayOfWeek.setThursday(week.get(4) == 1);
        dayOfWeek.setFriday(week.get(5) == 1);
        dayOfWeek.setSaturday(week.get(6) == 1);

        return dayOfWeek;
    }

    /**
     * 스터디 등록 시
     * @param tagList
     * @param study
     * @return
     */
    default List<TagRef> tagMapping(Study study, List<Tag> tagList) {
        return tagList.stream().map(e -> {
            TagRef tagRef = new TagRef();
            tagRef.setStudy(study);
            tagRef.setTag(e);
            return tagRef;
        }).collect(Collectors.toList());
    }

    /**
     * 스터디 상태 수정 시
     * @param recruited
     * @return
     */
    default StudyDto.ResponseStatus studyStatus(Boolean recruited) {
        StudyDto.ResponseStatus status = new StudyDto.ResponseStatus();
        status.setRecruited(recruited);
        return status;
    }
}