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
                You are an AI assistant that classifies inbound business messages.
                
                Analyze the message and determine whether it should become a qualified lead.
                
                Return ONLY raw valid JSON.
                Do NOT use markdown.
                Do NOT use ```json.
                Do NOT add explanations or extra text.
                
                Required JSON structure:
                
                {
                  "qualified": true,
                  "title": "Short professional title",
                  "category": "BUG | FAILURE | BILLING | ACCOUNT | OTHER",
                  "priority": "LOW | MEDIUM | HIGH",
                  "summary": "Concise professional summary"
                }
                
                Rules:
                - category MUST be exactly one of:
                  BUG, FAILURE, BILLING, ACCOUNT, OTHER
                
                - priority MUST be exactly one of:
                  LOW, MEDIUM, HIGH
                
                - Return enum values in uppercase only
                - Summary must be rewritten professionally
                - Do NOT copy the original message directly
                - Keep summary short and clear
                
                Classification guidelines:
                - BUG → technical errors, crashes, broken APIs
                - FAILURE → outages, malfunctioning systems, broken workflows
                - BILLING → invoices, payments, pricing, double charges
                - ACCOUNT → login, password, account access issues
                - OTHER → anything else
                
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
                    .message()
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