package com.cesarvillarreal.portfolio.controller;

import com.cesarvillarreal.portfolio.model.ContactMessage;
import com.cesarvillarreal.portfolio.model.Project;
import com.cesarvillarreal.portfolio.service.ContactService;
import com.cesarvillarreal.portfolio.service.EmailService;
import com.cesarvillarreal.portfolio.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
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
                .andExpect(model().attributeExists("contactMessage"));
    }

    @Test
    void submitContact_WithValidData_ShouldRedirectWithSuccessMessage() throws Exception {
        // Arrange
        ContactMessage savedMessage = new ContactMessage();
        savedMessage.setId(1L);
        savedMessage.setName("John Doe");
        savedMessage.setEmail("john@example.com");
        savedMessage.setMessage("Test message");

        when(contactService.saveMessage(any(ContactMessage.class))).thenReturn(savedMessage);

        // Act & Assert
        mockMvc.perform(post("/contact")
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("message", "Test message"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact"))
                .andExpect(flash().attributeExists("success"))
                .andExpect(flash().attribute("success",
                        "Thank you for your message! I'll get back to you soon."));

        verify(contactService, times(1)).saveMessage(any(ContactMessage.class));
    }

    @Test
    void submitContact_WithInvalidData_ShouldReturnContactViewWithErrors() throws Exception {
        // Act & Assert - Missing required fields
        mockMvc.perform(post("/contact")
                        .param("name", "")
                        .param("email", "invalid-email")
                        .param("message", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeHasFieldErrors("contactMessage", "name"))
                .andExpect(model().attributeHasFieldErrors("contactMessage", "email"))
                .andExpect(model().attributeHasFieldErrors("contactMessage", "message"));

        verify(contactService, never()).saveMessage(any(ContactMessage.class));
    }

    @Test
    void submitContact_WithShortMessage_ShouldReturnValidationError() throws Exception {
        // Act & Assert - Message too short (min 10 characters)
        mockMvc.perform(post("/contact")
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("message", "Short"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeHasFieldErrors("contactMessage", "message"));

        verify(contactService, never()).saveMessage(any(ContactMessage.class));
    }

    @Test
    void submitContact_WhenServiceThrowsException_ShouldRedirectWithErrorMessage() throws Exception {
        // Arrange
        when(contactService.saveMessage(any(ContactMessage.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(post("/contact")
                        .param("name", "John Doe")
                        .param("email", "john@example.com")
                        .param("message", "Test message for exception handling"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact"))
                .andExpect(flash().attributeExists("error"))
                .andExpect(flash().attribute("error",
                        "Sorry, there was an error sending your message. Please try again later."));

        verify(contactService, times(1)).saveMessage(any(ContactMessage.class));
    }

    @Test
    void howIBuiltThis_ShouldReturnHowIBuiltThisView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/how-i-built-this"))
                .andExpect(status().isOk())
                .andExpect(view().name("how-i-built-this"));
    }
}
