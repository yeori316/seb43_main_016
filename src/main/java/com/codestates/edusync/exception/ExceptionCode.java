package com.codestates.edusync.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionCode {

    /**
     * Member 에러 코드
     */
    MEMBER_NICKNAME_NOT_ALLOWED(403, "해당 닉네임은 사용할 수 없는 닉네임 입니다."),
    MEMBER_NOT_FOUND(404, "회원님을 찾을수 없습니다."),
    MEMBER_EXISTS_EMAIL(409, "해당 이메일로 가입한 계정이 있습니다."),
    MEMBER_EXISTS_NICKNAME(409, "중복된 닉네임 입니다."),
    MEMBER_PASSWORD_ERROR(400, "패스워드를 확인해 주세요."),
    MEMBER_ALREADY_ACTIVE(409, "회원님의 계정 상태는 이미 정상입니다."),
    INACTIVE_MEMBER(403, "해당 계정은 탈퇴한 계정입니다."),

    /**
     * Study 에러 코드
     */
    STUDY_NOT_FOUND(404, "스터디를 찾을 수 없습니다."),
    STUDY_NAME_EXISTS(409, "동일한 이름의 스터디가 존재합니다."),
    STUDY_RECRUITED_NOT_MODIFIED(403, "스터디 모집이 완료 되었습니다."),

    /**
     * Comment 에러 코드
     */
    COMMENT_NOT_FOUND(404, "해당 댓글을 찾을 수 없습니다."),

    /**
     * Schedule 에러 코드
     */
    SCHEDULE_NOT_FOUND(404, "일정이 존재하지 않습니다."),

    /**
     * 스터디 그룹 신청 관련 에렄 코드
     */
    STUDY_JOIN_NOT_FOUND(404, "가입된 스터디를 찾을 수 없습니다."),
    STUDY_JOIN_CANDIDATE_NOT_FOUND(404, "스터디에 신청한 내역이 없습니다."),
    STUDY_JOIN_EXISTS(409, "이미 가입된 스터디 입니다."),
    STUDY_JOIN_CANDIDATE_EXISTS(409, "이미 신청한 스터디 입니다."),

    /**
     * 기타 에러 코드
     */
    INVALID_PROVIDER(400, "지원하지 않는 인증 제공자입니다."),

    INVALID_PERMISSION(403, "권한이 유효하지 않습니다."),

    ;


    private final int status;

    private final String message;
}
