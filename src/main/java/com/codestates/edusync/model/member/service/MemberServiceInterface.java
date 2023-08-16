package com.codestates.edusync.model.member.service;

import com.codestates.edusync.model.member.dto.MemberDto;
import com.codestates.edusync.model.member.entity.Member;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface MemberServiceInterface {

       /**
        * 회원 가입
        * @param member Member
        */
       void create(Member member);

       /**
        * 닉네임 수정
        * @param member Member
        * @param email Email
        */
       @Transactional(isolation = Isolation.SERIALIZABLE)
       void updateNickName(Member member, String email);

       /**
        * 패스워드 수정
        * @param member Member
        * @param email Email
        */
       void updatePassword(Member member, String email);

       /**
        * 자기소개 수정
        * @param member Member
        * @param email Email
        */
       void updateAboutMe(Member member, String email);

       /**
        * 이미지 수정
        * @param email Email
        * @param image Image
        */
       void updateImage(MultipartFile image, String email);

       /**
        * 회원 조회 - 이메일
        * @param email email
        * @return Member
        */
       @Transactional(readOnly = true)
       Member get(String email);

       /**
        * 회원 조회 - 닉네임
        * @param nickName nickName
        * @return Member
        */
       @Transactional(readOnly = true)
       Member getNickName(String nickName);

       /**
        * 회원 조회 - 닉네임
        * @param nickName nickName
        * @return Member
        */
       @Transactional(readOnly = true)
       Member getNickName(String nickName, String email);

       /**
        * 스터디 가입 신청자 | 스터디 멤버 리스트 조회
        * @param studyId Study id
        * @param email email
        * @return Member NickName List
        */
       @Transactional(readOnly = true)
       MemberDto.MembersResponse getStudyMembers(Long studyId, String email, Boolean isMember);

       /**
        * 패스워드 인증
        * @param password String
        * @param email String
        * @return boolean
        */
       @Transactional(readOnly = true)
       boolean checkPassword(String email, String password);

       /**
        * 회원 가입 구분
        * @param email String
        * @return Map<String, String>
        */
       @Transactional(readOnly = true)
       Map<String, String> getProvider(String email);

       /**
        * 휴먼 회원 해제
        * @param email String
        * @param password String
        */
       void updateStatus(String email, String password);

       /**
        * 회원 탈퇴(소프트 삭제)
        * @param email String
        */
       void delete(String email);

       /**
        * 닉네임 체크
        * @param member Member
        */
       @Transactional(readOnly = true)
       void nickNameCheck(Member member);
}
