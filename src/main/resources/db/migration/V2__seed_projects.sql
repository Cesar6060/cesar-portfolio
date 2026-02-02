-- V2: Seed initial projects

-- Project 1: GameDev Learning Platform
INSERT INTO projects (title, slug, short_description, full_description, github_url, live_url, image_url, featured, display_order) VALUES
(
    'GameDev Learning Platform',
    'gamedev-learning-platform',
    'Full-stack educational platform serving 200+ students with video courses, assignments, and real-time features',
    'A comprehensive learning management system built for video game development courses at Prosper ISD. Features include video content with progress tracking, markdown-based lessons, assignment submission and grading workflows, real-time notifications via WebSockets, a code playground with Monaco Editor for hands-on coding, and discussion forums. Implements role-based access for students and instructors with secure enrollment codes. Backend achieves 81% test coverage.',
    'https://github.com/Cesar6060/dev-learning-platform',
    NULL,
    '/images/gamedev-platform2.png',
    true,
    1
);

INSERT INTO project_tech_stack (project_id, technology) VALUES
(1, 'Django'),
(1, 'React'),
(1, 'TypeScript'),
(1, 'PostgreSQL'),
(1, 'Redis'),
(1, 'Docker'),
(1, 'WebSockets');

-- Project 2: Portfolio Website
INSERT INTO projects (title, slug, short_description, full_description, github_url, live_url, image_url, featured, display_order) VALUES
(
    'Portfolio Website',
    'portfolio-website',
    'The site you''re looking at - Spring Boot + Thymeleaf + AWS',
    'Full-stack portfolio built with Spring Boot 3.2 and Java 21, demonstrating enterprise development practices. Features server-side rendering with Thymeleaf, PostgreSQL database, REST API endpoints, comprehensive test coverage with JUnit 5, and CI/CD pipeline with GitHub Actions. Deployed on AWS App Runner with RDS for production database.',
    'https://github.com/Cesar6060/cesar-portfolio',
    'https://cesarvillarreal.dev',
    '/images/portfolio-screenshot.png',
    true,
    2
);

INSERT INTO project_tech_stack (project_id, technology) VALUES
(2, 'Spring Boot'),
(2, 'Thymeleaf'),
(2, 'PostgreSQL'),
(2, 'AWS App Runner'),
(2, 'GitHub Actions');

-- Project 3: AI Pet Management Platform
INSERT INTO projects (title, slug, short_description, full_description, github_url, live_url, image_url, featured, display_order) VALUES
(
    'AI Pet Management Platform',
    'ai-pet-management',
    'Full-stack pet management app with AI-powered chatbot, bio generator, and appointment scheduling',
    'A comprehensive pet management application built with Blazor WebAssembly and ASP.NET Core 8.0. Features include an AI chatbot for pet-related questions powered by OpenAI, an AI pet bio generator, appointment scheduling with calendar integration, and separate user/admin portals. The backend uses Entity Framework Core with SQLite and follows RESTful API architecture. Implements JWT authentication and role-based authorization.',
    'https://github.com/Cesar6060/PawsitiveHaven',
    NULL,
    '/images/chatbot-screenshot.png',
    true,
    3
);

INSERT INTO project_tech_stack (project_id, technology) VALUES
(3, 'Blazor'),
(3, 'ASP.NET Core'),
(3, 'Entity Framework Core'),
(3, 'SQLite'),
(3, 'OpenAI API'),
(3, 'Bootstrap');
