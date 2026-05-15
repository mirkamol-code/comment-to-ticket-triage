package com.mirkamolcode.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest (
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String senderEmail,
        @NotBlank(message = "Content is required")
        String content
){}
