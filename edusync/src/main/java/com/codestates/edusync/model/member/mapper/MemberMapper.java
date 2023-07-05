package com.codestates.edusync.model.member.mapper;

import com.codestates.edusync.model.member.dto.MemberDto;
import com.codestates.edusync.model.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    /**
     * 회원 가입 시
     * @param memberPostDto memberPostDto
     * @return Member
     */
    default Member memberPostToMember(MemberDto.Post memberPostDto) {
        if (memberPostDto == null) return null;

        Member member = new Member();

        member.setUuid(UUID.randomUUID().toString());
        member.setEmail(memberPostDto.getEmail());
        member.setPassword(memberPostDto.getPassword());
        member.setNickName(memberPostDto.getNickName());
        member.setImage("https://www.gravatar.com/avatar/HASH");
        member.setStatus(Member.Status.MEMBER_ACTIVE);
        member.setProvider(Member.Provider.LOCAL);

        return member;
    }

    /**
     * 닉네임 수정 시
     * @param memberPatchNickNameDto memberPatchNickNameDto
     * @return Member
     */
    default Member memberPatchNickNameToMember(MemberDto.PatchNickName memberPatchNickNameDto) {
        if (memberPatchNickNameDto == null) return null;

        Member member = new Member();
        member.setNickName(memberPatchNickNameDto.getNickName());

        return member;
    }

    /**
     * 패스워드 수정 시
     * @param memberPatchPasswordDto memberPatchPasswordDto
     * @return Member
     */
    default Member memberPatchPasswordToMember(MemberDto.PatchPassword memberPatchPasswordDto) {
        if (memberPatchPasswordDto == null) return null;

        Member member = new Member();
        member.setPassword(memberPatchPasswordDto.getPassword());

        return member;
    }

    /**
     * 자기소개 수정 시
     * @param memberPatchAboutMeDto memberPatchAboutMeDto
     * @return Member
     */
    default Member memberPatchAboutMeToMember(MemberDto.PatchAboutMe memberPatchAboutMeDto) {
        if (memberPatchAboutMeDto == null) return null;

        Member member = new Member();
        member.setAboutMe(memberPatchAboutMeDto.getAboutMe());

        return member;
    }

    /**
     * WithMe 수정 시
     * @param memberPatchWithMeDto memberPatchWithMeDto
     * @return Member
     */
    default Member memberPatchWithMeToMember(MemberDto.PatchWithMe memberPatchWithMeDto) {
        if (memberPatchWithMeDto == null) return null;

        Member member = new Member();
        member.setWithMe(memberPatchWithMeDto.getWithMe());

        return member;
    }

    /**
     * 회원 조회
     * @param member member
     * @return memberResponseDto
     */
    default MemberDto.Response memberToMemberResponse(Member member) {
        if (member == null) return null;

        MemberDto.Response memberResponseDto = new MemberDto.Response();

        memberResponseDto.setUuid(member.getUuid());
        memberResponseDto.setEmail(member.getEmail());
        memberResponseDto.setNickName(member.getNickName());
        memberResponseDto.setImage(member.getImage());
        memberResponseDto.setAboutMe(member.getAboutMe());
        memberResponseDto.setWithMe(member.getWithMe());

        if (member.getRoles() != null) {
            memberResponseDto.setRoles(member.getRoles());
        }

        return memberResponseDto;
    }
}
