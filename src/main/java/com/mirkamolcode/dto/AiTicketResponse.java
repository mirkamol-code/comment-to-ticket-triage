package com.mirkamolcode.dto;

import com.mirkamolcode.entity.enums.Category;
import com.mirkamolcode.entity.enums.Priority;

public record AiTicketResponse(
        boolean qualified,
        String title,
        Category category,
        Priority priority,
        String summary
) {
}
