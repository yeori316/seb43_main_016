package com.codestates.edusync.model.member.service;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.common.service.AwsS3Service;
import com.codestates.edusync.model.member.dto.MemberDto;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.mapper.MemberDtoMapper;
import com.codestates.edusync.model.member.repository.MemberRepository;
import com.codestates.edusync.model.member.utils.NickNameCheckUtil;
import com.codestates.edusync.model.study.study.entity.Study;
import com.codestates.edusync.model.study.study.repository.StudyRepository;
import com.codestates.edusync.model.study.studyjoin.entity.StudyJoin;
import com.codestates.edusync.model.study.studyjoin.repository.StudyJoinRepository;
import com.codestates.edusync.security.auth.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {
    private final CustomAuthorityUtils authorityUtils;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository repository;
    private final StudyRepository studyRepository;
    private final StudyJoinRepository joinRepository;
    private final AwsS3Service awsS3Service;
    private final NickNameCheckUtil nickNameCheckUtil;
    private final MemberDtoMapper dtoMapper;

    /**
     * 회원 가입
     * @param member Member
     * @return Member
     */
    public void create(Member member) {
        // 이메일 중복 체크
        String email = member.getEmail();
        Optional<Member> optionalMember = repository.findByEmail(email);
        if (optionalMember.isPresent()) {
            Member findMember = optionalMember.get();

            if (findMember.getStatus().equals(Member.Status.ACTIVE)) {
                throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS_EMAIL, String.format("%s는 이미 가입한 이메일입니다.", email));
            } else if (findMember.getStatus().equals(Member.Status.SLEEP)) {
                throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS_EMAIL, String.format("%s는 현재 휴먼 계정입니다.", email));
            } else if (findMember.getStatus().equals(Member.Status.QUIT)) {
                throw new BusinessLogicException(ExceptionCode.INACTIVE_MEMBER, String.format("%s는 이미 탈퇴한 계정입니다.", email));
            }
        }

        // 닉네임 중복 체크
        String nickName = member.getNickName();
        optionalMember = repository.findByNickName(nickName);
        if (optionalMember.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS_NICKNAME, String.format("%s는 이미 사용중인 닉네임입니다.", nickName));

        // 닉네임 금지어 체크
        nickNameCheckUtil.validated(nickName);

        // 멤버 추가 로직
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);

        repository.save(member);
    }

    /**
     * 닉네임 수정
     * @param member Member
     * @param email String
     */
    //@Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateNickName(Member member, String email) {

        Member findMember = get(email);

        // 닉네임 중복 체크
        String nickName = member.getNickName();
        Optional<Member> opMember = repository.findByNickName(nickName);
        if (opMember.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS_NICKNAME, String.format("%s는 이미 사용중인 닉네임입니다.", nickName));

        // 닉네임 금지어 체크
        nickNameCheckUtil.validated(nickName);


        Optional.ofNullable(member.getNickName())
                .ifPresent(name -> {
                    if (!name.isEmpty()) {
                        findMember.setNickName(name);
                    }
                });

        repository.save(findMember);
    }

    /**
     * 패스워드 수정
     * @param member Member
     * @param email String
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updatePassword(Member member, String email) {

        Member findMember = get(email);

        Optional.ofNullable(member.getPassword())
                .ifPresent(password -> {
                    if (!password.isEmpty()) {
                        findMember.setPassword(passwordEncoder.encode(password));
                    }
                });

        repository.save(findMember);
    }

    /**
     * 자기소개 수정
     * @param member Member
     * @param email String
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateAboutMe(Member member, String email) {
        Member findMember = get(email);
        Optional.ofNullable(member.getAboutMe()).ifPresent(findMember::setAboutMe);
        repository.save(findMember);
    }

    /**
     * 이미지 수정
     * @param email String
     * @param image Image
     */
    public void updateImage(MultipartFile image, String email) {
        Member findMember = get(email);
        findMember.setImage(awsS3Service.uploadImage(image, "/member"));
        repository.save(findMember);
    }

    /**
     * 회원 조회 - 이메일
     * @param email String
     * @return Member
     */
    @Transactional(readOnly = true)
    public Member get(String email) {
        Optional<Member> optionalMember = repository.findByEmail(email);

        Member findMember = optionalMember.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND, String.format("%s 회원을 찾을 수 없습니다.", email)));

        if (findMember.getStatus().equals(Member.Status.SLEEP)) {
            throw new BusinessLogicException(ExceptionCode.INACTIVE_MEMBER, String.format("%s는 현재 휴먼 계정입니다.", email));
        } else if (findMember.getStatus().equals(Member.Status.QUIT)) {
            throw new BusinessLogicException(ExceptionCode.INACTIVE_MEMBER, String.format("%s는 이미 탈퇴한 계정입니다.", email));
        }

        return findMember;
    }

    /**
     * 회원 조회 - 닉네임
     * @param nickName
     * @return
     */
    @Transactional(readOnly = true)
    public Member getNickName(String nickName) {

        Optional<Member> optionalMember = repository.findByNickName(nickName);

        Member findMember = optionalMember.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND, String.format("%s 회원을 찾을 수 없습니다.", nickName)));

        if (findMember.getStatus().equals(Member.Status.SLEEP)) {
            throw new BusinessLogicException(ExceptionCode.INACTIVE_MEMBER, String.format("%s는 현재 휴먼 계정입니다.", nickName));
        } else if (findMember.getStatus().equals(Member.Status.QUIT)) {
            throw new BusinessLogicException(ExceptionCode.INACTIVE_MEMBER, String.format("%s는 이미 탈퇴한 계정입니다.", nickName));
        }

        return findMember;
    }



    /**
     * 스터디 가입 신청자 | 스터디 멤버 리스트 조회
     * @param studyId
     * @param email
     * @return
     */
    @Transactional(readOnly = true)
    public MemberDto.MembersResponse getStudyMembers(Long studyId, String email, Boolean isMember) {

        get(email);
        Study study =
                studyRepository.findById(studyId)
                        .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STUDYGROUP_NOT_FOUND));

        List<StudyJoin> studyJoinList;

        if (Boolean.TRUE.equals(isMember)) {
            studyJoinList = joinRepository.findAllByStudyAndIsApprovedIsTrue(study);

            studyJoinList
                    .stream()
                    .filter(e -> e.getMember().getEmail().equals(email))
                    .findFirst()
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        } else {
            if (!study.getLeader().getEmail().equals(email)) {
                throw new BusinessLogicException(ExceptionCode.INVALID_PERMISSION);
            }

            studyJoinList = joinRepository.findAllByStudyAndIsApprovedIsFalse(study);
        }

        return dtoMapper.studyJoinListToStudyMembersDto(studyJoinList);
    }

    /**
     * 패스워드 인증
     * @param password String
     * @param email String
     * @return boolean
     */
    @Transactional(readOnly = true)
    public boolean checkPassword(String email, String password){
        return passwordEncoder.matches(password, get(email).getPassword());
    }

    /**
     * 회원 가입 구분
     * @param email String
     * @return Map<String, String>
     */
    @Transactional(readOnly = true)
    public Map<String, String> getProvider(String email){

        Member findMember = get(email);

        String provider;

        if(findMember.getProvider()==Member.Provider.LOCAL){
            provider = "LOCAL";
        }else if(findMember.getProvider()==Member.Provider.GOOGLE){
            provider = "GOOGLE";
        }else if(findMember.getProvider()==Member.Provider.NAVER){
            provider = "NAVER";
        }else if(findMember.getProvider()==Member.Provider.KAKAO){
            provider = "KAKAO";
        }else{
            provider = "Error";
        }

        Map<String, String> response = new HashMap<>();
        response.put("provider", provider);

        return response;
    }

    /**
     * 휴먼 회원 해제
     * @param email String
     * @param password String
     */
    public void updateStatus(String email, String password){
        // 회원 인지 확인
        Optional<Member> optionalMember = repository.findByEmail(email);

        Member findMember = optionalMember.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND, String.format("%s 회원을 찾을 수 없습니다.", email)));

        if (!passwordEncoder.matches(password, findMember.getPassword())) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_PASSWORD_ERROR);
        } else {
            if (findMember.getStatus().equals(Member.Status.ACTIVE)) {
                throw new BusinessLogicException(ExceptionCode.MEMBER_ALREADY_ACTIVE);
            } else if (findMember.getStatus().equals(Member.Status.SLEEP)) {
                findMember.setStatus(Member.Status.ACTIVE);
                repository.save(findMember);
            } else if (findMember.getStatus().equals(Member.Status.QUIT)) {
                throw new BusinessLogicException(ExceptionCode.INACTIVE_MEMBER);
            }
        }
    }

    /**
     * 회원 탈퇴(소프트 삭제)
     * @param email String
     */
    public void delete(String email) {
        Member findMember = get(email);
        findMember.setStatus(Member.Status.QUIT);
        repository.save(findMember);
    }
}
