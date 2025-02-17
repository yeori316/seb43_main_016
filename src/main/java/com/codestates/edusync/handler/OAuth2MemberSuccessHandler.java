package com.codestates.edusync.handler;

import com.codestates.edusync.security.auth.jwt.JwtTokenizer;
import com.codestates.edusync.security.auth.token.TokenService;
import com.codestates.edusync.security.auth.utils.CustomAuthorityUtils;
import com.codestates.edusync.exception.BusinessLogicException;
import com.codestates.edusync.exception.ExceptionCode;
import com.codestates.edusync.model.member.entity.Member;
import com.codestates.edusync.model.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;

//  OAuth 2 인증 후, Frontend 애플리케이션 쪽으로 JWT를 전송하는 역할
@Slf4j
@Component
public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    public OAuth2MemberSuccessHandler(JwtTokenizer jwtTokenizer,
                                      CustomAuthorityUtils authorityUtils, MemberRepository memberRepository, TokenService tokenService) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String providerType = authToken.getAuthorizedClientRegistrationId();

        String email;
        String nickName;
        String profileImage;
        Member.Provider provider;

        if ("google".equals(providerType)) {
            email = String.valueOf(oAuth2User.getAttributes().get("email"));
            nickName = String.valueOf(oAuth2User.getAttributes().get("name"));
            profileImage = String.valueOf(oAuth2User.getAttributes().get("picture"));
            provider = Member.Provider.GOOGLE;
        } else if ("kakao".equals(providerType)) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakaoAccount.get("email");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            nickName = (String) profile.get("nickname");
            profileImage = (String) profile.get("profile_image_url");
            provider = Member.Provider.KAKAO;
        } else if ("naver".equals(providerType)) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> response2 = (Map<String, Object>) attributes.get("response");
            email = (String) response2.get("email");
            nickName = (String) response2.get("nickname");
            profileImage = (String) response2.get("profile_image");
            provider = Member.Provider.NAVER;
        } else {
            throw new BusinessLogicException(ExceptionCode.INVALID_PROVIDER, "Unsupported provider: " + providerType);
        }

        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        Member member;
        if (optionalMember.isEmpty()) {
            member = saveMember(email, nickName, profileImage, provider);
        } else {
            member = optionalMember.get();
        }
        redirect(request, response, member);
    }

    private Member saveMember(String email, String nickname, String profileImage, Member.Provider provider) {
        memberRepository.findByEmail(email).ifPresent(it ->
        {throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS_EMAIL, String.format("%s is duplicated 버그발생! OAuth2 핸들러 검사하시오.", email));
        });
        Member member = new Member();
        member.setEmail(email);
        member.setNickName(nickname);
        member.setImage(profileImage);
        List<String> roles = authorityUtils.createRoles(email);
        member.setRoles(roles);
        member.setProvider(provider);
        Member savedMember = memberRepository.save(member);

        return savedMember;
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, Member member) throws IOException {
        String accessToken = tokenService.delegateAccessToken(member);
        String refreshToken = tokenService.delegateRefreshToken(member);

        response.setHeader("Authorization", accessToken);
        response.setHeader("Refresh", refreshToken);

        String uri = createURI(accessToken, refreshToken).toString();
        getRedirectStrategy().sendRedirect(request, response, uri);

        log.info("# Authenticated successfully!");

        // response 헤더 정보 로그 출력
        for (String headerName : response.getHeaderNames()) {
            log.info(headerName + ": " + response.getHeader(headerName));
        }
    }

    private URI createURI(String accessToken, String refreshToken) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);

        return UriComponentsBuilder
                .newInstance()
                .scheme("https")
                .host("www.edusync.site")
                .path("/oauth/redirect")
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
