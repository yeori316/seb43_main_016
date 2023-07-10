package com.codestates.edusync.model.study.study.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.YearMonthDay;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
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
        private List<Integer> dayOfWeek;

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate startDate;

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate endDate;

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime startTime;

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime endTime;

        private List<String> tags;
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
        private LocalDate startDate;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;

        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime startTime;

        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime endTime;

        private List<String> tags;
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
        private LocalDate startDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate endDate;

        private List<Integer> dayOfWeek;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime startTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime endTime;

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
        private List<String> tags;
    }
}
