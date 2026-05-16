package com.mirkamolcode.service;

import com.mirkamolcode.dto.AiTicketResponse;
import com.mirkamolcode.dto.CommentResponse;
import com.mirkamolcode.dto.CreateCommentRequest;
import com.mirkamolcode.entity.Comment;
import com.mirkamolcode.entity.Ticket;
import com.mirkamolcode.entity.enums.Category;
import com.mirkamolcode.repository.CommentRepository;
import com.mirkamolcode.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final HuggingFaceService huggingFaceService;


    public CommentService(CommentRepository commentRepository, TicketRepository ticketRepository, HuggingFaceService huggingFaceService) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
        this.huggingFaceService = huggingFaceService;
    }

    public CommentResponse save(CreateCommentRequest request) {

        Comment comment = new Comment();

        comment.setSenderEmail(request.senderEmail());
        comment.setContent(request.content());

        Comment savedComment =
                commentRepository.save(comment);

        AiTicketResponse aiResponse =
                huggingFaceService.analyzeMessage(
                        savedComment.getContent()
                );

        if (aiResponse.qualified()) {

            Ticket ticket = new Ticket();

            ticket.setTitle(aiResponse.title());

            ticket.setCategory(aiResponse.category());

            ticket.setPriority(aiResponse.priority());

            ticket.setSummary(aiResponse.summary());

            ticket.setComment(savedComment);


            ticketRepository.save(ticket);
        }

        return mapToCommentResponse(savedComment);
    }

    public List<CommentResponse> getAll() {

        return commentRepository.findAll()
                .stream()
                .map(this::mapToCommentResponse)
                .toList();
    }

    private CommentResponse mapToCommentResponse(
            Comment comment
    ) {

        return new CommentResponse(
                comment.getId(),
                comment.getSenderEmail(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
