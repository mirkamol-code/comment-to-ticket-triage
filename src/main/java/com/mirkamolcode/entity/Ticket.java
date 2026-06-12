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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Column(length = 2000)
    private String summary;
    @OneToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Ticket() {
    }

    public Ticket(UUID id, String title, Category category, Priority priority, String summary, Comment comment, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.priority = priority;
        this.summary = summary;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Ticket(String title, Category category, Priority priority, String summary, Comment comment) {
        this.title = title;
        this.category = category;
        this.priority = priority;
        this.summary = summary;
        this.comment = comment;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime localDateTime) {
        this.createdAt = localDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && Objects.equals(title, ticket.title) && category == ticket.category && priority == ticket.priority && Objects.equals(comment, ticket.comment) && Objects.equals(createdAt, ticket.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, category, priority, comment, createdAt);
    }
}
