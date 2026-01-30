package com.cesarvillarreal.portfolio.repository;

import com.cesarvillarreal.portfolio.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Find a project by its slug
     */
    Optional<Project> findBySlug(String slug);

    /**
     * Find all featured projects ordered by display order
     */
    List<Project> findByFeaturedTrueOrderByDisplayOrderAsc();

    /**
     * Find all projects ordered by display order
     */
    List<Project> findAllByOrderByDisplayOrderAsc();

    /**
     * Check if a slug already exists (useful for validation)
     */
    boolean existsBySlug(String slug);
}
