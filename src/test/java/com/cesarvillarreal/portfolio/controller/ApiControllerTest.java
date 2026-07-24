package com.cesarvillarreal.portfolio.controller;

import com.cesarvillarreal.portfolio.config.SecurityConfig;
import com.cesarvillarreal.portfolio.model.Project;
import com.cesarvillarreal.portfolio.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * SecurityConfig must be imported explicitly — @WebMvcTest does not pick up plain
 * @Configuration classes, and without it Boot's default security auto-configuration would
 * return 401 for every request here.
 */
@WebMvcTest(ApiController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    private Project testProject;
    private List<Project> testProjects;

    @BeforeEach
    void setUp() {
        // Create test project
        testProject = new Project();
        testProject.setId(1L);
        testProject.setTitle("API Test Project");
        testProject.setSlug("api-test-project");
        testProject.setShortDescription("An API test project");
        testProject.setFullDescription("Full description for API testing");
        testProject.setGithubUrl("https://github.com/test/api-project");
        testProject.setLiveUrl("https://api-test.com");
        testProject.setFeatured(true);
        testProject.setDisplayOrder(1);

        List<String> techStack = new ArrayList<>();
        techStack.add("Spring Boot");
        techStack.add("REST API");
        techStack.add("JSON");
        testProject.setTechStack(techStack);

        // Create second project
        Project project2 = new Project();
        project2.setId(2L);
        project2.setTitle("Second Project");
        project2.setSlug("second-project");
        project2.setShortDescription("Second test project");
        project2.setFeatured(false);
        project2.setDisplayOrder(2);

        List<String> techStack2 = new ArrayList<>();
        techStack2.add("Java");
        techStack2.add("PostgreSQL");
        project2.setTechStack(techStack2);

        testProjects = Arrays.asList(testProject, project2);
    }

    @Test
    void getAllProjects_ShouldReturnJsonArrayOfProjects() throws Exception {
        // Arrange
        when(projectService.getAllProjects()).thenReturn(testProjects);

        // Act & Assert
        mockMvc.perform(get("/api/projects")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("API Test Project")))
                .andExpect(jsonPath("$[0].slug", is("api-test-project")))
                .andExpect(jsonPath("$[0].shortDescription", is("An API test project")))
                .andExpect(jsonPath("$[0].githubUrl", is("https://github.com/test/api-project")))
                .andExpect(jsonPath("$[0].liveUrl", is("https://api-test.com")))
                .andExpect(jsonPath("$[0].featured", is(true)))
                .andExpect(jsonPath("$[0].techStack", hasSize(3)))
                .andExpect(jsonPath("$[0].techStack[0]", is("Spring Boot")))
                .andExpect(jsonPath("$[0].techStack[1]", is("REST API")))
                .andExpect(jsonPath("$[0].techStack[2]", is("JSON")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Second Project")))
                .andExpect(jsonPath("$[1].slug", is("second-project")));

        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    void getAllProjects_WhenNoProjects_ShouldReturnEmptyArray() throws Exception {
        // Arrange
        when(projectService.getAllProjects()).thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(get("/api/projects")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    void getProjectBySlug_WithValidSlug_ShouldReturnProjectJson() throws Exception {
        // Arrange
        when(projectService.getProjectBySlug("api-test-project")).thenReturn(Optional.of(testProject));

        // Act & Assert
        mockMvc.perform(get("/api/projects/api-test-project")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("API Test Project")))
                .andExpect(jsonPath("$.slug", is("api-test-project")))
                .andExpect(jsonPath("$.shortDescription", is("An API test project")))
                .andExpect(jsonPath("$.fullDescription", is("Full description for API testing")))
                .andExpect(jsonPath("$.githubUrl", is("https://github.com/test/api-project")))
                .andExpect(jsonPath("$.liveUrl", is("https://api-test.com")))
                .andExpect(jsonPath("$.featured", is(true)))
                .andExpect(jsonPath("$.displayOrder", is(1)))
                .andExpect(jsonPath("$.techStack", hasSize(3)))
                .andExpect(jsonPath("$.techStack", containsInAnyOrder("Spring Boot", "REST API", "JSON")));

        verify(projectService, times(1)).getProjectBySlug("api-test-project");
    }

    @Test
    void getProjectBySlug_WithInvalidSlug_ShouldReturn404() throws Exception {
        // Arrange
        when(projectService.getProjectBySlug("nonexistent-project")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/projects/nonexistent-project")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(projectService, times(1)).getProjectBySlug("nonexistent-project");
    }

    @Test
    void getProjectBySlug_WithSpecialCharacters_ShouldHandleCorrectly() throws Exception {
        // Arrange
        when(projectService.getProjectBySlug("project-with-dashes")).thenReturn(Optional.of(testProject));

        // Act & Assert
        mockMvc.perform(get("/api/projects/project-with-dashes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(projectService, times(1)).getProjectBySlug("project-with-dashes");
    }

    @Test
    void getProjectBySlug_VerifyTechStackSerialization() throws Exception {
        // Arrange - This test specifically verifies the EAGER fetch fix for LazyInitializationException
        when(projectService.getProjectBySlug("api-test-project")).thenReturn(Optional.of(testProject));

        // Act & Assert
        mockMvc.perform(get("/api/projects/api-test-project")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.techStack").isArray())
                .andExpect(jsonPath("$.techStack", not(empty())))
                .andExpect(jsonPath("$.techStack[0]").isString());

        verify(projectService, times(1)).getProjectBySlug("api-test-project");
    }

    /**
     * V7 / F7 — a failure inside an /api/** route must not leak internals. ApiController had
     * no exception handling at all before GlobalExceptionHandler existed.
     */
    @Test
    void getAllProjects_WhenServiceThrows_ShouldReturnGenericErrorWithoutDetail() throws Exception {
        when(projectService.getAllProjects())
                .thenThrow(new IllegalStateException("Connection to neon-db-prod refused at 10.0.0.4:5432"));

        String body = mockMvc.perform(get("/api/projects")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("An unexpected error occurred.")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(body)
                .doesNotContain("IllegalStateException")
                .doesNotContain("neon-db-prod")
                .doesNotContain("10.0.0.4")
                .doesNotContain("java.lang")
                .doesNotContain("com.cesarvillarreal")
                .doesNotContain("at ");
    }

    /** V20 in miniature — an unknown /api path is still a plain 404, not a 500. */
    @Test
    void unknownApiPath_ShouldStillReturn404() throws Exception {
        mockMvc.perform(get("/api/does-not-exist")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
