package com.codestates.edusync.model.member.utils;

import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Component
public class MemberUtils {
    private final MemberRepository repository;

    public void checkIsActive(Member member) {
        if (member.getStatus() != Member.Status.MEMBER_ACTIVE) {
            throw new BusinessLogicException(ExceptionCode.INACTIVE_MEMBER,
                    String.format("멤버(%s)는 활성화되지 않았습니다. 해당 요청을 처리할 수 없습니다.", member.getEmail()));
        }
    }


    public Member get(String email) {
        Optional<Member> optionalMember =
                repository.findByEmail(email);

        Member findMember =
                optionalMember.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND, String.format("%s 회원을 찾을 수 없습니다.", email)));
        checkIsActive(findMember);

        return findMember;
    }


    public Member getLoggedIn(String email) {
        return get(email);
    }



    public Member getLoggedInWithAuthenticationCheck(Authentication authentication) {
        if( authentication == null ) {
            throw new BusinessLogicException(ExceptionCode.AUTHENTICATION_NOT_NULL_ALLOWED);
        }

        return getLoggedIn(authentication.getPrincipal().toString());
    }
}
