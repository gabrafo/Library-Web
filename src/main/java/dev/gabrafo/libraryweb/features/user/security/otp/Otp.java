package dev.gabrafo.libraryweb.features.user.security.otp;

import dev.gabrafo.libraryweb.features.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;  // Relacionamento com User

    private int otpCode;
    private Instant createdAt;
    private Instant expiresAt;
    private int attempts;

    private boolean hasExceededMaxAttempts(){
        return attempts > 3;
    }

    public Otp(User user, int otpCode, Instant createdAt, Instant expiresAt, int attempts){
        this.user = user;
        this.otpCode = otpCode;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.attempts = attempts;
    }

    public Otp() {
    }
}
