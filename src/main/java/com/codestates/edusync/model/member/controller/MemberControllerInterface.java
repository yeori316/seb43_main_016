package com.codestates.edusync.model.member.controller;

import com.codestates.edusync.model.member.dto.MemberDto;
import com.codestates.edusync.security.auth.dto.LoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

public interface MemberControllerInterface {
        /**
        * 회원 가입
        * @param postDto MemberPostDto
        * @return String
        */
       @PostMapping
       ResponseEntity<String> post(@Valid @RequestBody MemberDto.Post postDto);

       /**
        * 닉네임 수정
        * @param authentication Authentication
        * @param patchNickNameDto MemberPatchNickNameDto
        * @return String
        */
       @PatchMapping("/name")
       ResponseEntity<String> patchNickName(Authentication authentication,
                                            @Valid @RequestBody MemberDto.PatchNickName patchNickNameDto);

       /**
        * 패스워드 수정
        * @param authentication Authentication
        * @param patchPassDto MemberPatchPassDto
        * @return String
        */
       @PatchMapping("/pass")
       ResponseEntity<String> patchPass(Authentication authentication,
                                        @Valid @RequestBody MemberDto.PatchPassword patchPassDto);

       /**
        * 자기소개 수정
        * @param authentication Authentication
        * @param patchAboutMeDto MemberPatchAboutMeDto
        * @return String
        */
       @PatchMapping("/aboutme")
       ResponseEntity<String> patchAboutMe(Authentication authentication,
                                           @Valid @RequestBody MemberDto.PatchAboutMe patchAboutMeDto);

       /**
        * 이미지 수정
        * @param authentication Authentication
        * @param image Image
        * @return String
        */
       @PatchMapping("/image")
       ResponseEntity<String> patchImage(Authentication authentication,
                                         @RequestPart(value="image") MultipartFile image);

       /**
        * 마이페이지 조회
        * @param authentication Authentication
        * @return MemberDtoMyInfo
        */
       @GetMapping
       ResponseEntity<MemberDto.MyInfo> getMyInfo(Authentication authentication);

       /**
        * 멤버 조회
        * @param authentication Authentication
        * @param enNickName Encoded NickName
        * @return MemberDtoResponse
        */
       @GetMapping("/{enNickName}")
       ResponseEntity<MemberDto.MemberResponse> get(Authentication authentication,
                                                    @PathVariable String enNickName);

       /**
        * 스터디 가입 신청자 | 스터디 멤버 리스트 조회
        * @param authentication Authentication
        * @param enStudyId Encoded StudyId
        * @param enIsMember Encoded IsMember
        * @return MemberDtoResponse
        */
       @GetMapping("/list")
       ResponseEntity<MemberDto.MembersResponse> getWaitStudyMembers(Authentication authentication,
                                                                     @RequestParam("s") String enStudyId,
                                                                     @RequestParam("m") String enIsMember);

       /**
        * 패스워드 인증
        * @param authentication Authentication
        * @param checkPassDto MemberPatchPassDto
        * @return String
        */
       @PostMapping("/pass-auth")
       ResponseEntity<String> checkPassword(Authentication authentication,
                                            @RequestBody MemberDto.PatchPassword checkPassDto);

       /**
        * 회원 가입 구분
        * @param authentication Authentication
        * @return String
        */
       @GetMapping("/provider")
       ResponseEntity<Map<String, String>> getProvider(Authentication authentication);

       /**
        * 휴먼 회원 해제
        * @param loginDto LoginDto
        * @return String
        */
       @PatchMapping("/reactive")
       ResponseEntity<String> patchStatus(@RequestBody LoginDto loginDto);

       /**
        * 회원 탈퇴(소프트 삭제)
        * @param authentication Authentication
        * @return String
        */
       @DeleteMapping
       ResponseEntity<String> delete(Authentication authentication);
}
