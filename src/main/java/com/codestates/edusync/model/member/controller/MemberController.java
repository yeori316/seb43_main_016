package com.codestates.edusync.model.member.controller;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.common.util.ObfuscationUtil;
import com.codestates.edusync.model.member.dto.MemberDto;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.mapper.MemberMapper;
import com.codestates.edusync.model.member.service.MemberServiceInterface;
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
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
@Validated
public class MemberController implements MemberControllerInterface {
    private final MemberMapper mapper;
    private final MemberServiceInterface service;
    private final ObfuscationUtil obfuscationUtil;

    @PostMapping
    public ResponseEntity<String> post(@Valid @RequestBody MemberDto.Post postDto) {

        Member member = mapper.memberPostToMember(postDto);
        service.create(member);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/name")
    public ResponseEntity<String> patchNickName(Authentication authentication,
                                                @Valid @RequestBody MemberDto.PatchNickName patchNickNameDto) {
        Member member = mapper.memberPatchNickNameToMember(patchNickNameDto);
        String email = authentication.getName();
        service.updateNickName(member, email);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/pass")
    public ResponseEntity<String> patchPass(Authentication authentication,
                                            @Valid @RequestBody MemberDto.PatchPassword patchPassDto) {
        Member member = mapper.memberPatchPasswordToMember(patchPassDto);
        String email = authentication.getName();
        service.updatePassword(member, email);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/aboutme")
    public ResponseEntity<String> patchAboutMe(Authentication authentication,
                                               @Valid @RequestBody MemberDto.PatchAboutMe patchAboutMeDto) {
        Member member = mapper.memberPatchAboutMeToMember(patchAboutMeDto);
        String email = authentication.getName();
        service.updateAboutMe(member, email);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/image")
    public ResponseEntity<String> patchImage(Authentication authentication,
                                             @RequestPart(value="image") MultipartFile image) {
        String email = authentication.getName();
        service.updateImage(image, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MemberDto.MyInfo> getMyInfo(Authentication authentication) {

        String email = authentication.getName();
        Member member = service.get(email);
        MemberDto.MyInfo myInfoDto = mapper.memberToMemberInfoResponse(member);
        return new ResponseEntity<>(myInfoDto, HttpStatus.OK);
    }

    @GetMapping("/{enNickName}")
    public ResponseEntity<MemberDto.MemberResponse> get(Authentication authentication,
                                                        @PathVariable String enNickName) {
        String nickName = getDecoded(enNickName);
        String email = authentication.getName();
        Member member = service.getNickName(nickName, email);
        MemberDto.MemberResponse response = mapper.memberToMemberResponse(member);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<MemberDto.MembersResponse> getWaitStudyMembers(Authentication authentication,
                                                                         @RequestParam("s") String enStudyId,
                                                                         @RequestParam("m") String enIsMember) {
        long studyId = Long.parseLong(getDecoded(enStudyId));

        if (studyId < 1) {
            throw new BusinessLogicException(ExceptionCode.STUDY_NOT_FOUND);
        }

        String email = authentication.getName();
        Boolean isMember = getDecoded(enIsMember).equals("true");
        MemberDto.MembersResponse response = service.getStudyMembers(studyId, email, isMember);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pass-auth")
    public ResponseEntity<String> checkPassword(Authentication authentication,
                                                @RequestBody MemberDto.PatchPassword checkPassDto) {
        String email = authentication.getName();
        String passwd = checkPassDto.getPassword();
        boolean passCheck = service.checkPassword(email, passwd);
        return new ResponseEntity<>(passCheck ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/provider")
    public ResponseEntity<Map<String, String>> getProvider(Authentication authentication){

        String email = authentication.getName();
        Map<String, String> response = service.getProvider(email);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/reactive")
    public ResponseEntity<String> patchStatus(@RequestBody LoginDto loginDto){

        String email = loginDto.getEmail();
        String passwd = loginDto.getPassword();
        service.updateStatus(email, passwd);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(Authentication authentication) {

        String email = authentication.getName();
        service.delete(email);
        return ResponseEntity.ok().build();
    }

    /**
     * Base64 DecodeUtil
     * @param message Encoded Message
     * @return String
     */
    public String getDecoded(String message) {
        return obfuscationUtil.getDecoded(message);
    }
}