package com.codestates.edusync.model.study.comment.mapper;

import com.codestates.edusync.model.study.comment.dto.CommentDto;
import com.codestates.edusync.model.study.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentDtoMapper {

    default List<CommentDto.Response> commentsToResponesList(List<Comment> comments, String email) {
        return comments.stream().map(e -> commentToResponse(e, email)).collect(Collectors.toList());
    }

    default CommentDto.Response commentToResponse(Comment comment, String email) {
        CommentDto.Response response = new CommentDto.Response();

        response.setId(comment.getId());
        response.setNickName(comment.getMember().getNickName());
        response.setContent(comment.getContent());
        response.setIsMyComment(comment.getMember().getEmail().equals(email));

        return response;
    }
}
