package com.xink.training.taskmanagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "task_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskComment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentType type = CommentType.USER_COMMENT;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private TaskComment parent;
    
    public static TaskComment systemComment(String content) {
        return TaskComment.builder()
            .content(content)
            .type(CommentType.SYSTEM)
            .build();
    }
    
    public static TaskComment statusChangeComment(String content, User user, String additionalComment) {
        String fullContent = additionalComment != null && !additionalComment.isEmpty() 
            ? content + " - " + additionalComment 
            : content;
        return TaskComment.builder()
            .content(fullContent)
            .type(CommentType.STATUS_CHANGE)
            .user(user)
            .build();
    }
    
    public static TaskComment priorityChangeComment(String content) {
        return TaskComment.builder()
            .content(content)
            .type(CommentType.PRIORITY_CHANGE)
            .build();
    }
}

