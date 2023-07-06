package com.codestates.edusync.model.study.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentResponseDto {
    private Long commentId;
    private Long studyId;
    private String nickName;
    private String content;
    private Boolean isMyComment;
}
