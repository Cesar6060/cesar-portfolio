package com.cesarvillarreal.portfolio.repository;

import com.cesarvillarreal.portfolio.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProjectRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProjectRepository projectRepository;

    private Project project1;
    private Project project2;
    private Project project3;

    @BeforeEach
    void setUp() {
        // Clear any existing data
        projectRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        // Create test projects
        project1 = new Project();
        project1.setTitle("First Project");
        project1.setSlug("first-project");
        project1.setShortDescription("First project description");
        project1.setFullDescription("Full description of first project");
        project1.setGithubUrl("https://github.com/test/first");
        project1.setFeatured(true);
        project1.setDisplayOrder(1);

        List<String> techStack1 = new ArrayList<>();
        techStack1.add("Java");
        techStack1.add("Spring Boot");
        project1.setTechStack(techStack1);

        project2 = new Project();
        project2.setTitle("Second Project");
        project2.setSlug("second-project");
        project2.setShortDescription("Second project description");
        project2.setFeatured(false);
        project2.setDisplayOrder(2);

        List<String> techStack2 = new ArrayList<>();
        techStack2.add("Python");
        techStack2.add("Django");
        project2.setTechStack(techStack2);

        project3 = new Project();
        project3.setTitle("Third Project");
        project3.setSlug("third-project");
        project3.setShortDescription("Third project description");
        project3.setFeatured(true);
        project3.setDisplayOrder(3);

        List<String> techStack3 = new ArrayList<>();
        techStack3.add("JavaScript");
        techStack3.add("React");
        project3.setTechStack(techStack3);
    }

    @Test
    void saveProject_ShouldPersistProject() {
        // Act
        Project savedProject = projectRepository.save(project1);

        // Assert
        assertThat(savedProject).isNotNull();
        assertThat(savedProject.getId()).isNotNull();
        assertThat(savedProject.getTitle()).isEqualTo("First Project");
        assertThat(savedProject.getSlug()).isEqualTo("first-project");
        assertThat(savedProject.getShortDescription()).isEqualTo("First project description");
        assertThat(savedProject.getFeatured()).isTrue();
        assertThat(savedProject.getDisplayOrder()).isEqualTo(1);
        assertThat(savedProject.getTechStack()).hasSize(2);
        assertThat(savedProject.getTechStack()).contains("Java", "Spring Boot");
        assertThat(savedProject.getCreatedAt()).isNotNull();
        assertThat(savedProject.getUpdatedAt()).isNotNull();
    }

    @Test
    void findById_WithValidId_ShouldReturnProject() {
        // Arrange
        Project savedProject = entityManager.persistAndFlush(project1);

        // Act
        Optional<Project> found = projectRepository.findById(savedProject.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("First Project");
        assertThat(found.get().getSlug()).isEqualTo("first-project");
    }

    @Test
    void findById_WithInvalidId_ShouldReturnEmpty() {
        // Act
        Optional<Project> found = projectRepository.findById(999L);

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void findBySlug_WithValidSlug_ShouldReturnProject() {
        // Arrange
        entityManager.persistAndFlush(project1);

        // Act
        Optional<Project> found = projectRepository.findBySlug("first-project");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("First Project");
        assertThat(found.get().getSlug()).isEqualTo("first-project");
        assertThat(found.get().getTechStack()).hasSize(2);
    }

    @Test
    void findBySlug_WithInvalidSlug_ShouldReturnEmpty() {
        // Arrange
        entityManager.persistAndFlush(project1);

        // Act
        Optional<Project> found = projectRepository.findBySlug("nonexistent-slug");

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void findByFeaturedTrueOrderByDisplayOrderAsc_ShouldReturnOnlyFeaturedProjects() {
        // Arrange
        entityManager.persist(project1); // featured, displayOrder 1
        entityManager.persist(project2); // not featured, displayOrder 2
        entityManager.persist(project3); // featured, displayOrder 3
        entityManager.flush();

        // Act
        List<Project> featuredProjects = projectRepository.findByFeaturedTrueOrderByDisplayOrderAsc();

        // Assert
        assertThat(featuredProjects).hasSize(2);
        assertThat(featuredProjects.get(0).getTitle()).isEqualTo("First Project");
        assertThat(featuredProjects.get(1).getTitle()).isEqualTo("Third Project");
        assertThat(featuredProjects.get(0).getDisplayOrder()).isLessThan(featuredProjects.get(1).getDisplayOrder());
    }

    @Test
    void findByFeaturedTrueOrderByDisplayOrderAsc_WhenNoFeaturedProjects_ShouldReturnEmptyList() {
        // Arrange
        entityManager.persist(project2); // not featured
        entityManager.flush();

        // Act
        List<Project> featuredProjects = projectRepository.findByFeaturedTrueOrderByDisplayOrderAsc();

        // Assert
        assertThat(featuredProjects).isEmpty();
    }

    @Test
    void findAllByOrderByDisplayOrderAsc_ShouldReturnAllProjectsOrdered() {
        // Arrange
        entityManager.persist(project3); // displayOrder 3
        entityManager.persist(project1); // displayOrder 1
        entityManager.persist(project2); // displayOrder 2
        entityManager.flush();

        // Act
        List<Project> allProjects = projectRepository.findAllByOrderByDisplayOrderAsc();

        // Assert
        assertThat(allProjects).hasSize(3);
        assertThat(allProjects.get(0).getTitle()).isEqualTo("First Project");
        assertThat(allProjects.get(1).getTitle()).isEqualTo("Second Project");
        assertThat(allProjects.get(2).getTitle()).isEqualTo("Third Project");
        assertThat(allProjects.get(0).getDisplayOrder()).isEqualTo(1);
        assertThat(allProjects.get(1).getDisplayOrder()).isEqualTo(2);
        assertThat(allProjects.get(2).getDisplayOrder()).isEqualTo(3);
    }

    @Test
    void existsBySlug_WithExistingSlug_ShouldReturnTrue() {
        // Arrange
        entityManager.persistAndFlush(project1);

        // Act
        boolean exists = projectRepository.existsBySlug("first-project");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void existsBySlug_WithNonExistingSlug_ShouldReturnFalse() {
        // Arrange
        entityManager.persistAndFlush(project1);

        // Act
        boolean exists = projectRepository.existsBySlug("nonexistent-slug");

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    void deleteProject_ShouldRemoveFromDatabase() {
        // Arrange
        Project savedProject = entityManager.persistAndFlush(project1);
        Long projectId = savedProject.getId();

        // Act
        projectRepository.deleteById(projectId);
        entityManager.flush();

        // Assert
        Optional<Project> found = projectRepository.findById(projectId);
        assertThat(found).isEmpty();
    }

    @Test
    void updateProject_ShouldModifyExistingProject() {
        // Arrange
        Project savedProject = entityManager.persistAndFlush(project1);
        entityManager.clear(); // Clear persistence context to force fresh fetch

        // Act
        savedProject.setTitle("Updated Title");
        savedProject.setShortDescription("Updated description");
        Project updatedProject = projectRepository.save(savedProject);
        entityManager.flush();

        // Assert
        Project found = projectRepository.findById(updatedProject.getId()).get();
        assertThat(found.getTitle()).isEqualTo("Updated Title");
        assertThat(found.getShortDescription()).isEqualTo("Updated description");
        assertThat(found.getUpdatedAt()).isAfter(found.getCreatedAt());
    }

    @Test
    void findAll_ShouldReturnAllProjects() {
        // Arrange
        entityManager.persist(project1);
        entityManager.persist(project2);
        entityManager.persist(project3);
        entityManager.flush();

        // Act
        List<Project> allProjects = projectRepository.findAll();

        // Assert
        assertThat(allProjects).hasSize(3);
    }

    @Test
    void techStack_ShouldBePersisted() {
        // Arrange
        Project savedProject = projectRepository.save(project1);
        entityManager.flush();
        entityManager.clear();

        // Act
        Project found = projectRepository.findById(savedProject.getId()).get();

        // Assert
        assertThat(found.getTechStack()).isNotNull();
        assertThat(found.getTechStack()).hasSize(2);
        assertThat(found.getTechStack()).contains("Java", "Spring Boot");
    }
}
