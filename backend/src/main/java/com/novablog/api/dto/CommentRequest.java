package com.novablog.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        Long parentId,
        @NotBlank @Size(max = 60) String nickname,
        @Email @Size(max = 120) String email,
        @NotBlank @Size(max = 1000) String content
) {
}
