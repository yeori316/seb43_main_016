package com.codestates.edusync.model.study.studyjoin.mapper;

import com.codestates.edusync.model.study.study.mapper.StudyMapper;
import com.codestates.edusync.model.study.studyjoin.dto.StudyJoinDto;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface StudyJoinMapper extends StudyMapper {

    /**
     * 스터디 멤버 리스트 & 가입 대기 리스트 매퍼
     * @param studyJoinList
     * @return
     */
    default StudyJoinDto.Response studygroupJoinToStudygroupJoinDtos(List<StudyJoin> studyJoinList) {
        StudyJoinDto.Response studygroupJoinDto = new StudyJoinDto.Response();
        List<String> nickName =
                studyJoinList.stream()
                      .map(e -> e.getMember().getNickName())
                      .collect(Collectors.toList());
        studygroupJoinDto.setNickName(nickName);
        return studygroupJoinDto;
    }
}