-- V5: Update project images

-- Update Pawsitive Haven image
UPDATE projects
SET image_url = '/images/pawsitive-haven-chatbot.png'
WHERE slug = 'ai-pet-management';

-- Update Portfolio image
UPDATE projects
SET image_url = '/images/Portfolio.png'
WHERE slug = 'portfolio-website';
