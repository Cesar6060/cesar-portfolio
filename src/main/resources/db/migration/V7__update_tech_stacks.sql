-- V7: Update tech stacks - add more portfolio technologies, remove JWT from Pawsitive Haven

-- Remove JWT Auth from Pawsitive Haven
DELETE FROM project_tech_stack
WHERE project_id = (SELECT id FROM projects WHERE slug = 'ai-pet-management')
AND technology = 'JWT Auth';

-- Clear and rebuild Portfolio Website tech stack with complete list
DELETE FROM project_tech_stack
WHERE project_id = (SELECT id FROM projects WHERE slug = 'portfolio-website');

INSERT INTO project_tech_stack (project_id, technology)
SELECT id, tech FROM projects, unnest(ARRAY[
    'Spring Boot',
    'Java 21',
    'Thymeleaf',
    'PostgreSQL',
    'Flyway',
    'AWS App Runner',
    'AWS RDS',
    'AWS SES',
    'AWS ECR',
    'Docker',
    'GitHub Actions',
    'Cloudflare',
    'Tailwind CSS',
    'JUnit 5',
    'Claude Code',
    'Greptile'
]) AS tech
WHERE slug = 'portfolio-website';
