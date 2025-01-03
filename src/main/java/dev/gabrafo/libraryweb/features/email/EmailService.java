package dev.gabrafo.libraryweb.features.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(@NotEmpty Email email){
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email.to());
            helper.setSubject(email.subject());
            helper.setText(email.body(), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Email não enviado! ERRO: ", e);
        }
    }
}
