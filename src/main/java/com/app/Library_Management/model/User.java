package com.app.Library_Management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import com.app.Library_Management.domain.AuthProvider;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String fullName;
    private String role;
    private String phoneNumber;
    private AuthProvider  authProvider = AuthProvider.LOCAL;
    private String googleId;
    private String profileImg;
    private LocalDateTime lastLogin;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp

    private LocalDateTime updatedAt;
    private String password;

}
