package com.codestates.edusync.security.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginDto {
    private String email;
    private String password;
}
