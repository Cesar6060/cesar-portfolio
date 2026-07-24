package com.cesarvillarreal.portfolio.service;

import com.cesarvillarreal.portfolio.model.ContactForm;
import com.cesarvillarreal.portfolio.model.ContactMessage;
import com.cesarvillarreal.portfolio.repository.ContactMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactService.class);
    private final ContactMessageRepository contactMessageRepository;
    private final EmailService emailService;

    public ContactService(ContactMessageRepository contactMessageRepository, EmailService emailService) {
        this.contactMessageRepository = contactMessageRepository;
        this.emailService = emailService;
    }

    /**
     * Save a new contact message and send email notification.
     * <p>
     * Takes the {@link ContactForm} DTO rather than an entity and builds the entity here, so a
     * submitted {@code id} can never reach {@code save()} and turn an insert into an update.
     * Name and email are deliberately not logged — Render captures stdout, so logging them
     * would put visitor PII into the host's log retention.
     */
    @Transactional
    public ContactMessage saveMessage(ContactForm form) {
        log.info("Saving contact message");

        ContactMessage message = new ContactMessage();
        message.setName(form.name());
        message.setEmail(form.email());
        message.setMessage(form.message());

        ContactMessage saved = contactMessageRepository.save(message);

        // Send email notification asynchronously
        try {
            emailService.sendContactNotification(saved);
        } catch (Exception e) {
            log.error("Failed to send email notification, but message was saved", e);
        }

        return saved;
    }

    /**
     * Get all contact messages (newest first)
     */
    public List<ContactMessage> getAllMessages() {
        log.debug("Fetching all contact messages");
        return contactMessageRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Get unread messages only
     */
    public List<ContactMessage> getUnreadMessages() {
        log.debug("Fetching unread contact messages");
        return contactMessageRepository.findByReadFalseOrderByCreatedAtDesc();
    }

    /**
     * Get a single message by ID
     */
    public Optional<ContactMessage> getMessageById(Long id) {
        log.debug("Fetching contact message with id: {}", id);
        return contactMessageRepository.findById(id);
    }

    /**
     * Mark a message as read
     */
    @Transactional
    public void markAsRead(Long id) {
        log.info("Marking message {} as read", id);
        contactMessageRepository.findById(id).ifPresent(message -> {
            message.setRead(true);
            contactMessageRepository.save(message);
        });
    }

    /**
     * Count unread messages
     */
    public long countUnreadMessages() {
        return contactMessageRepository.countByReadFalse();
    }

    /**
     * Delete a message
     */
    @Transactional
    public void deleteMessage(Long id) {
        log.info("Deleting contact message with id: {}", id);
        contactMessageRepository.deleteById(id);
    }
}
