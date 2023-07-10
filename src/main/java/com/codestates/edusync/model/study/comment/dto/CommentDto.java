package com.codestates.edusync.model.study.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class CommentDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Post {
        @NotNull
        private String content;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Patch {
        private Long id;
        private String content;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private Long id;
        private String nickName;
        private String content;
        private Boolean isMyComment;
    }
}
