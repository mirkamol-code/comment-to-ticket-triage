package com.mirkamolcode.entity;

import com.mirkamolcode.entity.enums.Category;
import com.mirkamolcode.entity.enums.Priority;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @OneToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @CreationTimestamp
    private LocalDateTime localDateTime;

    public Ticket() {
    }

    public Ticket(UUID id, String title, Category category, Priority priority, Comment comment, LocalDateTime localDateTime) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.priority = priority;
        this.comment = comment;
        this.localDateTime = localDateTime;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && Objects.equals(title, ticket.title) && category == ticket.category && priority == ticket.priority && Objects.equals(comment, ticket.comment) && Objects.equals(localDateTime, ticket.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, category, priority, comment, localDateTime);
    }
}
