-- V12: Rebrand GameDev Learning Platform to STEM Quest
-- The app was rebuilt and rebranded; now live at https://stemquest.cesarvillarreal11.workers.dev

UPDATE projects
SET
    title = 'STEM Quest',
    slug = 'stem-quest',
    short_description = 'Production-deployed learning management system with mastery-based quizzes, gamified progress, and instructor analytics — solo-built and running entirely on free-tier cloud infrastructure.',
    full_description = 'STEM Quest is a full-stack learning management system for Computer Science education, built solo and running in production entirely on free-tier cloud infrastructure (Cloudflare Workers, Render, Neon).

Students learn in a distraction-free Learning Mode course player — paginated lessons with markdown content and embedded video, and progress tracking that resumes to the exact section and video position. Comprehension is enforced through mastery quizzes that re-queue missed questions until answered correctly, alongside auto-graded unit quizzes with configurable attempt limits. A gamification layer adds XP, levels, streaks, badges, and a customizable mascot.

Instructors get a full course builder (courses → units → lessons → sections), a gradebook matrix with inline grading and CSV export, a student roster with enrollment invitations, announcements with email notifications, and an analytics dashboard.

Under the hood: a Django 4.2 + Django REST Framework API with deny-by-default role-based permissions and ~376 backend tests, a React 18 + TypeScript + Vite frontend, PostgreSQL on Neon, media on Cloudflare R2, Sentry error tracking on both tiers, and a GitHub Actions CI pipeline (pytest, tsc, ESLint, production build gate) that auto-deploys to Render and Cloudflare Workers on every merge. The live site is a locked-down public demo with a one-click student account.',
    github_url = 'https://github.com/Cesar6060/LMS',
    live_url = 'https://stemquest.cesarvillarreal11.workers.dev',
    image_url = '/images/stem-quest.png',
    updated_at = CURRENT_TIMESTAMP
WHERE slug = 'gamedev-learning-platform';

-- Rebuild tech stack (insertion order is display order)
DELETE FROM project_tech_stack WHERE project_id = (SELECT id FROM projects WHERE slug = 'stem-quest');

INSERT INTO project_tech_stack (project_id, technology)
SELECT id, tech FROM projects, unnest(ARRAY[
    'React 18',
    'TypeScript',
    'Django',
    'PostgreSQL',
    'Vite',
    'Tailwind CSS',
    'Cloudflare Workers',
    'Render',
    'Neon',
    'Sentry',
    'GitHub Actions'
]) AS tech
WHERE slug = 'stem-quest';
