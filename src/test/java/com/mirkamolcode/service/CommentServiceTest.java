package com.mirkamolcode.service;

import com.mirkamolcode.dto.AiTicketResponse;
import com.mirkamolcode.dto.CommentResponse;
import com.mirkamolcode.dto.CreateCommentRequest;
import com.mirkamolcode.entity.Comment;
import com.mirkamolcode.entity.Ticket;
import com.mirkamolcode.entity.enums.Category;
import com.mirkamolcode.entity.enums.Priority;
import com.mirkamolcode.repository.CommentRepository;
import com.mirkamolcode.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private HuggingFaceService huggingFaceService;

    @Mock
    private CommentRepository commentRepository;


    @InjectMocks
    private CommentService underTest;

    @Test
    void shouldSaveCommentAndCreateTicketWhenQualified() {
        // given
        CreateCommentRequest commentRequest = new CreateCommentRequest("atbc@gmail.com", "Payment system is broken");
        Comment commentAboutBilling = new Comment();
        commentAboutBilling.setSenderEmail(commentRequest.senderEmail());
        commentAboutBilling.setContent(commentRequest.content());

        AiTicketResponse aiResponse = new AiTicketResponse(
                true,
                "Payment Failure",
                Category.BILLING,
                Priority.HIGH,
                "Customer can't complete payment"

        );
        given(commentRepository.save(any())).willReturn(commentAboutBilling);
        given(huggingFaceService.analyzeMessage(commentAboutBilling.getContent())).willReturn(aiResponse);
        // when
        CommentResponse actual = underTest.save(commentRequest);
        // then
        assertThat(actual).isNotNull();
        verify(commentRepository).save(commentAboutBilling);
        verify(huggingFaceService).analyzeMessage(commentAboutBilling.getContent());
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void shouldSaveCommentWithoutCreatingTicketWhenNotQualified() {
        // given
        CreateCommentRequest commentRequest = new CreateCommentRequest("atbc@gmail.com", "Hello, how are you?");
        Comment commentAboutBilling = new Comment();
        commentAboutBilling.setSenderEmail(commentRequest.senderEmail());
        commentAboutBilling.setContent(commentRequest.content());

        AiTicketResponse aiResponse = new AiTicketResponse(
                false,
                null,
               null,
               null,
               null

        );
        given(commentRepository.save(any())).willReturn(commentAboutBilling);
        given(huggingFaceService.analyzeMessage(commentAboutBilling.getContent())).willReturn(aiResponse);
        // when
        CommentResponse actual = underTest.save(commentRequest);
        // then
        assertThat(actual).isNotNull();
        verify(commentRepository).save(commentAboutBilling);
        verify(huggingFaceService).analyzeMessage(commentAboutBilling.getContent());
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    void shouldGetComments() {
        // given
        List<Comment> expected = List.of(
                new Comment("atbc@gmail.com", "I paid my subscription twice a month"),
                new Comment("bdc@gmail.com", "I have difficulty with login")
        );
        given(commentRepository.findAll()).willReturn(expected);
        // when
        List<CommentResponse> actual = underTest.getAll();

        // then
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.getLast().senderEmail()).isEqualTo(expected.getLast().getSenderEmail());
        assertThat(actual.getLast().content()).isEqualTo(expected.getLast().getContent());
    }
}