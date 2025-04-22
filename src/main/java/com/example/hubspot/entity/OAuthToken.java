package com.example.hubspot.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Entity
@Table(name = "oauth_tokens")
@Data
public class OAuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String accessToken;
    private String refreshToken;
    private Instant expiresAt;
}
