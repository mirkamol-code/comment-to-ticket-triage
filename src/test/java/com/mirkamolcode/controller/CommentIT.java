package com.mirkamolcode.controller;

import com.mirkamolcode.AbstractTestConfig;
import com.mirkamolcode.dto.CommentResponse;
import com.mirkamolcode.dto.CreateCommentRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentIT extends AbstractTestConfig {
    private static final String PRODUCT_BASE_URL = "/messages";
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canCreateComment() {
        createComment(
                new CreateCommentRequest(
                        "test@gmail.com",
                        "I am having issue with payment"
                )
        );

    }

    @Test
    void canGetAllComments() {
        // given
        CreateCommentRequest commentRequest1 = new CreateCommentRequest(
                "test1@gmail.com",
                "I am having issue with payment"
        );
        createComment(commentRequest1);
        CreateCommentRequest commentRequest2 = new CreateCommentRequest(
                "test2@gmail.com",
                "I am having issue with login"
        );
         createComment(commentRequest2);
        // when
        List<CommentResponse> comments = webTestClient.get()
                .uri(PRODUCT_BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CommentResponse>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(comments)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "createdAt")
                .containsExactly(
                        new CommentResponse(null,
                                commentRequest1.senderEmail(),
                                commentRequest1.content(),
                                null
                        ),
                        new CommentResponse(null,
                                commentRequest2.senderEmail(),
                                commentRequest2.content(),
                                null
                        )
                );
    }


    private void createComment(CreateCommentRequest request) {
        webTestClient.post()
                .uri(PRODUCT_BASE_URL)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CommentResponse>() {
                })
                .value(c -> assertThat(c).isNotNull());
    }


}
