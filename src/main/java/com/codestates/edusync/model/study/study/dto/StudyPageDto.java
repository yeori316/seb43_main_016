package com.codestates.edusync.model.study.study.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

public class StudyPageDto {

    @AllArgsConstructor
    @Getter
    public static class PageInfo {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
    }

    @Getter
    public static class ResponsePage<T> {
        private final T study;
        private final PageInfo pageInfo;

        public ResponsePage(T data, Page page) {
            this.study = data;
            this.pageInfo =
                    new PageInfo(
                            page.getNumber() + 1,
                            page.getSize(),
                            page.getTotalElements(),
                            page.getTotalPages()
                    );
        }
    }

    @AllArgsConstructor
    @Getter
    public static class ResponseList<T> {
        private T study;
    }
}

