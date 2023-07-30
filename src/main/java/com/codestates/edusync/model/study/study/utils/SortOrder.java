package com.codestates.edusync.model.study.study.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum SortOrder {
    SORT_BY_ID("recent", "id"), // 최근
    SORT_BY_MODIFIED_AT("modified", "modifiedAt"), // 수정 일자
    SORT_BY_RECRUITED("status", "isRecruited"), // 모집 상태별
    SORT_BY_VIEWS("views", "views"), // 조회 순
    ;

    private final String order;
    private final String value;

    public static String getString (String order) {
        return Arrays.stream(SortOrder.values())
                .filter(e -> e.getOrder().equals(order))
                .findFirst()
                .map(SortOrder::getValue)
                .orElse("id");
    }
}