-- V6: Add AI development tools to project tech stacks

-- Add Claude Code and Greptile to GameDev Learning Platform
INSERT INTO project_tech_stack (project_id, technology)
SELECT id, 'Claude Code' FROM projects WHERE slug = 'gamedev-learning-platform';

INSERT INTO project_tech_stack (project_id, technology)
SELECT id, 'Greptile' FROM projects WHERE slug = 'gamedev-learning-platform';

-- Add Greptile to Portfolio Website
INSERT INTO project_tech_stack (project_id, technology)
SELECT id, 'Greptile' FROM projects WHERE slug = 'portfolio-website';
