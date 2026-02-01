-- Database initialization script
-- This runs automatically when PostgreSQL container starts

-- Projects table (matches Spring Data JPA schema)
CREATE TABLE IF NOT EXISTS projects (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    short_description TEXT,
    full_description TEXT,
    github_url VARCHAR(500),
    live_url VARCHAR(500),
    image_url VARCHAR(500),
    featured BOOLEAN DEFAULT false NOT NULL,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Project tech stack table (for @ElementCollection)
CREATE TABLE IF NOT EXISTS project_tech_stack (
    project_id BIGINT NOT NULL,
    technology VARCHAR(100),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Contact messages table
CREATE TABLE IF NOT EXISTS contact_messages (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    read BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Clear existing data
TRUNCATE TABLE project_tech_stack CASCADE;
TRUNCATE TABLE projects CASCADE;
TRUNCATE TABLE contact_messages CASCADE;

-- Seed Project 1: GameDev Learning Platform
INSERT INTO projects (title, slug, short_description, full_description, github_url, live_url, image_url, featured, display_order) VALUES
(
    'GameDev Learning Platform',
    'gamedev-learning-platform',
    'Full-stack educational platform serving 200+ students with video courses, assignments, and real-time features',
    'A comprehensive learning management system built for video game development courses at Prosper ISD. Features include video content with progress tracking, markdown-based lessons, assignment submission and grading workflows, real-time notifications via WebSockets, a code playground with Monaco Editor for hands-on coding, and discussion forums. Implements role-based access for students and instructors with secure enrollment codes. Backend achieves 81% test coverage.',
    'https://github.com/Cesar6060/gamedev-learning-platform',
    NULL,
    '/images/gamedev-platform2.png',
    true,
    1
);

-- Tech stack for GameDev Learning Platform
INSERT INTO project_tech_stack (project_id, technology) VALUES
(1, 'Django'),
(1, 'React'),
(1, 'TypeScript'),
(1, 'PostgreSQL'),
(1, 'Redis'),
(1, 'Docker'),
(1, 'WebSockets');

-- Seed Project 2: Portfolio Website
INSERT INTO projects (title, slug, short_description, full_description, github_url, live_url, image_url, featured, display_order) VALUES
(
    'Portfolio Website',
    'portfolio-website',
    'The site you''re looking at - Spring Boot + Thymeleaf + AWS',
    'Full-stack portfolio built with Spring Boot 3.2 and Java 21, demonstrating enterprise development practices. Features server-side rendering with Thymeleaf, PostgreSQL database, REST API endpoints, comprehensive test coverage with JUnit 5, and CI/CD pipeline with GitHub Actions. Deployed on AWS App Runner with RDS for production database.',
    'https://github.com/Cesar6060/portfolio',
    'https://cesarvillarreal.dev',
    '/images/portfolio-screenshot.png',
    true,
    2
);

-- Tech stack for Portfolio Website
INSERT INTO project_tech_stack (project_id, technology) VALUES
(2, 'Spring Boot'),
(2, 'Thymeleaf'),
(2, 'PostgreSQL'),
(2, 'AWS App Runner'),
(2, 'GitHub Actions');

-- Seed Project 3: AI Pet Management Platform
INSERT INTO projects (title, slug, short_description, full_description, github_url, live_url, image_url, featured, display_order) VALUES
(
    'AI Pet Management Platform',
    'ai-pet-management',
    'Full-stack pet management app with AI-powered chatbot, bio generator, and appointment scheduling',
    'A comprehensive pet management application built with Blazor WebAssembly and ASP.NET Core 8.0. Features include an AI chatbot for pet-related questions powered by OpenAI, an AI pet bio generator, appointment scheduling with calendar integration, and separate user/admin portals. The backend uses Entity Framework Core with SQLite and follows RESTful API architecture. Implements JWT authentication and role-based authorization.',
    'https://github.com/Cesar6060/AA-CHATBOT',
    NULL,
    '/images/chatbot-screenshot.png',
    true,
    3
);

-- Tech stack for AI Pet Management Platform
INSERT INTO project_tech_stack (project_id, technology) VALUES
(3, 'Blazor'),
(3, 'ASP.NET Core'),
(3, 'Entity Framework Core'),
(3, 'SQLite'),
(3, 'OpenAI API'),
(3, 'Bootstrap');

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_projects_slug ON projects(slug);
CREATE INDEX IF NOT EXISTS idx_projects_featured ON projects(featured);
CREATE INDEX IF NOT EXISTS idx_contact_messages_read ON contact_messages(read);
