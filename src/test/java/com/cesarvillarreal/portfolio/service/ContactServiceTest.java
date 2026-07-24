package com.cesarvillarreal.portfolio.service;

import com.cesarvillarreal.portfolio.model.ContactForm;
import com.cesarvillarreal.portfolio.model.ContactMessage;
import com.cesarvillarreal.portfolio.repository.ContactMessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * The persistence half of the F2 mass-assignment fix: whatever arrives from the form, the
 * entity that reaches the repository must be a fresh insert.
 */
@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactMessageRepository contactMessageRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ContactService contactService;

    /**
     * V2 / F2 — assert on the entity actually handed to the repository. An entity carrying an
     * id would make save() an UPDATE rather than an INSERT.
     */
    @Test
    void saveMessage_ShouldPersistEntityWithNoIdAndUnread() {
        when(contactMessageRepository.save(any(ContactMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ContactForm form = new ContactForm(
                "Mallory", "mallory@example.com", "A perfectly ordinary message", null);

        contactService.saveMessage(form);

        ArgumentCaptor<ContactMessage> captor = ArgumentCaptor.forClass(ContactMessage.class);
        verify(contactMessageRepository).save(captor.capture());

        ContactMessage persisted = captor.getValue();
        assertThat(persisted.getId())
                .as("a submitted id must never reach the repository — save() would UPDATE")
                .isNull();
        assertThat(persisted.getRead())
                .as("new messages are always unread")
                .isFalse();
        assertThat(persisted.getName()).isEqualTo("Mallory");
        assertThat(persisted.getEmail()).isEqualTo("mallory@example.com");
        assertThat(persisted.getMessage()).isEqualTo("A perfectly ordinary message");
    }

    /** A failing mail provider must not lose the message or surface an error. */
    @Test
    void saveMessage_WhenEmailFails_ShouldStillReturnSavedMessage() {
        when(contactMessageRepository.save(any(ContactMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        doThrow(new RuntimeException("SMTP unavailable"))
                .when(emailService).sendContactNotification(any(ContactMessage.class));

        ContactForm form = new ContactForm(
                "Jane", "jane@example.com", "Message body long enough", null);

        ContactMessage saved = contactService.saveMessage(form);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Jane");
        verify(contactMessageRepository, times(1)).save(any(ContactMessage.class));
    }
}
