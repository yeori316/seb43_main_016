package com.codestates.edusync.model.study.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

public class CommentPageDto {

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
        private final T comment;
        private final PageInfo pageInfo;

        public ResponsePage(T data, Page page) {
            this.comment = data;
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
        private T data;
    }
}
