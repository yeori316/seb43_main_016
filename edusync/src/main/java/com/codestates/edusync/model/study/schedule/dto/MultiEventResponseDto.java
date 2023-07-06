package com.codestates.edusync.model.study.schedule.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class MultiEventResponseDto<T> {
    private List<T> events;
}
