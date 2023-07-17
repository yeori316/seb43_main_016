package com.codestates.edusync.model.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
                message = "비밀번호는 8~25자리의 영문 대소문자, 숫자, 특수문자 조합이어야 합니다.")
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
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
                message = "비밀번호는 8~25자리의 영문 대소문자, 숫자, 특수문자 조합이어야 합니다.")
        private String password;
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
    public static class MyInfo {
        private String email;
        private String nickName;
        private String image;
        private String aboutMe;
        private List<String> roles;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class MemberResponse {
        private String nickName;
        private String image;
        private String aboutMe;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class MembersResponse {
        private List<String> nickName;
    }
}
