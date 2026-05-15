package com.mirkamolcode.dto;

import com.mirkamolcode.entity.enums.Category;
import com.mirkamolcode.entity.enums.Priority;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        String title,
        Category category,
        Priority priority,
        String summary,
        @CreationTimestamp
        LocalDateTime createdAt
) {
}
