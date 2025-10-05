package com.miniprojet.users.util;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EmailService implements EmailSender {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String emailContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(emailContent, true);

            helper.setTo(to);
            helper.setFrom("sarramk46@gmail.com"); 
            helper.setSubject("Confirm your email");

            mailSender.send(mimeMessage);

            System.out.println("Email envoyé avec succès à " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new IllegalStateException("Échec de l'envoi de l'email : " + e.getMessage());
        }
    }
}
