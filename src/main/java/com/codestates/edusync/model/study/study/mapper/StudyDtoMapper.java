package com.codestates.edusync.model.study.study.mapper;

import com.codestates.edusync.model.schedule.entity.StudyDayOfWeek;
import com.codestates.edusync.model.study.study.dto.StudyDto;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.tag.entity.Tag;
import com.codestates.edusync.model.study.tag.entity.TagRef;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudyDtoMapper {

    /**
     * 스터디 조회 시
     * @param study
     * @param studyMemberCnt
     * @param isLeader
     * @return
     */
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
        response.setStartDate(study.getStudySchedule().getStartDate());
        response.setEndDate(study.getStudySchedule().getEndDate());
        response.setDayOfWeek(this.responseDayOfWeek(study.getStudySchedule().getStudyDayOfWeek()));
        response.setStartTime(study.getStudySchedule().getStartTime());
        response.setEndTime(study.getStudySchedule().getEndTime());
        response.setTags(study.getTagRefs().stream().map(e -> e.getTag().getTagValue()).collect(Collectors.toList()));
        response.setLeaderNickName(study.getLeader().getNickName());
        response.setIsLeader(isLeader);

        return response;
    }

    /**
     * 스터디 조회 시
     * @param studyDayOfWeek
     * @return
     */
    default List<Integer> responseDayOfWeek(StudyDayOfWeek studyDayOfWeek) {
        List<Integer> response = new ArrayList<>();

        response.add(Boolean.TRUE.equals(studyDayOfWeek.getSunday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getMonday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getTuesday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getWednesday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getThursday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getFriday()) ? 1 : 0);
        response.add(Boolean.TRUE.equals(studyDayOfWeek.getSaturday()) ? 1 : 0);

        return response;
    }

    /**
     * 스터디 리스트 조회
     * @param studyPage
     * @return
     */
    default List<StudyDto.Summary> studyListToResponseList(List<Study> studyList) {
        return studyList.stream().map(this::studyToSummary).collect(Collectors.toList());
    }

    /**
     * 스터디 리스트 조회
     * @param study
     * @return
     */
    default StudyDto.Summary studyToSummary(Study study) {
        StudyDto.Summary studySummary = new StudyDto.Summary();

        studySummary.setId(study.getId());
        studySummary.setImage(study.getImage());
        studySummary.setTitle(study.getStudyName());
        studySummary.setTags(this.tagRefsToTagList(study.getTagRefs()));

        return studySummary;
    }

    /**
     * 스터디 리스트 조회
     * @param tagRefs
     * @return
     */
    default List<String> tagRefsToTagList(List<TagRef> tagRefs) {
        return tagRefs.stream().map(TagRef::getTag).map(Tag::getTagValue).collect(Collectors.toList());
    }
}
