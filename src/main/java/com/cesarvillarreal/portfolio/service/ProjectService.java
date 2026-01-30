package com.cesarvillarreal.portfolio.service;

import com.cesarvillarreal.portfolio.model.Project;
import com.cesarvillarreal.portfolio.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Get all projects ordered by display order
     */
    public List<Project> getAllProjects() {
        log.debug("Fetching all projects");
        return projectRepository.findAllByOrderByDisplayOrderAsc();
    }

    /**
     * Get featured projects only
     */
    public List<Project> getFeaturedProjects() {
        log.debug("Fetching featured projects");
        return projectRepository.findByFeaturedTrueOrderByDisplayOrderAsc();
    }

    /**
     * Get a single project by slug
     */
    public Optional<Project> getProjectBySlug(String slug) {
        log.debug("Fetching project with slug: {}", slug);
        return projectRepository.findBySlug(slug);
    }

    /**
     * Get a single project by ID
     */
    public Optional<Project> getProjectById(Long id) {
        log.debug("Fetching project with id: {}", id);
        return projectRepository.findById(id);
    }

    /**
     * Save or update a project
     */
    @Transactional
    public Project saveProject(Project project) {
        log.info("Saving project: {}", project.getTitle());
        return projectRepository.save(project);
    }

    /**
     * Delete a project by ID
     */
    @Transactional
    public void deleteProject(Long id) {
        log.info("Deleting project with id: {}", id);
        projectRepository.deleteById(id);
    }

    /**
     * Check if a slug exists
     */
    public boolean slugExists(String slug) {
        return projectRepository.existsBySlug(slug);
    }
}
