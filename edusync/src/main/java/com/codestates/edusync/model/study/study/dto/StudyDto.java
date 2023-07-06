package com.codestates.edusync.model.study.study.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

public class StudyDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Validated
    public static class Post {
        @NotNull
        private String studyName;

        @Positive
        private Integer memberMin;

        @Positive
        private Integer memberMax;

        @NotNull
        private String platform;

        @NotNull
        private String introduction;

        @NotNull
        private List<Integer> DayOfWeek;

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime startDate;

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime endDate;

        @NotNull
        @DateTimeFormat(pattern = "HH:mm")
        private LocalDateTime startTime;

        @NotNull
        @DateTimeFormat(pattern = "HH:mm")
        private LocalDateTime endTime;

        private String tags;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Validated
    public static class Patch {

        private String studyName;

        @Positive
        private Integer memberMin;

        @Positive
        private Integer memberMax;

        private String platform;

        private String introduction;

        private List<Integer> DayOfWeek;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime startDate;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime endDate;

        @DateTimeFormat(pattern = "HH:mm")
        private LocalDateTime startTime;

        @DateTimeFormat(pattern = "HH:mm")
        private LocalDateTime endTime;

        private String tags;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PatchLeader {
        @NotNull
        private String nickName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private String studyName;
        private String image;
        private Integer memberMin;
        private Integer memberMax;

        private Integer memberCnt;

        private String platform;
        private String introduction;
        private Boolean isRecruited;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime startDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime endDate;

        private List<Integer> dayOfWeek;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime startTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime endTime;

        private List<String> tags;
        private String leaderNickName;
        private Boolean isLeader;
    }

    /**
     * 스터디 조회
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Summary {
        private Long id;
        private String image;
        private String title;
        private List<String> tagValues;
    }
}
