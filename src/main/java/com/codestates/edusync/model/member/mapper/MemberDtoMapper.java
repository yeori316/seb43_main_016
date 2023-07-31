package com.codestates.edusync.model.member.mapper;

import com.codestates.edusync.model.member.dto.MemberDto;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberDtoMapper {

    /**
     * 스터디 멤버 리스트 & 가입 대기 리스트 매퍼
     * @param studyJoinList MemberList
     * @return nickNameList
     */
    default MemberDto.MembersResponse studyJoinListToStudyMembersDto(List<StudyJoin> studyJoinList) {

        MemberDto.MembersResponse studyMembersDto = new MemberDto.MembersResponse();

        List<String> nickNameList = studyJoinList.stream()
                .map(e -> e.getMember().getNickName())
                .collect(Collectors.toList());

        studyMembersDto.setNickName(nickNameList);

        return studyMembersDto;
    }
}
