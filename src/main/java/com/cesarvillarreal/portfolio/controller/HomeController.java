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

    private static final String BASE_URL = "https://cesarvillarreal.dev";
    private static final String DEFAULT_IMAGE = BASE_URL + "/images/og-default.png";

    /**
     * Home page - Hero section with featured projects
     */
    @GetMapping("/")
    public String home(Model model) {
        log.debug("Rendering home page");
        List<Project> featuredProjects = projectService.getFeaturedProjects();
        model.addAttribute("featuredProjects", featuredProjects);
        addSeoAttributes(model,
            "Cesar Villarreal - Cloud Engineer & Software Developer",
            "Portfolio of Cesar Villarreal - Cloud Engineer and Game Design, Computer Science & Robotics Teacher. Building enterprise-grade applications with Spring Boot, AWS, and modern technologies.",
            BASE_URL,
            DEFAULT_IMAGE,
            "website");
        return "index";
    }

    /**
     * About page - Bio, skills, experience
     */
    @GetMapping("/about")
    public String about(Model model) {
        log.debug("Rendering about page");
        addSeoAttributes(model,
            "About - Cesar Villarreal",
            "Learn about Cesar Villarreal - Cloud Engineer and game design, computer science, and robotics teacher with an M.S. in Software Engineering and experience building enterprise applications and cloud infrastructure.",
            BASE_URL + "/about",
            DEFAULT_IMAGE,
            "profile");
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
        addSeoAttributes(model,
            "Projects - Cesar Villarreal",
            "Browse my portfolio of projects showcasing full-stack development, cloud architecture, and system design with Spring Boot, AWS, React, and more.",
            BASE_URL + "/projects",
            DEFAULT_IMAGE,
            "website");
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

        Project p = project.get();
        model.addAttribute("project", p);
        String projectImage = p.getImageUrl() != null ? BASE_URL + p.getImageUrl() : DEFAULT_IMAGE;
        addSeoAttributes(model,
            p.getTitle() + " - Cesar Villarreal",
            p.getShortDescription() != null ? p.getShortDescription() : "View details about " + p.getTitle(),
            BASE_URL + "/projects/" + slug,
            projectImage,
            "article");
        return "project-detail";
    }

    /**
     * Contact form page (GET)
     */
    @GetMapping("/contact")
    public String contactForm(Model model) {
        log.debug("Rendering contact form");
        model.addAttribute("contactMessage", new ContactMessage());
        addSeoAttributes(model,
            "Contact - Cesar Villarreal",
            "Get in touch with Cesar Villarreal for cloud engineering or software development opportunities.",
            BASE_URL + "/contact",
            DEFAULT_IMAGE,
            "website");
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

        log.info("Contact form submission received - Name: {}, Email: {}",
                contactMessage.getName(), contactMessage.getEmail());

        if (bindingResult.hasErrors()) {
            log.warn("Contact form validation failed. Errors: {}", bindingResult.getAllErrors());
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
        addSeoAttributes(model,
            "How I Built This - Cesar Villarreal",
            "A deep dive into the architecture, tech stack, and engineering decisions behind cesarvillarreal.dev. Built with Spring Boot, AWS, and modern CI/CD practices.",
            BASE_URL + "/how-i-built-this",
            DEFAULT_IMAGE,
            "article");
        return "how-i-built-this";
    }

    /**
     * Helper method to add SEO attributes to the model
     */
    private void addSeoAttributes(Model model, String title, String description, String url, String image, String type) {
        model.addAttribute("pageTitle", title);
        model.addAttribute("pageDescription", description);
        model.addAttribute("pageUrl", url);
        model.addAttribute("pageImage", image);
        model.addAttribute("pageType", type);
    }
}
