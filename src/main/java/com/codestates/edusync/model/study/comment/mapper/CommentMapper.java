package com.codestates.edusync.model.study.comment.mapper;

import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.study.comment.dto.CommentDto;
import com.codestates.edusync.model.study.comment.entity.Comment;
import com.codestates.edusync.model.study.study.entity.Study;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    /**
     * 댓글 등록
     * @param commentPostDto CommentPostDto
     * @return Comment
     */
    default Comment commentPostToComment(CommentDto.Post commentPostDto, Member member, Study study) {
        if ( commentPostDto == null ) return null;

        Comment comment = new Comment();
        comment.setContent(commentPostDto.getContent());
        comment.setMember(member);
        comment.setStudy(study);

        return comment;
    }

    /**
     * 댓글 수정
     * @param commentPatchDto CommentPatchDto
     * @return Comment
     */
    default Comment commentPatchToComment(CommentDto.Patch commentPatchDto) {
        if ( commentPatchDto == null ) return null;

        Comment comment = new Comment();
        comment.setId(commentPatchDto.getId());
        comment.setContent(commentPatchDto.getContent());

        return comment;
    }
}
