package com.codestates.edusync.model.member.service;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.common.service.AwsS3ServiceInterface;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MemberService implements MemberServiceInterface {
    private final MemberRepository repository;
    private final StudyRepository studyRepository;
    private final StudyJoinRepository joinRepository;
    private final AwsS3ServiceInterface awsS3Service;
    private final CustomAuthorityUtils authorityUtils;
    private final PasswordEncoder passwordEncoder;
    private final NickNameCheckUtil nickNameCheckUtil;
    private final MemberDtoMapper dtoMapper;

    public void create(Member member) {

        // 이메일 중복 체크
        Optional<Member> optionalMember = repository.findByEmail(member.getEmail());

        if (optionalMember.isPresent()) {
            Member findMember = optionalMember.get();

            if (findMember.getStatus().equals(Member.Status.ACTIVE)) {
                throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS_EMAIL);
            } else if (findMember.getStatus().equals(Member.Status.SLEEP)) {
                throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS_EMAIL);
            } else if (findMember.getStatus().equals(Member.Status.QUIT)) {
                throw new BusinessLogicException(ExceptionCode.INACTIVE_MEMBER);
            }
        }

        nickNameCheck(member);

        // 멤버 셋팅
        String encryptedPassword = passEncoding(member.getPassword());
        member.setPassword(encryptedPassword);

        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);

        repository.save(member);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateNickName(Member member, String email) {

        Member findMember = get(email);

        nickNameCheck(member);

        Optional.ofNullable(member.getNickName())
                .ifPresent(name -> {
                    if (!name.isEmpty()) {
                        findMember.setNickName(name);
                    }
                });

        repository.save(findMember);
    }

    public void updatePassword(Member member, String email) {

        Member findMember = get(email);

        Optional.ofNullable(member.getPassword())
                .ifPresent(password -> {
                    if (!password.isEmpty()) {
                        findMember.setPassword(passEncoding(password));
                    }
                });

        repository.save(findMember);
    }

    public void updateAboutMe(Member member, String email) {
        Member findMember = get(email);
        Optional.ofNullable(member.getAboutMe()).ifPresent(findMember::setAboutMe);
        repository.save(findMember);
    }

    public void updateImage(MultipartFile image, String email) {
        String imageAddress = awsS3Service.uploadImage(image, "/member");
        Member findMember = get(email);
        findMember.setImage(imageAddress);
        repository.save(findMember);
    }

    @Transactional(readOnly = true)
    public Member get(String email) {
        Optional<Member> optionalMember = repository.findByEmail(email);
        return verifyMember(optionalMember);
    }

    @Transactional(readOnly = true)
    public Member getNickName(String nickName) {
        Optional<Member> optionalMember = repository.findByNickName(nickName);
        return verifyMember(optionalMember);
    }

    @Transactional(readOnly = true)
    public Member getNickName(String nickName, String email) {
        get(email);
        Optional<Member> optionalMember = repository.findByNickName(nickName);
        return verifyMember(optionalMember);
    }

    @Transactional(readOnly = true)
    public MemberDto.MembersResponse getStudyMembers(Long studyId, String email, Boolean isMember) {

        get(email);

        Study study = studyRepository.findById(studyId)
                        .orElseThrow(() -> new BusinessLogicException(ExceptionCode.STUDY_NOT_FOUND));

        List<StudyJoin> studyJoinList;

        if (Boolean.TRUE.equals(isMember)) {
            studyJoinList = joinRepository.findAllByStudyAndIsApprovedIsTrue(study);

            studyJoinList.stream()
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

    @Transactional(readOnly = true)
    public boolean checkPassword(String email, String password){
        return passwordEncoder.matches(password, get(email).getPassword());
    }

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
            } else if (findMember.getStatus().equals(Member.Status.QUIT)) {
                throw new BusinessLogicException(ExceptionCode.INACTIVE_MEMBER);
            } else if (findMember.getStatus().equals(Member.Status.SLEEP)) {
                findMember.setStatus(Member.Status.ACTIVE);
                repository.save(findMember);
            }
        }
    }

    public void delete(String email) {
        Member findMember = get(email);
        findMember.setStatus(Member.Status.QUIT);
        findMember.setDeletedAt(LocalDateTime.now());
        repository.save(findMember);
    }

    @Transactional(readOnly = true)
    public void nickNameCheck(Member member) {

        String nickName = member.getNickName();
        Optional<Member> opMember = repository.findByNickName(nickName);
        if (opMember.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS_NICKNAME);

        nickNameCheckUtil.validated(nickName);
    }

   /**
    * 패스워드 암호화
    * @param password Password
    * @return String
    */
    private String passEncoding(String password) {
        return passwordEncoder.encode(password);
    }

   /**
    * 멤버 조회 검증
    * @param optionalMember OptionalMember
    * @return Member
    */
    private Member verifyMember(Optional<Member> optionalMember) {

        Member findMember = optionalMember.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        if (findMember.getStatus().equals(Member.Status.SLEEP) || findMember.getStatus().equals(Member.Status.QUIT)) {
            throw new BusinessLogicException(ExceptionCode.INACTIVE_MEMBER);
        }

        return findMember;
    }
}
