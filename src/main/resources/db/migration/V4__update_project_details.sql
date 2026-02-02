-- V4: Update project details with accurate descriptions from documentation

-- Update GameDev Learning Platform
UPDATE projects
SET
    short_description = 'A full-stack Learning Management System built for video game development education, featuring real-time WebSocket notifications, an immersive course player, and comprehensive grading tools.',
    full_description = 'A comprehensive Learning Management System built from the ground up to support video game development education at Prosper ISD. This full-stack application demonstrates modern web development practices with a focus on user experience, real-time interactivity, and scalable architecture.

For Students: Immersive learning mode with distraction-free video player, progress tracking that resumes exactly where you left off, lesson comprehension quizzes that gate progression, assignment submissions with file uploads, real-time notifications for grades and announcements, and a personal dashboard with upcoming deadlines and course progress.

For Instructors: Course builder with units, lessons, and slide-based content, YouTube/Vimeo video integration, quiz builder with configurable attempts and passing scores, gradebook with inline editing and weighted categories, student roster with activity tracking, announcement system with optional email notifications, and calendar with custom reminders.

Key Technical Achievements: Designed RESTful API with 40+ endpoints and comprehensive permission system, implemented WebSocket architecture for instant notifications without polling, built responsive UI with 65+ reusable components and dark mode support, created slide-based lesson system with keyboard navigation, and developed weighted gradebook with late penalty calculations.'
WHERE slug = 'gamedev-learning-platform';

-- Delete old tech stack for GameDev
DELETE FROM project_tech_stack WHERE project_id = (SELECT id FROM projects WHERE slug = 'gamedev-learning-platform');

-- Insert updated tech stack for GameDev
INSERT INTO project_tech_stack (project_id, technology)
SELECT id, tech FROM projects, unnest(ARRAY['React 18', 'TypeScript', 'Django', 'PostgreSQL', 'Redis', 'Docker', 'WebSockets', 'Tailwind CSS']) AS tech
WHERE slug = 'gamedev-learning-platform';

-- Update AI Pet Management Platform (Pawsitive Haven)
UPDATE projects
SET
    title = 'Pawsitive Haven',
    short_description = 'A comprehensive pet management system with AI-powered assistance, featuring GPT-4o chat for pet advice, bio generation, appointment scheduling, and admin tools with security hardening.',
    full_description = 'Pawsitive Haven is a full-stack pet management system with AI-powered assistance for pet owners. Built with a clean architecture featuring proper dependency injection, PostgreSQL from day one, and Docker-first development.

Features include: AI chatbot powered by OpenAI GPT-4o-mini for pet care advice with conversation history persistence, AI-generated pet bios, pet profile management with species-specific icons, appointment and task management with filters, admin dashboard for user and FAQ management, and a resources page with pet care guides.

Security features: ChatSecurityService with 30+ prompt injection detection patterns, RateLimitService with tiered limits (20/min, 100/hr, 500/day), automatic 24-hour bans after 5 violations, hardened system prompts with strict security boundaries, output filtering to prevent system prompt leakage, and input sanitization with Unicode normalization.

Architecture: Blazor Server frontend communicating with ASP.NET Core 9 Web API backend, PostgreSQL 17 database, JWT authentication with BCrypt password hashing, and fully containerized with Docker Compose for both development and production environments.'
WHERE slug = 'ai-pet-management';

-- Delete old tech stack for Pet Platform
DELETE FROM project_tech_stack WHERE project_id = (SELECT id FROM projects WHERE slug = 'ai-pet-management');

-- Insert updated tech stack for Pet Platform
INSERT INTO project_tech_stack (project_id, technology)
SELECT id, tech FROM projects, unnest(ARRAY['ASP.NET Core 9', 'Blazor Server', 'PostgreSQL', 'OpenAI GPT-4o', 'JWT Auth', 'Docker']) AS tech
WHERE slug = 'ai-pet-management';
