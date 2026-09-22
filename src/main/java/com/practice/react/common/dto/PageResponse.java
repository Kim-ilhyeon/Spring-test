package com.practice.react.common.dto;

import java.util.List;

/** 목록 조회 결과와 페이지 계산 결과를 함께 전달하는 공통 DTO. */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext
) {
    /** 전체 건수와 페이지 크기로 전체 페이지·다음 페이지 존재 여부를 계산한다. */
    public static <T> PageResponse<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PageResponse<>(content, page, size, totalElements, totalPages, page < totalPages);
    }
}
