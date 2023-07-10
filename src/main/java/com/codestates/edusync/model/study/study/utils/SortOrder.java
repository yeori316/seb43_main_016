package com.codestates.edusync.model.study.study.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum SortOrder {
    SORT_BY_ID("recent", "id"),
    SORT_BY_MODIFIED_AT("modified", "modifiedAt"),
    SORT_BY_CATEGORY("category", "searchTags.tagKey"),
    SORT_BY_RECRUITED("status", "isRecruited"),
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