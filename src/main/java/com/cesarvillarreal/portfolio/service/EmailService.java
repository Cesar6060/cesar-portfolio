package com.cesarvillarreal.portfolio.service;

import com.cesarvillarreal.portfolio.model.ContactMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String notificationRecipient;
    private final boolean emailEnabled;

    public EmailService(ObjectProvider<JavaMailSender> mailSenderProvider,
                        @Value("${app.email.notification-recipient:#{null}}") String notificationRecipient,
                        @Value("${app.email.enabled:false}") boolean emailEnabled) {
        // JavaMailSender only exists as a bean when spring.mail.host is configured
        // (prod profile) — dev and test profiles have no mail config and must still boot
        this.mailSender = mailSenderProvider.getIfAvailable();
        this.notificationRecipient = notificationRecipient;
        this.emailEnabled = emailEnabled;
    }

    public void sendContactNotification(ContactMessage message) {
        if (!emailEnabled || notificationRecipient == null) {
            log.info("Email notifications disabled or recipient not configured");
            return;
        }

        if (mailSender == null) {
            log.warn("Email notifications enabled but no mail server configured (spring.mail.host not set)");
            return;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(notificationRecipient);
            helper.setTo(notificationRecipient);
            helper.setSubject("New Contact Form Submission - " + message.getName());
            helper.setText(buildEmailBody(message), true);

            mailSender.send(mimeMessage);
            log.info("Contact notification email sent successfully to {}", notificationRecipient);

        } catch (MailException | MessagingException e) {
            // A mail failure must never break contact-form submission
            log.error("Failed to send contact notification email: {}", e.getMessage());
        }
    }

    private String buildEmailBody(ContactMessage message) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: #2563eb; color: white; padding: 20px; border-radius: 8px 8px 0 0; }
                    .content { background: #f9fafb; padding: 20px; border: 1px solid #e5e7eb; }
                    .field { margin-bottom: 15px; }
                    .label { font-weight: bold; color: #374151; }
                    .value { margin-top: 5px; padding: 10px; background: white; border-radius: 4px; }
                    .message-box { white-space: pre-wrap; }
                    .footer { text-align: center; padding: 15px; color: #6b7280; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h2 style="margin: 0;">New Contact Form Submission</h2>
                    </div>
                    <div class="content">
                        <div class="field">
                            <div class="label">From:</div>
                            <div class="value">%s</div>
                        </div>
                        <div class="field">
                            <div class="label">Email:</div>
                            <div class="value"><a href="mailto:%s">%s</a></div>
                        </div>
                        <div class="field">
                            <div class="label">Message:</div>
                            <div class="value message-box">%s</div>
                        </div>
                    </div>
                    <div class="footer">
                        Sent from cesarvillarreal.dev contact form
                    </div>
                </div>
            </body>
            </html>
            """.formatted(
                escapeHtml(message.getName()),
                escapeHtml(message.getEmail()),
                escapeHtml(message.getEmail()),
                escapeHtml(message.getMessage())
            );
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
