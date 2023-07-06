package com.codestates.edusync.model.study.comment.mapper;

import com.codestates.edusync.model.study.comment.dto.CommentPostDto;
import com.codestates.edusync.model.study.comment.dto.CommentResponseDto;
import com.codestates.edusync.model.study.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment studygroupPostCommentPostDtoToStudygroupPostComment(CommentPostDto.Post postDto);
    Comment studygroupPostCommentPatchDtoToStudygroupPostComment(CommentPostDto.Patch patchDto);

    default List<CommentResponseDto> studygroupPostCommentToStudygroupPostCommentResponseDtos(List<Comment> comments,
                                                                                              String emailOfLoginMember) {
        List<CommentResponseDto> result = new ArrayList<>();
        comments.forEach(
                comment -> {
                    CommentResponseDto resultResponse =
                            studygroupPostCommentToStudygroupPostCommentResponseDto(comment);
                    resultResponse.setIsMyComment(comment.getMember().getEmail().equals(emailOfLoginMember));
                    result.add(resultResponse);
                }
        );

        return result;
    };

//    @Mapping(source = "id", target = "commentId")
//    @Mapping(source = "study.id", target = "studyId")
//    @Mapping(source = "member.nickName", target = "nickName")
    CommentResponseDto studygroupPostCommentToStudygroupPostCommentResponseDto(Comment comment);
}
