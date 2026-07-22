package com.cesarvillarreal.portfolio.service;

import com.cesarvillarreal.portfolio.model.ContactMessage;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    private static final String RECIPIENT = "cesarvillarreal11@gmail.com";

    @Mock
    private ObjectProvider<JavaMailSender> mailSenderProvider;

    @Mock
    private JavaMailSender mailSender;

    private EmailService newEmailService(boolean enabled, String recipient) {
        when(mailSenderProvider.getIfAvailable()).thenReturn(mailSender);
        return new EmailService(mailSenderProvider, recipient, enabled);
    }

    private ContactMessage sampleMessage() {
        return new ContactMessage(1L, "Jane Doe", "jane@example.com",
                "Hello, I'd like to talk about a <project>.", false, LocalDateTime.now());
    }

    @Test
    void doesNotSendWhenEmailDisabled() {
        EmailService emailService = newEmailService(false, RECIPIENT);

        emailService.sendContactNotification(sampleMessage());

        verifyNoInteractions(mailSender);
    }

    @Test
    void doesNotSendWhenRecipientNotConfigured() {
        EmailService emailService = newEmailService(true, null);

        emailService.sendContactNotification(sampleMessage());

        verifyNoInteractions(mailSender);
    }

    @Test
    void doesNotFailWhenMailSenderBeanIsAbsent() {
        when(mailSenderProvider.getIfAvailable()).thenReturn(null);
        EmailService emailService = new EmailService(mailSenderProvider, RECIPIENT, true);

        assertDoesNotThrow(() -> emailService.sendContactNotification(sampleMessage()));
    }

    @Test
    void sendsNotificationWhenEnabledAndConfigured() {
        EmailService emailService = newEmailService(true, RECIPIENT);
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        emailService.sendContactNotification(sampleMessage());

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void swallowsSendFailureSoContactFormSubmissionSucceeds() {
        EmailService emailService = newEmailService(true, RECIPIENT);
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        doThrow(new MailSendException("SMTP connection refused"))
                .when(mailSender).send(any(MimeMessage.class));

        assertDoesNotThrow(() -> emailService.sendContactNotification(sampleMessage()));
    }
}
