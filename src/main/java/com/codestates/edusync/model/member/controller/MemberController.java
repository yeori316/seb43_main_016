package com.codestates.edusync.model.member.controller;

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
import javax.validation.constraints.Positive;
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

    /**
     * 회원 가입
     * @param postDto MemberPostDto
     * @return String
     */
    @PostMapping
    public ResponseEntity<String> post(@Valid @RequestBody MemberDto.Post postDto) {
        service.create(mapper.memberPostToMember(postDto));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 닉네임 수정
     * @param authentication Authentication
     * @param patchNickNameDto MemberPatchNickNameDto
     * @return String
     */
    @PatchMapping("/name")
    public ResponseEntity<String> patchNickName(Authentication authentication,
                                                @Valid @RequestBody MemberDto.PatchNickName patchNickNameDto) {
        service.updateNickName(mapper.memberPatchNickNameToMember(patchNickNameDto), authentication.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * 패스워드 수정
     * @param authentication Authentication
     * @param patchPassDto MemberPatchPassDto
     * @return String
     */
    @PatchMapping("/pass")
    public ResponseEntity<String> patchPass(Authentication authentication,
                                            @Valid @RequestBody MemberDto.PatchPassword patchPassDto) {
        service.updatePassword(mapper.memberPatchPasswordToMember(patchPassDto), authentication.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * 자기소개 수정
     * @param authentication 토큰
     * @param patchAboutMeDto memberPatchAboutMeDto
     * @return URI
     */
    @PatchMapping("/aboutme")
    public ResponseEntity<String> patchAboutMe(Authentication authentication,
                                               @Valid @RequestBody MemberDto.PatchAboutMe patchAboutMeDto) {
        service.updateAboutMe(mapper.memberPatchAboutMeToMember(patchAboutMeDto), authentication.getName());
        return ResponseEntity.ok().build();
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
        service.updateImage(image, authentication.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * 본인 정보 조회
     * @param authentication 토큰
     * @return Response
     */
    @GetMapping
    public ResponseEntity<MemberDto.MyInfo> getMyInfo(Authentication authentication) {

        MemberDto.MyInfo myInfoDto =
                mapper.memberToMemberInfoResponse(service.get(authentication.getName()));

        return new ResponseEntity<>(myInfoDto, HttpStatus.OK);
    }

    /**
     * 멤버 조회
     * @param nickName
     * @return
     */
    @GetMapping("/{nickName}")
    public ResponseEntity<MemberDto.MemberResponse> get(Authentication authentication,
                                                        @PathVariable String nickName) {

//        String decode = URLDecoder.decode(studyId, "UTF-8");
//        Base64.Decoder decoder = Base64.getDecoder();
//        byte[] decodedBytes = decoder.decode(decode.getBytes());
//        long id = Long.valueOf(new String(decodedBytes));

        service.get(authentication.getName());

        MemberDto.MemberResponse memberResponseDto =
                mapper.memberToMemberResponse(service.getNickName(nickName));

        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }


    /**
     * 스터디 가입 신청자 | 스터디 멤버 리스트 조회
     * @param studyId
     * @param authentication
     * @return
     */
    @GetMapping("/list") // URI 수정
    public ResponseEntity<MemberDto.MembersResponse> getWaitStudyMembers(Authentication authentication,
                                                                         @RequestParam("s") @Positive Long studyId,
                                                                         @RequestParam("m") Boolean isMember) {

        return ResponseEntity.ok(service.getStudyMembers(studyId, authentication.getName(), isMember));
    }


    /**
     * 패스워드 인증
     * @param authentication 토큰
     * @param checkPassword requestBody
     * @return response
     */
    @PostMapping("/pass-auth")
    public ResponseEntity<Object> checkPassword(Authentication authentication,
                                                @RequestBody MemberDto.PatchPassword checkPassDto) {
        return new ResponseEntity<>(
                service.checkPassword(authentication.getName(), checkPassDto.getPassword()) ?
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

    /**
     * 휴먼 회원 해제
     * @param loginDto loginDto
     * @return URI
     */
    @PatchMapping("/reactive")
    public ResponseEntity<URI> patchStatus(@RequestBody LoginDto loginDto){
        service.updateStatus(loginDto.getEmail(),loginDto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 회원 탈퇴(소프트 삭제)
     * @param authentication 토큰
     * @return URI
     */
    @DeleteMapping
    public ResponseEntity<URI> delete(Authentication authentication) {
        service.delete(authentication.getName());
        return ResponseEntity.ok().build();
    }
}