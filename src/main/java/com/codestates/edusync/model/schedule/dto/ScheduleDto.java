package com.codestates.edusync.model.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

public class ScheduleDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Post {
        @NotNull
        private String title;

        private String description;

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;

        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime startTime;

        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime endTime;

        private String color;

        private List<Integer> dayOfWeek;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Patch {
        private String title;

        private String description;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;

        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime startTime;

        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime endTime;

        private String color;

        private List<Integer> dayOfWeek;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private String title;

        private String description;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;

        @JsonFormat(pattern = "HH:mm")
        private LocalTime startTime;

        @JsonFormat(pattern = "HH:mm")
        private LocalTime endTime;

        private String color;

        private List<Integer> dayOfWeek;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ResponseList {
        private List<Response> ResponseList;
    }
}
