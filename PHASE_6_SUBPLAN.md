# Phase 6: Polish - Detailed Subplan

> **For Claude Code:** Follow this subplan to complete Phase 6 polish tasks.

---

## Overview

User has completed Phases 1-5. Now needs polish work in six areas:

1. **Color Palette** - Unify skill badges (keep blue primary)
2. **Privacy** - Remove phone number (spam concern)
3. **Projects** - Curate to 3 main projects
4. **Images** - Add headshot, real project screenshots
5. **Project Cards** - Show tech stack on cards
6. **Finishing Touches** - Favicon, error page styling

---

## Task 1: Color Palette - Unify Skill Badges

### Current Issue
- Primary blue is fine, keep it
- Skill badges on About page are rainbow/chaotic (green, blue, teal, orange, pink)
- Need a cohesive, unified look

### Decision: Option D - Keep Blue, Unify Badges

Keep the current sky/blue primary color. Only fix the skill badges.

### Skill Badges Fix (about.html)

**Current:** Rainbow chaos (each category different color)

**Fix:** Use ONE consistent style for ALL skill categories:

```html
<!-- Unified style: Gray background with subtle primary accent -->
<span class="px-3 py-1 bg-gray-100 text-gray-700 rounded-full text-sm border border-gray-200 hover:bg-gray-200 transition-colors">
  Java
</span>
```

Apply this same style to:
- Programming Languages (currently gray ✓)
- Frameworks (currently green - change to gray)
- Cloud, DevOps & Databases (currently teal - change to gray)
- Methods & Tools (currently orange/pink - change to gray)

### Files to Update
- `about.html` - Unify all skill badge colors to gray

---

## Task 2: Remove Phone Number

### Why
- Spam/robocall risk
- Email is sufficient for professional contact
- LinkedIn provides another channel

### Files to Update

**about.html**
- Remove phone number from "Get In Touch" sidebar
- Keep: Email, GitHub, LinkedIn

**contact.html**
- Remove phone from "Contact Information" section
- Remove "For urgent inquiries, please call or text" from Response Time card
- Update to: "I typically respond within 24-48 hours during business days."

**fragments/footer.html**
- Verify phone is not listed (should only have GitHub, LinkedIn, Email icons)

---

## Task 3: Curate Projects to 3

### Projects to REMOVE from database
- Cloud Infrastructure Automation
- VEX Robotics Curriculum

### Final 3 Projects

#### 1. GameDev Learning Platform (Featured)
```
title: GameDev Learning Platform
slug: gamedev-learning-platform
shortDescription: Full-stack educational platform serving 200+ students with video courses, assignments, and real-time features
fullDescription: A comprehensive learning management system built for video game development courses at Prosper ISD. Features include video content with progress tracking, markdown-based lessons, assignment submission and grading workflows, real-time notifications via WebSockets, a code playground with Monaco Editor for hands-on coding, and discussion forums. Implements role-based access for students and instructors with secure enrollment codes. Backend achieves 81% test coverage.
techStack: Django, React, TypeScript, PostgreSQL, Redis, Docker, WebSockets
githubUrl: https://github.com/Cesar6060/gamedev-learning-platform
liveUrl: null
imageUrl: /images/gamedev-screenshot.png
featured: true
displayOrder: 1
```

#### 2. Portfolio Website (Featured)
```
title: Portfolio Website
slug: portfolio-website
shortDescription: The site you're looking at - Spring Boot + Thymeleaf + AWS
fullDescription: Full-stack portfolio built with Spring Boot 3.2 and Java 21, demonstrating enterprise development practices. Features server-side rendering with Thymeleaf, PostgreSQL database, REST API endpoints, comprehensive test coverage with JUnit 5, and CI/CD pipeline with GitHub Actions. Deployed on AWS App Runner.
techStack: Spring Boot, Thymeleaf, PostgreSQL, AWS App Runner, GitHub Actions
githubUrl: https://github.com/Cesar6060/portfolio
liveUrl: https://cesarvillarreal.dev
imageUrl: /images/portfolio-screenshot.png
featured: true
displayOrder: 2
```

#### 3. AI Pet Management Platform (Featured)
```
title: AI Pet Management Platform
slug: ai-pet-management
shortDescription: Full-stack pet management app with AI-powered chatbot, bio generator, and appointment scheduling
fullDescription: A comprehensive pet management application built with Blazor WebAssembly and ASP.NET Core 8.0. Features include an AI chatbot for pet-related questions powered by OpenAI, an AI pet bio generator, appointment scheduling with calendar integration, and separate user/admin portals. The backend uses Entity Framework Core with SQLite and follows RESTful API architecture.
techStack: Blazor, ASP.NET Core, Entity Framework Core, SQLite, OpenAI API, Bootstrap
githubUrl: https://github.com/Cesar6060/AA-CHATBOT
liveUrl: null
imageUrl: /images/chatbot-screenshot.png
featured: true
displayOrder: 3
```

### Recommendation
Use Option A (update init.sql) for now. Reset database with:
```bash
docker-compose down -v
docker-compose up -d
```

---

## Task 4: Add Images

### Current Issues
- About page has no photo of user
- Project cards show letter placeholders (L, P, C) instead of screenshots
- Looks unfinished/template-like

### Images Needed (User Must Provide)

| Image | Location | Recommended Size |
|-------|----------|------------------|
| `headshot.jpg` | About page sidebar | 400x400px (square) |
| `lms-screenshot.png` | LMS project card + detail | 800x600px |
| `portfolio-screenshot.png` | Portfolio project card + detail | 800x600px |
| `chatbot-screenshot.png` | Chatbot project card + detail | 800x600px |

### File Locations
Place images in: `src/main/resources/static/images/`

### Files to Update

**about.html - Add headshot**
```html
<!-- In the sidebar, above "Get In Touch" -->
<div class="mb-6">
  <img src="/images/headshot.jpg" alt="Cesar Villarreal" 
       class="w-32 h-32 rounded-full mx-auto shadow-lg object-cover">
</div>
```

**fragments/project-card.html - Use real screenshots**
```html
<!-- Replace the gradient placeholder with actual image -->
<div th:if="${project.imageUrl}" class="h-48 overflow-hidden">
  <img th:src="${project.imageUrl}" th:alt="${project.title}" 
       class="w-full h-full object-cover">
</div>
<div th:unless="${project.imageUrl}" class="h-48 bg-gradient-to-br from-primary-400 to-primary-600 flex items-center justify-center">
  <!-- Keep letter fallback for projects without images -->
  <span class="text-6xl font-bold text-white/80" th:text="${project.title.substring(0,1)}"></span>
</div>
```

**project-detail.html - Show screenshot**
```html
<!-- In the project detail page, show larger screenshot -->
<div th:if="${project.imageUrl}" class="rounded-lg overflow-hidden shadow-lg mb-8">
  <img th:src="${project.imageUrl}" th:alt="${project.title}" class="w-full">
</div>
```

### Database Update (init.sql)
Add image URLs to projects:
```sql
UPDATE projects SET image_url = '/images/lms-screenshot.png' WHERE slug = 'learning-management-platform';
UPDATE projects SET image_url = '/images/portfolio-screenshot.png' WHERE slug = 'portfolio-website';
UPDATE projects SET image_url = '/images/chatbot-screenshot.png' WHERE slug = 'chatbot-project';
```

---

## Task 5: Add Tech Stack to Project Cards

### Current Issue
- Project cards only show title + links
- Tech stack only visible on detail page
- Visitors can't see technologies at a glance

### Solution
Add 3-4 tech badges to each project card.

### Update fragments/project-card.html

```html
<!-- Add after the project title, before the links -->
<div class="flex flex-wrap gap-2 mb-4" th:if="${project.techStack != null and !project.techStack.isEmpty()}">
  <span th:each="tech : ${project.techStack}" 
        th:if="${#lists.indexOf(project.techStack, tech) < 4}"
        th:text="${tech}"
        class="px-2 py-1 bg-gray-100 text-gray-600 text-xs rounded-full">
  </span>
  <span th:if="${project.techStack.size() > 4}" 
        class="px-2 py-1 bg-gray-100 text-gray-500 text-xs rounded-full">
    +<span th:text="${project.techStack.size() - 4}"></span> more
  </span>
</div>
```

This shows first 4 technologies + "+N more" if there are additional.

---

## Task 6: Finishing Touches

### 6a. Add Favicon

**User must provide:** favicon files from realfavicongenerator.net
- favicon.ico
- favicon-32x32.png
- favicon-16x16.png
- apple-touch-icon.png

**Place in:** `src/main/resources/static/`

**Update templates (add to <head> in base layout or each page):**
```html
<link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
<link rel="icon" type="image/png" sizes="32x32" href="/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="16x16" href="/favicon-16x16.png">
<link rel="icon" href="/favicon.ico">
```

### 6b. Style error.html

Current error page is unstyled. Match it to site design:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Error - Cesar Villarreal</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body class="font-sans bg-gray-50 min-h-screen flex items-center justify-center">
  <div class="text-center px-4">
    <h1 class="text-6xl font-bold text-gray-300 mb-4">Oops!</h1>
    <p class="text-xl text-gray-600 mb-8">Something went wrong.</p>
    <a href="/" class="inline-flex items-center px-6 py-3 bg-sky-600 text-white rounded-lg hover:bg-sky-700 transition-colors">
      ← Back to Home
    </a>
  </div>
</body>
</html>
```

### 6c. Meta Tags for SEO & Social Sharing (Optional but Recommended)

Add to each page's <head>:
```html
<meta name="description" content="Cesar Villarreal - Cloud Engineer & Software Developer portfolio showcasing full-stack projects">
<meta property="og:title" content="Cesar Villarreal - Portfolio">
<meta property="og:description" content="Cloud Engineer | Technical Consultant | Software Engineer">
<meta property="og:image" content="/images/portfolio-screenshot.png">
<meta property="og:type" content="website">
```

---

## Task 7: Update Certifications (about.html)

### Keep These 3 Only
- AWS Solutions Architect Associate
- Microsoft Azure Fundamentals
- CompTIA Security+

### Remove
- AWS Cloud Practitioner (SAA supersedes it)
- Teaching certification (keep focus technical)

---

## Information Needed from User

### All Project Details - PROVIDED ✅
See Task 3 above for complete project data.

### For Images (User Must Prepare)

| Asset | How to Get It |
|-------|---------------|
| **headshot.jpg** | Professional photo (400x400px, square crop) |
| **gamedev-screenshot.png** | Screenshot your LMS dashboard |
| **portfolio-screenshot.png** | Screenshot this portfolio's home page |
| **chatbot-screenshot.png** | Screenshot the pet app UI |

### For Favicon
1. Go to https://realfavicongenerator.net
2. Upload an image with your initials "CV" or a simple icon
3. Download the favicon package
4. Extract to `src/main/resources/static/`

---

## Execution Order

1. **Unify skill badges** in about.html (all gray style)
2. **Update certifications** in about.html (keep only 3: AWS SAA, Azure, Security+)
3. **Remove phone number** from about.html and contact.html
4. **Update init.sql** with the 3 projects defined in Task 3
5. **Add tech stack badges** to project cards
6. **Update templates** for project images (with fallback)
7. **Add headshot section** to about.html
8. **Add favicon links** to templates
9. **Style error.html** to match site
10. **Add meta tags** for SEO
11. **Reset database**: `docker-compose down -v && docker-compose up -d`
12. **Test all pages**

**User tasks (after Claude Code finishes):**
- Add headshot.jpg to static/images/
- Add project screenshots to static/images/
- Add favicon files to static/

---

## Files Summary

| File | Changes |
|------|---------|
| `about.html` | Remove phone, unify badges to gray, update certs, add headshot |
| `contact.html` | Remove phone, update response time text |
| `error.html` | Style to match site design |
| `fragments/project-card.html` | Add tech stack badges, add image support |
| `project-detail.html` | Add image display section |
| `scripts/init.sql` | Replace with 3 projects from Task 3 |
| All templates | Add favicon links, meta tags |

---

## Starter Prompt for Claude Code

```
Read docs/PHASE_6_SUBPLAN.md and execute all tasks. All details are in the file.
```

---

## Notes

- User mentioned "Cesar Villarreal" not "Cesar Guerra" - already handled in Phase 1
- Package is `com.cesarvillarreal.portfolio`
- All tests passing (32/32)
- JaCoCo disabled due to Java 25 incompatibility
- Color decision: Keep blue, only unify skill badges to gray
- Certifications: Keep AWS SAA, Azure Fundamentals, Security+ (remove Practitioner)
