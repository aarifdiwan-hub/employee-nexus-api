package com.bmo.dto;

import java.util.List;

/**
 * Custom pagination response wrapper.
 * Provides a consistent structure for paginated responses with metadata.
 * Uses nested record for pagination metadata.
 *
 * @param <T> Type of content being paginated
 */
public record PageResponseDto<T>(
    List<T> content,
    PaginationMetadata metadata
) {
    /**
     * Nested record for pagination metadata.
     * Contains information about the current page and total results.
     */
    public record PaginationMetadata(
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
    ) {}

    /**
     * Factory method to create PageResponseDto from Spring Data Page.
     *
     * @param page Spring Data Page object
     * @param <T> Type of content
     * @return PageResponseDto containing content and metadata
     */
    public static <T> PageResponseDto<T> from(org.springframework.data.domain.Page<T> page) {
        return new PageResponseDto<>(
            page.getContent(),
            new PaginationMetadata(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
            )
        );
    }
}