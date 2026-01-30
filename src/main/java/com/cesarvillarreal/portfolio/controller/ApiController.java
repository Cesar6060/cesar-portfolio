package com.cesarvillarreal.portfolio.controller;

import com.cesarvillarreal.portfolio.model.Project;
import com.cesarvillarreal.portfolio.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Transactional(readOnly = true)
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);
    private final ProjectService projectService;

    public ApiController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * GET /api/projects - List all projects as JSON
     */
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getAllProjects() {
        log.debug("API: Fetching all projects");
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    /**
     * GET /api/projects/{slug} - Get single project as JSON
     */
    @GetMapping("/projects/{slug}")
    public ResponseEntity<Project> getProjectBySlug(@PathVariable String slug) {
        log.debug("API: Fetching project with slug: {}", slug);
        Optional<Project> project = projectService.getProjectBySlug(slug);

        return project
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("API: Project not found with slug: {}", slug);
                    return ResponseEntity.notFound().build();
                });
    }
}
