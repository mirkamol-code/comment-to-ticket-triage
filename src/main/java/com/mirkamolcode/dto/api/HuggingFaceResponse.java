package com.mirkamolcode.dto.api;

import java.util.List;

public record HuggingFaceResponse(
        List<Choice> choices
) {
    public record Choice(
            Comment message
    ){}
    public record Comment(
            String content
    ){}
}
