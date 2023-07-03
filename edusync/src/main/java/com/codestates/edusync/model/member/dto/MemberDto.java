package com.codestates.edusync.model.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class MemberDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Validated
    public static class Post{
        @Email(message = "올바른 이메일 형태가 아닙니다.")
        private String email;

        @NotBlank(message = "비밀번호는 공백이 아니어야 합니다.")
        private String password;

        @NotBlank(message = "닉네임은 공백이 아니어야 합니다.")
        private String nickName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Validated
    public static class PatchNickName {
        @NotBlank(message = "닉네임은 공백이 아니어야 합니다.")
        private String nickName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Validated
    public static class PatchPassword {
        @NotBlank(message = "비밀번호는 공백이 아니어야 합니다.")
        private String Password;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PatchAboutMe {
        private String aboutMe;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PatchWithMe {
        private String withMe;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Response {
        private String uuid;
        private String email;
        private String nickName;
        private String image;
        private String aboutMe;
        private String withMe;
        private List<String> roles;
    }
}
