package com.mirkamolcode.dto.api;

import java.util.List;

public record HuggingFaceRequest(
        String model,
        List<HuggingFaceComment> messages,
        boolean stream
) {
}
