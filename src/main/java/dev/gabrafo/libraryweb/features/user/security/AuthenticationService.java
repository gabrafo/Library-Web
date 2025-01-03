package dev.gabrafo.libraryweb.features.user.security;

import dev.gabrafo.libraryweb.errors.exceptions.ExceededAttemptsException;
import dev.gabrafo.libraryweb.features.email.Email;
import dev.gabrafo.libraryweb.features.email.EmailService;
import dev.gabrafo.libraryweb.features.user.User;
import dev.gabrafo.libraryweb.features.user.security.otp.Otp;
import dev.gabrafo.libraryweb.features.user.security.otp.OtpRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private final OtpRepository otpRepository;
    private final EmailService emailService;

    public AuthenticationService(OtpRepository otpRepository, EmailService emailService) {
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }

    public void verifyOtp(User user, int otpCode) {
        Otp otp = otpRepository.findByUserAndOtpCode(user, otpCode)
                .orElseThrow(() -> new RuntimeException("Código OTP inválido"));

        if (otp.getAttempts() >= 3) {
            throw new ExceededAttemptsException("Número máximo de tentativas excedido.");
        }

        if (otp.getExpiresAt().isBefore(Instant.now())) {
            throw new CredentialsExpiredException("O OTP expirou.");
        }

        otp.setAttempts(otp.getAttempts() + 1);
        otpRepository.save(otp);
    }

    public void confirmationEmail(User user, String subject, String filepath){
        String htmlContent = loadHtmlTemplate(filepath);

        int otpCode = otpGenerator();

        Instant expirationTime = Instant.now().plus(10, ChronoUnit.MINUTES);

        Otp otp = new Otp();
        otp.setUser(user);
        otp.setOtpCode(otpCode);
        otp.setCreatedAt(Instant.now());
        otp.setExpiresAt(expirationTime);
        otp.setAttempts(0);

        otpRepository.save(otp);

        htmlContent = htmlContent.replace("{{NAME}}", user.getName());
        htmlContent = htmlContent.replace("{{OTP_CODE}}", String.valueOf(otpCode));

        Email verificationEmail = Email.builder()
                .to(user.getEmail())
                .body(htmlContent)
                .subject(subject)
                .build();

        emailService.sendEmail(verificationEmail);
    }

    private String loadHtmlTemplate(String filePath) {
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar o template HTML", e);
        }
    }

    private int otpGenerator(){
        SecureRandom random = new SecureRandom();
        return random.nextInt(100_000, 999_999);
    }
}