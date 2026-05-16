package com.mirkamolcode.config;

import com.mirkamolcode.dto.api.HuggingFaceRequest;
import com.mirkamolcode.dto.api.HuggingFaceResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/chat/completions")
public interface HttpClient {
    @PostExchange
    HuggingFaceResponse completions(@RequestBody HuggingFaceRequest huggingFaceRequest);
}
