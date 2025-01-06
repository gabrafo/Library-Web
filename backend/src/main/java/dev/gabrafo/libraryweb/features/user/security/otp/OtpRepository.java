package dev.gabrafo.libraryweb.features.user.security.otp;

import dev.gabrafo.libraryweb.features.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByUserAndOtpCode(User user, int otpCode);
}
