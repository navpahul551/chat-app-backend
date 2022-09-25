package com.nav.ChatApi.services;

import com.nav.ChatApi.models.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMail(EmailDetails emailDetails) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(emailDetails.getRecipient());
        mailMessage.setText(emailDetails.getMsgBody());
        mailMessage.setSubject(emailDetails.getSubject());

        javaMailSender.send(mailMessage);
    }
}
