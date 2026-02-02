-- V3: Update GitHub URLs to correct repository names

UPDATE projects
SET github_url = 'https://github.com/Cesar6060/dev-learning-platform'
WHERE slug = 'gamedev-learning-platform';

UPDATE projects
SET github_url = 'https://github.com/Cesar6060/PawsitiveHaven'
WHERE slug = 'ai-pet-management';

UPDATE projects
SET github_url = 'https://github.com/Cesar6060/cesar-portfolio'
WHERE slug = 'portfolio-website';
