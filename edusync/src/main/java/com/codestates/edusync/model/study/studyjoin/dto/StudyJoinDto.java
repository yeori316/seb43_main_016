package com.codestates.edusync.model.study.studyjoin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

public class StudyJoinDto {
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Dto {
        @NotNull
        private String nickName;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private List<String> nickName;
    }
}
