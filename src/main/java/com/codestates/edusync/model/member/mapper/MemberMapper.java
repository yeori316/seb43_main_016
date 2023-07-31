package com.codestates.edusync.model.member.mapper;

import com.codestates.edusync.model.member.dto.MemberDto;
import com.codestates.edusync.model.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

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
        member.setStatus(Member.Status.ACTIVE);
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
     * 본인 정보 조회
     * @param member member
     * @return memberResponseDto
     */
    default MemberDto.MyInfo memberToMemberInfoResponse(Member member) {
        if (member == null) return null;

        MemberDto.MyInfo myInfoDto = new MemberDto.MyInfo();

        myInfoDto.setEmail(member.getEmail());
        myInfoDto.setNickName(member.getNickName());
        myInfoDto.setImage(member.getImage());
        myInfoDto.setAboutMe(member.getAboutMe());

        if (member.getRoles() != null) {
            myInfoDto.setRoles(member.getRoles());
        }

        return myInfoDto;
    }

    default MemberDto.MemberResponse memberToMemberResponse(Member member) {
        if (member == null) return null;

        MemberDto.MemberResponse memberResponseDto = new MemberDto.MemberResponse();

        memberResponseDto.setNickName(member.getNickName());
        memberResponseDto.setImage(member.getImage());
        memberResponseDto.setAboutMe(member.getAboutMe());

        return memberResponseDto;
    }
}
