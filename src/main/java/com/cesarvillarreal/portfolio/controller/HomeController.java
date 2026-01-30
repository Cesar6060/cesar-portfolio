package com.cesarvillarreal.portfolio.controller;

import com.cesarvillarreal.portfolio.model.ContactMessage;
import com.cesarvillarreal.portfolio.model.Project;
import com.cesarvillarreal.portfolio.service.ContactService;
import com.cesarvillarreal.portfolio.service.ProjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    private final ProjectService projectService;
    private final ContactService contactService;

    public HomeController(ProjectService projectService, ContactService contactService) {
        this.projectService = projectService;
        this.contactService = contactService;
    }

    /**
     * Home page - Hero section with featured projects
     */
    @GetMapping("/")
    public String home(Model model) {
        log.debug("Rendering home page");
        List<Project> featuredProjects = projectService.getFeaturedProjects();
        model.addAttribute("featuredProjects", featuredProjects);
        return "index";
    }

    /**
     * About page - Bio, skills, experience
     */
    @GetMapping("/about")
    public String about(Model model) {
        log.debug("Rendering about page");
        return "about";
    }

    /**
     * Projects listing page - All projects grid
     */
    @GetMapping("/projects")
    public String projects(Model model) {
        log.debug("Rendering projects listing page");
        List<Project> allProjects = projectService.getAllProjects();
        model.addAttribute("projects", allProjects);
        return "projects";
    }

    /**
     * Single project detail page
     */
    @GetMapping("/projects/{slug}")
    public String projectDetail(@PathVariable String slug, Model model) {
        log.debug("Rendering project detail page for slug: {}", slug);
        Optional<Project> project = projectService.getProjectBySlug(slug);

        if (project.isEmpty()) {
            log.warn("Project not found with slug: {}", slug);
            return "redirect:/projects";
        }

        model.addAttribute("project", project.get());
        return "project-detail";
    }

    /**
     * Contact form page (GET)
     */
    @GetMapping("/contact")
    public String contactForm(Model model) {
        log.debug("Rendering contact form");
        model.addAttribute("contactMessage", new ContactMessage());
        return "contact";
    }

    /**
     * Contact form submission (POST)
     */
    @PostMapping("/contact")
    public String submitContact(
            @Valid @ModelAttribute("contactMessage") ContactMessage contactMessage,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        log.debug("Processing contact form submission from: {}", contactMessage.getEmail());

        if (bindingResult.hasErrors()) {
            log.warn("Contact form has validation errors");
            return "contact";
        }

        try {
            contactService.saveMessage(contactMessage);
            log.info("Contact message saved successfully from: {}", contactMessage.getEmail());
            redirectAttributes.addFlashAttribute("success", "Thank you for your message! I'll get back to you soon.");
            return "redirect:/contact";
        } catch (Exception e) {
            log.error("Error saving contact message", e);
            redirectAttributes.addFlashAttribute("error", "Sorry, there was an error sending your message. Please try again later.");
            return "redirect:/contact";
        }
    }

    /**
     * How I Built This page - Architecture showcase
     */
    @GetMapping("/how-i-built-this")
    public String howIBuiltThis(Model model) {
        log.debug("Rendering how I built this page");
        return "how-i-built-this";
    }
}
