package com.app.reddit.service;

import com.app.reddit.models.NotificationEmail;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.rmi.activation.ActivationException;

@Service
@AllArgsConstructor
public class MailService {

    JavaMailSender javaMailSender;
    MailBuilder mailBuilder;

    @Async
    void sendEmail (NotificationEmail notificationEmail) throws ActivationException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("activation@redditclone.com");
            messageHelper.setTo(notificationEmail.getRecepient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailBuilder.build(notificationEmail.getBody()));
        };
        try {
            javaMailSender.send(messagePreparator);
            System.out.println("Activation Email Sent");
        } catch (MailException e) {
            throw new ActivationException("Error sending activation email to " + notificationEmail.getRecepient());
        }
    }

}
