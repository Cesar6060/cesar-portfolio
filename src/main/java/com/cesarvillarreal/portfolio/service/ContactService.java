package com.cesarvillarreal.portfolio.service;

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
     * Save a new contact message and send email notification
     */
    @Transactional
    public ContactMessage saveMessage(ContactMessage message) {
        log.info("Saving contact message from: {} ({})", message.getName(), message.getEmail());
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
