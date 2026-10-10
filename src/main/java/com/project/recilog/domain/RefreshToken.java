package com.project.recilog.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "refresh_token", nullable = false, unique = true, length = 500)
    private String refreshToken;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    public void updateToken(String token, LocalDateTime expiryDate) {
        this.refreshToken = token;
        this.expiryDate = expiryDate;
    }
}