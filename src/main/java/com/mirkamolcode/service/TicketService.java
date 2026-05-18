package com.mirkamolcode.service;

import com.mirkamolcode.dto.TicketResponse;
import com.mirkamolcode.entity.Ticket;
import com.mirkamolcode.exception.ResourceNotFoundException;
import com.mirkamolcode.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<TicketResponse> getAll() {
        return ticketRepository.findAll()
                .stream()
                .map(this::mapToTicketResponse)
                .toList();
    }
    public TicketResponse getById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        return mapToTicketResponse(ticket);
    }
    private TicketResponse mapToTicketResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getCategory(),
                ticket.getPriority(),
                ticket.getSummary(),
                ticket.getLocalDateTime()
        );
    }


}
