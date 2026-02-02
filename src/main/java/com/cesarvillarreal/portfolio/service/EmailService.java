package com.cesarvillarreal.portfolio.service;

import com.cesarvillarreal.portfolio.model.ContactMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Value("${app.email.notification-recipient:#{null}}")
    private String notificationRecipient;

    @Value("${app.email.enabled:false}")
    private boolean emailEnabled;

    private final SesClient sesClient;

    public EmailService() {
        this.sesClient = SesClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    public void sendContactNotification(ContactMessage message) {
        if (!emailEnabled || notificationRecipient == null) {
            log.info("Email notifications disabled or recipient not configured");
            return;
        }

        try {
            String subject = "New Contact Form Submission - " + message.getName();
            String body = buildEmailBody(message);

            SendEmailRequest request = SendEmailRequest.builder()
                    .source(notificationRecipient)
                    .destination(Destination.builder()
                            .toAddresses(notificationRecipient)
                            .build())
                    .message(Message.builder()
                            .subject(Content.builder()
                                    .data(subject)
                                    .charset("UTF-8")
                                    .build())
                            .body(Body.builder()
                                    .html(Content.builder()
                                            .data(body)
                                            .charset("UTF-8")
                                            .build())
                                    .build())
                            .build())
                    .build();

            sesClient.sendEmail(request);
            log.info("Contact notification email sent successfully to {}", notificationRecipient);

        } catch (SesException e) {
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
