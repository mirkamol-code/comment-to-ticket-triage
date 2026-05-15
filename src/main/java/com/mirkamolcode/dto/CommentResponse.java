package com.mirkamolcode.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        String senderEmail,
        String content,
        LocalDateTime createdAt
) {
}
