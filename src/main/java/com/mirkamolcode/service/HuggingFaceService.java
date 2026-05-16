package com.mirkamolcode.service;

import com.mirkamolcode.config.HttpClient;
import com.mirkamolcode.dto.AiTicketResponse;
import com.mirkamolcode.dto.api.HuggingFaceComment;
import com.mirkamolcode.dto.api.HuggingFaceRequest;
import com.mirkamolcode.dto.api.HuggingFaceResponse;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class HuggingFaceService {
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public HuggingFaceService(HttpClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public AiTicketResponse analyzeMessage(String content) {
        String prompt = """
                 Analyze this inbound message.
                
                                Decide if it should become a qualified lead.
                
                                Return ONLY valid JSON.
                
                                {
                                  "qualified": true,
                                  "title": "short title",
                                  "type": "bug | feature | billing | account | other",
                                  "urgency": "low | medium | high",
                                  "summary": "short summary"
                                }
                
                                Rules:
                                - type MUST be exactly one of:
                                  bug, feature, billing, account, other
                
                                - urgency MUST be exactly one of:
                                  low, medium, high
                
                                Message:
                                %s
                """.formatted(content);

        HuggingFaceRequest huggingFaceRequest = new HuggingFaceRequest(
                "meta-llama/Llama-3.3-70B-Instruct:groq",
                List.of(
                        new HuggingFaceComment("system", "You are lead qualification assistant"),
                        new HuggingFaceComment("user", prompt)),
                false
        );
        try {
            HuggingFaceResponse response = client.completions(huggingFaceRequest);

            String aiJson = response.choices()
                    .getFirst()
                    .comment()
                    .content();

            return objectMapper.readValue(aiJson, AiTicketResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to analyze Ai response",
                    e
            );
        }

    }
}
