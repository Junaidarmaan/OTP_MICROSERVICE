package com.junnu.app.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    

    public void sendEmail(
            String to,
            String subject,
            String content) {

        try {

            MimeMessage mime =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(mime, true);

            helper.setFrom("junnubest@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);

            mailSender.send(mime);

        } catch (MessagingException ex) {

            throw new RuntimeException(
                    "Failed to send email",
                    ex
            );
        }
    }
}