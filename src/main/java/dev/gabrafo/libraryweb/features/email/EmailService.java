package dev.gabrafo.libraryweb.features.email;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(@NotEmpty Email email){
        var message = new SimpleMailMessage();
        message.setTo(email.to());
        message.setFrom("librarynoreplay@email.com");
        message.setSubject(email.subject());
        message.setText(email.body());
        mailSender.send(message);
    }
}
