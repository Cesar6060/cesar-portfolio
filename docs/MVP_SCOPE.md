# MVP Scope - Get Live Fast

> **Goal:** Deploy a polished, working portfolio in 1 weekend, then iterate.

---

## Philosophy: Ship Then Improve

```
❌ Wrong approach:
   Plan everything → Build everything → Deploy → Never launch

✅ Right approach:
   Plan MVP → Build MVP → Deploy → Add features live
```

---

## MVP Scope (Weekend 1)

### Pages to Build

| Page | Priority | Content Needed |
|------|----------|----------------|
| **Home** | ✅ Must have | Hero, brief intro, 3 featured projects |
| **Projects** | ✅ Must have | Grid of 3-4 projects with links |
| **About** | ✅ Must have | Bio, skills, what you're looking for |
| **Contact** | ✅ Must have | Simple form (can just be mailto: link for MVP) |
| **How I Built This** | ✅ Must have | Your differentiator - simplified ADRs |
| ~~/Resume~~ | ⏸️ Phase 2 | Link to PDF for now |
| ~~/Admin~~ | ⏸️ Phase 2 | Edit database directly or redeploy |
| ~~/Blog~~ | ❌ Cut | Not needed for job search |

### Features

| Feature | MVP | Phase 2 |
|---------|-----|---------|
| Static project data | ✅ Hardcode in templates | Database + admin |
| Contact form | ✅ mailto: link | Store in database |
| GitHub OAuth | ❌ Skip | Add for admin panel |
| Admin panel | ❌ Skip | Add when needed |
| Dark mode | ⏸️ Nice to have | Easy with Tailwind |
| Analytics | ⏸️ Add after launch | Plausible or GA |

### Infrastructure

| Component | MVP | Phase 2 |
|-----------|-----|---------|
| Database | ❌ Skip for MVP | Add when admin needed |
| App Runner | ✅ Yes | Same |
| ECR | ✅ Yes | Same |
| Route 53 | ✅ Yes (or external registrar) | Same |
| RDS | ❌ Skip | Add with admin panel |
| Secrets Manager | ❌ Skip | Add with OAuth |

---

## MVP Architecture (Simplified)

```
┌─────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   GitHub    │────▶│  GitHub Actions │────▶│      ECR        │
│   (code)    │     │  - build        │     │  (Docker image) │
└─────────────┘     │  - push         │     └────────┬────────┘
                    └─────────────────┘              │
                                                     ▼
┌─────────────┐                             ┌─────────────────┐
│  Route 53   │◄────────────────────────────│   App Runner    │
│  (domain)   │                             │  (runs app)     │
└─────────────┘                             └─────────────────┘

No database! Projects hardcoded in templates or a JSON file.
```

---

## What Gets Cut (For Now)

| Feature | Why Cut | When to Add |
|---------|---------|-------------|
| PostgreSQL/RDS | No dynamic content needed yet | When you want admin panel |
| Spring Security | No protected routes | When you add admin |
| GitHub OAuth | No login needed | When you add admin |
| Admin panel | Overkill for MVP | After you're hired 😄 |
| Contact form DB storage | mailto: works fine | When you want to track leads |

---

## MVP Tech Stack

```
┌─────────────────────────────────────────────┐
│              SIMPLIFIED STACK               │
├─────────────────────────────────────────────┤
│  Spring Boot 3.2 + Java 21                  │
│  Thymeleaf templates                        │
│  Tailwind CSS (via CDN)                     │
│  Static project data (no database)          │
│  Docker → ECR → App Runner                  │
│  GitHub Actions (build + deploy)            │
└─────────────────────────────────────────────┘
```

---

## MVP Timeline

### Day 1 (Saturday) - 4-6 hours

| Time | Task |
|------|------|
| 1 hr | Set up Spring Boot project + Tailwind template |
| 2 hr | Build Home + About pages with real content |
| 1 hr | Build Projects page with 3-4 hardcoded projects |
| 1 hr | Build "How I Built This" page |
| 1 hr | Build Contact page (mailto: link) |

### Day 2 (Sunday) - 4-6 hours

| Time | Task |
|------|------|
| 1 hr | Create Dockerfile, test locally |
| 1 hr | Set up ECR + push image |
| 1 hr | Set up App Runner |
| 1 hr | Configure domain + SSL |
| 1 hr | Test everything, fix bugs |
| 1 hr | Final polish, screenshot for LinkedIn |

---

## Content You Need Before Starting

### Projects (3-4)

For each project, prepare:
- [ ] Title
- [ ] One-paragraph description
- [ ] Tech stack (list)
- [ ] Screenshot or image
- [ ] GitHub link (if public)
- [ ] Live demo link (if available)
- [ ] 2-3 bullet points of what you learned/achieved

### About Page

- [ ] 2-3 paragraph bio (your story: math → cloud → teaching → industry)
- [ ] Skills list with categories
- [ ] What you're looking for (cloud engineering, consulting, etc.)
- [ ] Professional photo

### How I Built This Page

- [ ] Architecture diagram (simplified)
- [ ] 3-5 key decisions with one-line rationale
- [ ] Link to full ADR doc on GitHub

---

## Phase 2 Features (After MVP)

Add these incrementally after the site is live:

| Feature | Effort | Impact |
|---------|--------|--------|
| Dark mode toggle | 2 hrs | Medium |
| Analytics (Plausible) | 30 min | Low |
| Resume page | 2 hrs | Medium |
| Database + Admin panel | 4-6 hrs | High (enables dynamic content) |
| Blog section | 4-6 hrs | Medium |
| Contact form with storage | 2 hrs | Low |

---

## Files Changed for MVP

Update these in the project:

### CLAUDE.md
- Remove database references
- Remove admin panel routes
- Simplify to MVP scope

### pom.xml
- Remove spring-boot-starter-data-jpa
- Remove spring-boot-starter-security
- Remove spring-boot-starter-oauth2-client
- Remove postgresql driver

### application.yml
- Remove database config
- Keep it minimal

---

## Success Criteria

MVP is done when:

- [ ] Site loads at your domain with HTTPS
- [ ] Home page shows your name + featured projects
- [ ] Projects page lists 3-4 real projects
- [ ] About page tells your story
- [ ] "How I Built This" page shows architecture
- [ ] Contact has a way to reach you
- [ ] Mobile responsive
- [ ] Lighthouse score > 80
- [ ] You'd be proud to send the link to a recruiter
