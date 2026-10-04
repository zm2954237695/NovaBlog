package com.novablog.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotBlank @Size(max = 160) String title,
        @Size(max = 180) String slug,
        @Size(max = 500) String summary,
        @NotBlank String contentMarkdown,
        String contentHtml,
        @Size(max = 500) String coverUrl,
        String status,
        Boolean featured,
        Long categoryId
) {
}
