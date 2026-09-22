package com.practice.react.post.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotNull @Min(1) Short categoryId,
        @NotBlank @Size(max = 63) String title,
        @NotBlank @Size(max = 30000) String content
) { }
