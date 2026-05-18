package com.mirkamolcode.service;

import com.mirkamolcode.config.HttpClient;
import com.mirkamolcode.dto.AiTicketResponse;
import com.mirkamolcode.dto.api.HuggingFaceComment;
import com.mirkamolcode.dto.api.HuggingFaceRequest;
import com.mirkamolcode.dto.api.HuggingFaceResponse;
import com.mirkamolcode.dto.api.HuggingFaceResponse.Choice;
import com.mirkamolcode.entity.enums.Category;
import com.mirkamolcode.entity.enums.Priority;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static com.mirkamolcode.dto.api.HuggingFaceResponse.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class HuggingFaceServiceTest {
    @Mock
    private HttpClient client;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private HuggingFaceService underTest;

    @Test
    void shouldReturnAiTicketResponseWhenMessageIsAnalyzedSuccessfully() {
        // given
        String content = "Our payment was charged twice";

        String aiJson = """
                {
                  "qualified": true,
                  "title": "Duplicate payment detected",
                  "category": "BILLING",
                  "priority": "HIGH",
                  "summary": "Customer reported duplicate billing charges."
                }
                """;

        AiTicketResponse expectedResponse = new AiTicketResponse(
                true,
                "Duplicate payment detected",
                Category.BILLING,
                Priority.HIGH,
                "Customer reported duplicate billing charges."
        );

        Comment message = mock(Comment.class);
        given(message.content()).willReturn(aiJson);

        Choice choice = mock(Choice.class);
        given(choice.message()).willReturn(message);

        HuggingFaceResponse response = mock(HuggingFaceResponse.class);
        given(response.choices()).willReturn(List.of(choice));

        given(client.completions(any(HuggingFaceRequest.class)))
                .willReturn(response);

        given(objectMapper.readValue(aiJson, AiTicketResponse.class))
                .willReturn(expectedResponse);

        // when
        AiTicketResponse result = underTest.analyzeMessage(content);

        // then
        assertThat(result).isNotNull();
        assertThat(result.qualified()).isTrue();
        assertThat(result.category()).isEqualTo(Category.BILLING);
        assertThat(result.priority()).isEqualTo(Priority.HIGH);
        assertThat(result).isEqualTo(expectedResponse);

        then(client).should()
                .completions(any(HuggingFaceRequest.class));

        then(objectMapper).should()
                .readValue(aiJson, AiTicketResponse.class);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenAiClientFails() {
        // given
        String content = "API is down";

        given(client.completions(any(HuggingFaceRequest.class)))
                .willThrow(new RuntimeException("API failure"));


        // then
        assertThatThrownBy(() -> underTest.analyzeMessage(content))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to analyze Ai response");

        then(client).should()
                .completions(any(HuggingFaceRequest.class));

        then(objectMapper).shouldHaveNoInteractions();
    }
}