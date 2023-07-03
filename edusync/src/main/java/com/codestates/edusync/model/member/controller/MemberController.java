package com.codestates.edusync.model.member.controller;

import com.codestates.edusync.model.common.utils.UriCreator;
import com.codestates.edusync.model.member.dto.MemberDto;
import com.codestates.edusync.model.member.mapper.MemberMapper;
import com.codestates.edusync.model.member.service.MemberService;
import com.codestates.edusync.security.auth.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
@Validated
public class MemberController {
    private final MemberMapper mapper;
    private final MemberService service;
    private final URI loginUri = UriCreator.createUri("members", "login");

    /**
     * 회원 가입
     * @param memberPostDto memberPostDto
     * @return String
     */
    @PostMapping
    public ResponseEntity<String> post(@Valid @RequestBody MemberDto.Post memberPostDto) {
        service.create(mapper.memberPostToMember(memberPostDto));
        return ResponseEntity.created(loginUri).build();
    }

    /**
     * 닉네임 수정
     * @param authentication 토큰
     * @param memberPatchNickNameDto memberPatchNickNameDto
     * @return URI
     */
    @PatchMapping("/nickName")
    public ResponseEntity<String> patchNickName(Authentication authentication,
                                             @Valid @RequestBody MemberDto.PatchNickName memberPatchNickNameDto) {
        service.updateNickName(mapper.memberPatchNickNameToMember(memberPatchNickNameDto), authentication.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 패스워드 수정
     * @param authentication 토큰
     * @param memberPatchPassDto memberPatchPassDto
     * @return URI
     */
    @PatchMapping("/pass") // uri 변경 필요
    public ResponseEntity<String> patchPass(Authentication authentication,
                                         @Valid @RequestBody MemberDto.PatchPassword memberPatchPassDto) {
        service.updatePassword(mapper.memberPatchPasswordToMember(memberPatchPassDto), authentication.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 자기소개 수정
     * @param authentication 토큰
     * @param memberPatchAboutMeDto memberPatchAboutMeDto
     * @return URI
     */
    @PatchMapping("/aboutme")
    public ResponseEntity<String> patchAboutMe(Authentication authentication,
                                            @Valid @RequestBody MemberDto.PatchAboutMe memberPatchAboutMeDto) {
        service.updateAboutMe(mapper.memberPatchAboutMeToMember(memberPatchAboutMeDto), authentication.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * WithMe 수정
     * @param authentication 토큰
     * @param memberPatchWithMeDto memberPatchWithMeDto
     * @return URI
     */
    @PatchMapping("/withme")
    public ResponseEntity<String> patchWithMe(Authentication authentication,
                                           @Valid @RequestBody MemberDto.PatchWithMe memberPatchWithMeDto) {
        service.updateWithMe(mapper.memberPatchWithMeToMember(memberPatchWithMeDto), authentication.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 이미지 수정
     * @param authentication 토큰
     * @param image 이미지
     * @return URI
     */
    @PatchMapping("/image")
    public ResponseEntity<String> patchImage(Authentication authentication,
                                          @RequestPart(value="image") MultipartFile image) {
        service.updateImage(authentication.getName(), image);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 회원 조회
     * @param authentication 토큰
     * @return Response
     */
    @GetMapping
    public ResponseEntity<MemberDto.Response> get(Authentication authentication) {

        MemberDto.Response responseDto =
                mapper.memberToMemberResponse(service.find(authentication.getName()));

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 회원 탈퇴(소프트 삭제)
     * @param authentication 토큰
     * @return URI
     */
    @DeleteMapping
    public ResponseEntity<URI> delete(Authentication authentication) {
        service.delete(authentication.getName());
        return new ResponseEntity<>( HttpStatus.OK);
    }

    /**
     * 휴먼 회원 인증
     * @param loginDto loginDto
     * @return URI
     */
    @PatchMapping("/reactive")
    public ResponseEntity<URI> patchStatus(@RequestBody LoginDto loginDto){
        service.updateStatus(loginDto.getEmail(),loginDto.getPassword());
        return new ResponseEntity<>(loginUri, HttpStatus.OK);
    }

    /**
     * 패스워드 인증
     * @param authentication 토큰
     * @param checkPassword requestBody
     * @return response
     */
    @PostMapping("/pass-auth")
    public ResponseEntity<Object> checkPassword(Authentication authentication,
                                                @RequestBody MemberDto.PatchPassword checkPassword) {
        return new ResponseEntity<>(
                service.checkPassword(checkPassword.getPassword(), authentication.getName()) ?
                        HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }

    /**
     * 회원 가입 구분
     * @param authentication 토큰
     * @return response
     */
    @GetMapping("/provider")
    public ResponseEntity<Map<String, String>> getProvider(Authentication authentication){
        return ResponseEntity.ok(service.getProvider(authentication.getName()));
    }
}
