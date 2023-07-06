package com.codestates.edusync.model.study.study.mapper;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.schedule.entity.Schedule;
import com.codestates.edusync.model.study.study.dto.StudyDto;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import com.codestates.edusync.model.study.tag.entity.Tag;
import com.codestates.edusync.model.study.tagref.entity.TagRef;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudyMapper {

    default Study studyPostToStudy(StudyDto.Post studyPostDto, Member member) {
        if (studyPostDto == null) return null;

        Study study = new Study();

        study.setStudyName(studyPostDto.getStudyName());
        study.setMemberMin(studyPostDto.getMemberMin());
        study.setMemberMax(studyPostDto.getMemberMax());
        study.setPlatform(studyPostDto.getPlatform());
        study.setIntroduction(studyPostDto.getIntroduction());
        study.setLeader(member);
        study.setStudyJoins(joinMapping(member, study));
        study.setSchedule(scheduleMapping(studyPostDto, member, study));
        study.setTagRefs(tagMapping(studyPostDto.getTags(), study));

        return study;
    }

    default Study studyPatchToStudy(StudyDto.Patch studyPatchDto, Long studyId) {
        if (studyPatchDto == null) return null;

        Study study = new Study();

        study.setId(studyId);
        study.setStudyName(studyPatchDto.getStudyName());
        study.setMemberMin(studyPatchDto.getMemberMin());
        study.setMemberMax(studyPatchDto.getMemberMax());
        study.setPlatform(studyPatchDto.getPlatform());
        study.setIntroduction(studyPatchDto.getIntroduction());

        study.setSchedule(scheduleMapping(studyPatchDto, study));
        study.setTagRefs(tagMapping(studyPatchDto.getTags(), study));

        return study;
    }

    default StudyDto.Response studyToResponse(Study study, int studyMemberCnt, boolean isLeader) {
        StudyDto.Response response = new StudyDto.Response();

        response.setStudyName(study.getStudyName());
        response.setImage(study.getImage());
        response.setMemberMin(study.getMemberMin());
        response.setMemberMax(study.getMemberMax());
        response.setMemberCnt(studyMemberCnt);
        response.setPlatform(study.getPlatform());
        response.setIntroduction(study.getIntroduction());
        response.setIsRecruited(study.getIsRecruited());
        response.setStartDate(study.getSchedule().getStartDate());
        response.setEndDate(study.getSchedule().getEndDate());
        response.setDayOfWeek(study.getSchedule().getDayOfWeek().stream().map(e -> e ? 1 : 0).collect(Collectors.toList()));
        response.setStartTime(study.getSchedule().getStartTime());
        response.setEndTime(study.getSchedule().getEndTime());
        response.setTags(study.getTagRefs().stream().map(e -> e.getTag().getValue()).collect(Collectors.toList()));
        response.setLeaderNickName(study.getLeader().getNickName());
        response.setIsLeader(isLeader);

        return response;
    }

    default List<StudyDto.Summary> studyListToResponseList(List<Study> studyPage) {
        return studyPage.stream().map(this::studyToSummary).collect(Collectors.toList());
    }

    default StudyDto.Summary studyToSummary(Study study) {
        StudyDto.Summary studySummary = new StudyDto.Summary();

        studySummary.setId(study.getId());
        studySummary.setImage(study.getImage());
        studySummary.setTitle(study.getStudyName());
        studySummary.setTagValues(tagRefsToTagList(study.getTagRefs()));

        return studySummary;
    }

    default List<String> tagRefsToTagList(List<TagRef> tagRefs) {
        return tagRefs.stream().map(TagRef::getTag).map(Tag::getValue).collect(Collectors.toList());
    }

    default List<StudyJoin> joinMapping(Member member, Study study) {
        StudyJoin join = new StudyJoin();

        join.setIsApproved(true);
        join.setMember(member);
        join.setStudy(study);

        return List.of(join);
    }

    default Schedule scheduleMapping(StudyDto.Post studyPostDto, Member member, Study study) {
        Schedule schedule = new Schedule();

        schedule.setTitle(studyPostDto.getStudyName());
        schedule.setDescription(studyPostDto.getIntroduction());
        schedule.setStartDate(studyPostDto.getStartDate());
        schedule.setEndDate(studyPostDto.getEndDate());
        schedule.setDayOfWeek(studyPostDto.getDayOfWeek().stream().map(e -> e != 0).collect(Collectors.toList()));
        schedule.setStartTime(studyPostDto.getStartTime());
        schedule.setEndTime(studyPostDto.getEndTime());
        schedule.setMember(member);
        schedule.setStudy(study);

        return schedule;
    }

    default Schedule scheduleMapping(StudyDto.Patch studyPatchDto, Study study) {
        Schedule schedule = new Schedule();

        schedule.setTitle(studyPatchDto.getStudyName());
        schedule.setDescription(studyPatchDto.getIntroduction());
        schedule.setStartDate(studyPatchDto.getStartDate());
        schedule.setEndDate(studyPatchDto.getEndDate());
        schedule.setDayOfWeek(studyPatchDto.getDayOfWeek().stream().map(e -> e != 0).collect(Collectors.toList()));
        schedule.setStartTime(studyPatchDto.getStartTime());
        schedule.setEndTime(studyPatchDto.getEndTime());
        schedule.setStudy(study);

        return schedule;
    }

    default List<TagRef> tagMapping(String tagValue, Study study) {
        if (tagValue == null) return null;

        Tag tag = new Tag();
        tag.setValue(tagValue);

        TagRef tagRef = new TagRef();
        tagRef.setTag(tag);
        tagRef.setStudy(study);

        return List.of(tagRef);
    }
}