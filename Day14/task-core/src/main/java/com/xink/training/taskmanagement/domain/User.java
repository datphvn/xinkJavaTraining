package com.xink.training.taskmanagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String email;
    
    @Column(name = "display_name")
    private String displayName;
    
    @Column(name = "is_active")
    private boolean active = true;
    
    public String getDisplayName() {
        return displayName != null ? displayName : username;
    }
    
    public boolean isActive() {
        return active;
    }
}

