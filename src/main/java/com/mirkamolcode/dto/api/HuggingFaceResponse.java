package com.mirkamolcode.dto.api;

import java.util.List;

public record HuggingFaceResponse(
        List<Comment> choices
) {
    public record Choice(
            Comment message
    ){}
    public record Comment(
            String content
    ){}
}
