package com.cesarvillarreal.portfolio.controller;

import com.cesarvillarreal.portfolio.config.SecurityConfig;
import com.cesarvillarreal.portfolio.model.ContactForm;
import com.cesarvillarreal.portfolio.model.ContactMessage;
import com.cesarvillarreal.portfolio.model.Project;
import com.cesarvillarreal.portfolio.service.ContactService;
import com.cesarvillarreal.portfolio.service.EmailService;
import com.cesarvillarreal.portfolio.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @WebMvcTest does not pick up plain @Configuration classes, so SecurityConfig has to be
 * imported explicitly. Without it, Boot's default security auto-configuration applies instead
 * and every request in this class would come back 401.
 */
@WebMvcTest(HomeController.class)
@Import(SecurityConfig.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private ContactService contactService;

    @MockBean
    private EmailService emailService;

    private Project testProject;
    private List<Project> testProjects;

    @BeforeEach
    void setUp() {
        // Create test project
        testProject = new Project();
        testProject.setId(1L);
        testProject.setTitle("Test Project");
        testProject.setSlug("test-project");
        testProject.setShortDescription("A test project description");
        testProject.setFullDescription("Full description of the test project");
        testProject.setGithubUrl("https://github.com/test/project");
        testProject.setLiveUrl("https://test-project.com");
        testProject.setFeatured(true);
        testProject.setDisplayOrder(1);

        List<String> techStack = new ArrayList<>();
        techStack.add("Java");
        techStack.add("Spring Boot");
        testProject.setTechStack(techStack);

        // Create list of test projects
        testProjects = Arrays.asList(testProject);
    }

    @Test
    void home_ShouldReturnIndexViewWithFeaturedProjects() throws Exception {
        // Arrange
        when(projectService.getFeaturedProjects()).thenReturn(testProjects);

        // Act & Assert
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("featuredProjects"))
                .andExpect(model().attribute("featuredProjects", hasSize(1)))
                .andExpect(content().string(containsString("Test Project")));

        verify(projectService, times(1)).getFeaturedProjects();
    }

    @Test
    void about_ShouldReturnAboutView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"));
    }

    @Test
    void projects_ShouldReturnProjectsViewWithAllProjects() throws Exception {
        // Arrange
        when(projectService.getAllProjects()).thenReturn(testProjects);

        // Act & Assert
        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects"))
                .andExpect(model().attributeExists("projects"))
                .andExpect(model().attribute("projects", hasSize(1)));

        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    void projectDetail_WithValidSlug_ShouldReturnProjectDetailView() throws Exception {
        // Arrange
        when(projectService.getProjectBySlug("test-project")).thenReturn(Optional.of(testProject));

        // Act & Assert
        mockMvc.perform(get("/projects/test-project"))
                .andExpect(status().isOk())
                .andExpect(view().name("project-detail"))
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attribute("project", testProject))
                .andExpect(content().string(containsString("Test Project")));

        verify(projectService, times(1)).getProjectBySlug("test-project");
    }

    @Test
    void projectDetail_WithInvalidSlug_ShouldRedirectToProjects() throws Exception {
        // Arrange
        when(projectService.getProjectBySlug("invalid-slug")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/projects/invalid-slug"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));

        verify(projectService, times(1)).getProjectBySlug("invalid-slug");
    }

    @Test
    void contactForm_ShouldReturnContactView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("contactForm"));
    }

    @Test
    void submitContact_WithValidData_ShouldRedirectWithSuccessMessage() throws Exception {
        // Arrange
        ContactMessage savedMessage = new ContactMessage();
        savedMessage.setId(1L);
        savedMessage.setName("John Doe");
        savedMessage.setEmail("john@example.com");
        savedMessage.setMessage("Test message");

        when(contactService.saveMessage(any(ContactForm.class))).thenReturn(savedMessage);

        // Act & Assert
        mockMvc.perform(post("/contact")
                        .with(csrf())
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("message", "Test message"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact"))
                .andExpect(flash().attributeExists("success"))
                .andExpect(flash().attribute("success",
                        "Thank you for your message! I'll get back to you soon."));

        verify(contactService, times(1)).saveMessage(any(ContactForm.class));
    }

    @Test
    void submitContact_WithInvalidData_ShouldReturnContactViewWithErrors() throws Exception {
        // Act & Assert - Missing required fields
        mockMvc.perform(post("/contact")
                        .with(csrf())
                        .param("name", "")
                        .param("email", "invalid-email")
                        .param("message", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeHasFieldErrors("contactForm", "name"))
                .andExpect(model().attributeHasFieldErrors("contactForm", "email"))
                .andExpect(model().attributeHasFieldErrors("contactForm", "message"));

        verify(contactService, never()).saveMessage(any(ContactForm.class));
    }

    @Test
    void submitContact_WithShortMessage_ShouldReturnValidationError() throws Exception {
        // Act & Assert - Message too short (min 10 characters)
        mockMvc.perform(post("/contact")
                        .with(csrf())
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("message", "Short"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeHasFieldErrors("contactForm", "message"));

        verify(contactService, never()).saveMessage(any(ContactForm.class));
    }

    @Test
    void submitContact_WhenServiceThrowsException_ShouldRedirectWithErrorMessage() throws Exception {
        // Arrange
        when(contactService.saveMessage(any(ContactForm.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(post("/contact")
                        .with(csrf())
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("message", "Test message for exception handling"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact"))
                .andExpect(flash().attributeExists("error"))
                .andExpect(flash().attribute("error",
                        "Sorry, there was an error sending your message. Please try again later."));

        verify(contactService, times(1)).saveMessage(any(ContactForm.class));
    }

    @Test
    void howIBuiltThis_ShouldReturnHowIBuiltThisView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/how-i-built-this"))
                .andExpect(status().isOk())
                .andExpect(view().name("how-i-built-this"));
    }

    // ───────────────────────────────────────────────────────────────────────────
    // Phase 13 regression tests
    // ───────────────────────────────────────────────────────────────────────────

    /**
     * V2 / F2 — the regression test for this phase's most serious bug.
     * <p>
     * Before ContactForm existed, these two extra params bound onto the entity and the
     * subsequent save() became an UPDATE of message 1. Now they have nowhere to bind.
     */
    @Test
    void submitContact_WithIdAndReadParams_ShouldNotBindThem() throws Exception {
        when(contactService.saveMessage(any(ContactForm.class))).thenReturn(new ContactMessage());

        mockMvc.perform(post("/contact")
                        .with(csrf())
                        .param("name", "Mallory")
                        .param("email", "mallory@example.com")
                        .param("message", "Attempting to overwrite an existing message")
                        .param("id", "1")
                        .param("read", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact"));

        ArgumentCaptor<ContactForm> captor = ArgumentCaptor.forClass(ContactForm.class);
        verify(contactService).saveMessage(captor.capture());

        ContactForm submitted = captor.getValue();
        assertThat(submitted.name()).isEqualTo("Mallory");
        assertThat(submitted.email()).isEqualTo("mallory@example.com");

        // ContactForm exposes no id or read component at all — the extra params were
        // discarded by the binder rather than carried into the persistence layer.
        assertThat(ContactForm.class.getRecordComponents())
                .extracting(java.lang.reflect.RecordComponent::getName)
                .containsExactlyInAnyOrder("name", "email", "message", "website");
    }

    /** V3 / F5 — honeypot: looks successful, saves nothing. */
    @Test
    void submitContact_WithHoneypotFilled_ShouldSilentlyDiscard() throws Exception {
        mockMvc.perform(post("/contact")
                        .with(csrf())
                        .param("name", "Spam Bot")
                        .param("email", "bot@example.com")
                        .param("message", "Buy cheap widgets right now")
                        .param("website", "http://spam.example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact"))
                // Indistinguishable from a real success — the bot learns nothing.
                .andExpect(flash().attribute("success",
                        "Thank you for your message! I'll get back to you soon."));

        verify(contactService, never()).saveMessage(any(ContactForm.class));
    }

    /**
     * Regression for an adversarial-review finding: the honeypot originally used isBlank(),
     * so a bot padding every field with a space slipped straight through. A browser submits
     * "" for an untouched field, so whitespace can only have been put there deliberately.
     */
    @Test
    void submitContact_WithWhitespaceOnlyHoneypot_ShouldStillBeDiscarded() throws Exception {
        for (String filler : new String[]{" ", "\t", "\n", "   "}) {
            mockMvc.perform(post("/contact")
                            .with(csrf())
                            .param("name", "Spam Bot")
                            .param("email", "bot@example.com")
                            .param("message", "Buy cheap widgets right now")
                            .param("website", filler))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/contact"));
        }

        verify(contactService, never()).saveMessage(any(ContactForm.class));
    }

    /** The other side of it: an untouched honeypot must not block a real submission. */
    @Test
    void submitContact_WithEmptyHoneypot_ShouldSaveNormally() throws Exception {
        when(contactService.saveMessage(any(ContactForm.class))).thenReturn(new ContactMessage());

        mockMvc.perform(post("/contact")
                        .with(csrf())
                        .param("name", "Real Person")
                        .param("email", "real@example.com")
                        .param("message", "A genuine enquiry about your work.")
                        .param("website", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("success"));

        verify(contactService, times(1)).saveMessage(any(ContactForm.class));
    }

    /** V4 / F6 — 5001 characters against a TEXT column. */
    @Test
    void submitContact_WithOversizedMessage_ShouldReturnValidationError() throws Exception {
        String oversized = "x".repeat(5001);

        mockMvc.perform(post("/contact")
                        .with(csrf())
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("message", oversized))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeHasFieldErrors("contactForm", "message"));

        verify(contactService, never()).saveMessage(any(ContactForm.class));
    }

    /** V4 boundary — exactly 5000 characters is still accepted. */
    @Test
    void submitContact_WithMessageAtMaxLength_ShouldSucceed() throws Exception {
        when(contactService.saveMessage(any(ContactForm.class))).thenReturn(new ContactMessage());

        mockMvc.perform(post("/contact")
                        .with(csrf())
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("message", "x".repeat(5000)))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("success"));

        verify(contactService, times(1)).saveMessage(any(ContactForm.class));
    }

    /** V5 / F3 — no CSRF token means no submission. */
    @Test
    void submitContact_WithoutCsrfToken_ShouldBeForbidden() throws Exception {
        mockMvc.perform(post("/contact")
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("message", "This should never be saved"))
                .andExpect(status().isForbidden());

        verify(contactService, never()).saveMessage(any(ContactForm.class));
    }

    /**
     * B4 / F7, HTML branch. The /api/** branch is covered in ApiControllerTest; this is the
     * other half. submitContact catches its own exceptions, so the only way to reach the
     * ModelAndView branch is a service throwing from a plain GET route.
     */
    @Test
    void htmlRoute_WhenServiceThrows_ShouldRenderErrorViewWithoutLeakingDetail() throws Exception {
        when(projectService.getAllProjects())
                .thenThrow(new IllegalStateException("neon-db-prod unreachable at 10.0.0.4:5432"));

        String body = mockMvc.perform(get("/projects"))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(body)
                .doesNotContain("IllegalStateException")
                .doesNotContain("neon-db-prod")
                .doesNotContain("10.0.0.4")
                .doesNotContain("com.cesarvillarreal");
    }

    /** V6 / F4 — the security headers the live site had none of. */
    @Test
    void get_ShouldEmitSecurityHeaders() throws Exception {
        when(projectService.getFeaturedProjects()).thenReturn(testProjects);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("X-Frame-Options", "DENY"))
                .andExpect(header().string("Referrer-Policy", "strict-origin-when-cross-origin"))
                .andExpect(header().string("Permissions-Policy",
                        "geolocation=(), microphone=(), camera=()"))
                .andExpect(header().exists("Content-Security-Policy-Report-Only"))
                // Report-Only by design this phase — it must not be enforcing yet.
                .andExpect(header().doesNotExist("Content-Security-Policy"))
                .andExpect(header().string("Content-Security-Policy-Report-Only",
                        containsString("https://cdn.tailwindcss.com")));
    }

    /**
     * A3 — HSTS is correctly absent over plain HTTP. It appears in production only because
     * forward-headers-strategy=framework lets Spring see X-Forwarded-Proto from Cloudflare
     * and Render.
     */
    @Test
    void get_OverPlainHttp_ShouldNotEmitHsts() throws Exception {
        when(projectService.getFeaturedProjects()).thenReturn(testProjects);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("Strict-Transport-Security"));
    }

    /** E3 — Thymeleaf must actually be injecting the CSRF token into the form. */
    @Test
    void contactForm_ShouldRenderCsrfHiddenInput() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("name=\"_csrf\"")));
    }

    /** E2 — the honeypot is present, and hidden in a way a screen reader and keyboard skip. */
    @Test
    void contactForm_ShouldRenderHoneypotHiddenFromAssistiveTech() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id=\"website\"")))
                .andExpect(content().string(containsString("aria-hidden=\"true\"")))
                .andExpect(content().string(containsString("tabindex=\"-1\"")))
                // display:none is what bots look for; this must stay off-screen positioning.
                .andExpect(content().string(not(containsString("display:none"))));
    }
}
