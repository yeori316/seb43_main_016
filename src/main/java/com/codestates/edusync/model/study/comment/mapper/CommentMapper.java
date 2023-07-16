package com.codestates.edusync.model.study.comment.mapper;

import com.codestates.edusync.model.study.comment.dto.CommentDto;
import com.codestates.edusync.model.study.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    /**
     * 댓글 등록
     * @param commentPostDto
     * @return
     */
    default Comment commentPostToComment(CommentDto.Post commentPostDto) {
        if ( commentPostDto == null ) return null;

        Comment comment = new Comment();
        comment.setContent(commentPostDto.getContent());

        return comment;
    }

    /**
     * 댓글 수정
     * @param commentPatchDto
     * @return
     */
    default Comment commentPatchToComment(CommentDto.Patch commentPatchDto) {
        if ( commentPatchDto == null ) return null;

        Comment comment = new Comment();
        comment.setId(commentPatchDto.getId());
        comment.setContent(commentPatchDto.getContent());

        return comment;
    }
}
