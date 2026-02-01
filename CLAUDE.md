# CLAUDE.md - Portfolio Website Build Instructions

> **For Claude Code:** This document contains everything needed to build Cesar's portfolio website from scratch. Follow these instructions sequentially.

---

## Project Overview

**Goal:** Build a full-stack portfolio website demonstrating enterprise Java/Spring skills for cloud engineering and consulting roles.

**Owner:** Cesar Villarreal  
**Target Roles:** Cloud Engineer, Technical Consultant, Software Engineer  
**Timeline:** MVP in 1-2 weeks

---

## Tech Stack (Non-Negotiable)

| Layer | Technology | Why |
|-------|------------|-----|
| **Backend** | Spring Boot 3.2+ | Target role relevance |
| **Java** | Java 21 | Latest LTS |
| **Templates** | Thymeleaf | SSR, SEO, single deploy |
| **CSS** | Tailwind CSS (CDN) | Rapid styling, no build step |
| **Database** | PostgreSQL 16 | Industry standard |
| **ORM** | Spring Data JPA | Standard for Spring |
| **Validation** | Jakarta Validation | Form handling |
| **Build** | Maven | Enterprise standard |
| **Containers** | Docker + Docker Compose | Local dev + production |
| **CI/CD** | GitHub Actions | Free, native integration |
| **Cloud** | AWS App Runner + RDS | Managed, uses free credits |
| **Monitoring** | Spring Actuator | Health checks |
| **Testing** | JUnit 5 + MockMvc | Integration tests |
| **Coverage** | JaCoCo | 70%+ target |

---

## Directory Structure to Create

```
cesar-portfolio/
├── CLAUDE.md                           # This file
├── README.md                           # Project readme
├── pom.xml                             # Maven config (PROVIDED)
├── docker-compose.yml                  # Local PostgreSQL (PROVIDED)
├── Dockerfile                          # Production build (PROVIDED)
├── .gitignore                          # Git ignores
│
├── docs/                               # Documentation (PROVIDED)
│   ├── ARCHITECTURE_DECISIONS.md
│   ├── MVP_SCOPE.md
│   ├── PROJECT_PLAN.md
│   └── PROJECT_SHOWCASE.md
│
├── scripts/
│   └── init.sql                        # Database seed (PROVIDED)
│
├── .github/
│   └── workflows/
│       ├── ci.yml                      # Test on PR (PROVIDED)
│       └── deploy.yml                  # Deploy on merge (PROVIDED)
│
└── src/
    ├── main/
    │   ├── java/com/cesarvillarreal/portfolio/
    │   │   ├── PortfolioApplication.java       # Main class
    │   │   ├── config/
    │   │   │   └── WebConfig.java              # Web configuration
    │   │   ├── model/
    │   │   │   ├── Project.java                # Project entity
    │   │   │   └── ContactMessage.java         # Contact form entity
    │   │   ├── repository/
    │   │   │   ├── ProjectRepository.java
    │   │   │   └── ContactMessageRepository.java
    │   │   ├── service/
    │   │   │   ├── ProjectService.java
    │   │   │   └── ContactService.java
    │   │   └── controller/
    │   │       ├── HomeController.java         # Web pages
    │   │       └── ApiController.java          # REST API
    │   │
    │   └── resources/
    │       ├── application.yml                 # Base config
    │       ├── application-dev.yml             # Local dev
    │       ├── application-prod.yml            # Production
    │       ├── templates/
    │       │   ├── layout/
    │       │   │   └── base.html               # Main layout
    │       │   ├── fragments/
    │       │   │   ├── navbar.html
    │       │   │   ├── footer.html
    │       │   │   └── project-card.html
    │       │   ├── index.html                  # Home page
    │       │   ├── about.html
    │       │   ├── projects.html
    │       │   ├── project-detail.html
    │       │   ├── contact.html
    │       │   ├── how-i-built-this.html
    │       │   └── error.html
    │       └── static/
    │           ├── css/
    │           │   └── custom.css              # Custom overrides
    │           ├── js/
    │           │   └── main.js                 # Minimal JS
    │           └── images/
    │               └── .gitkeep
    │
    └── test/
        ├── java/com/cesarvillarreal/portfolio/
        │   ├── PortfolioApplicationTests.java
        │   ├── controller/
        │   │   ├── HomeControllerTest.java
        │   │   └── ApiControllerTest.java
        │   └── repository/
        │       └── ProjectRepositoryTest.java
        └── resources/
            └── application-test.yml            # H2 for tests
```

---

## Database Schema

### Projects Table

```sql
CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    short_description TEXT,
    full_description TEXT,
    github_url VARCHAR(500),
    live_url VARCHAR(500),
    image_url VARCHAR(500),
    featured BOOLEAN DEFAULT FALSE,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE project_tech_stack (
    project_id BIGINT REFERENCES projects(id) ON DELETE CASCADE,
    technology VARCHAR(100)
);

CREATE INDEX idx_project_slug ON projects(slug);
CREATE INDEX idx_project_featured ON projects(featured);
```

### Contact Messages Table

```sql
CREATE TABLE contact_messages (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_contact_read ON contact_messages(read);
```

---

## Routes to Implement

### Web Routes (Thymeleaf)

| Method | Path | Controller Method | Template | Description |
|--------|------|-------------------|----------|-------------|
| GET | `/` | `home()` | index.html | Hero + featured projects |
| GET | `/about` | `about()` | about.html | Bio, skills, experience |
| GET | `/projects` | `projects()` | projects.html | All projects grid |
| GET | `/projects/{slug}` | `projectDetail()` | project-detail.html | Single project |
| GET | `/contact` | `contactForm()` | contact.html | Contact form |
| POST | `/contact` | `submitContact()` | redirect | Save message |
| GET | `/how-i-built-this` | `howIBuiltThis()` | how-i-built-this.html | Architecture showcase |

### REST API Routes (JSON)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/projects` | List all projects |
| GET | `/api/projects/{slug}` | Single project |
| GET | `/actuator/health` | Health check (built-in) |

---

## Frontend: Component Library Approach

**DO NOT use a full template.** Build the frontend yourself using Tailwind CSS component libraries.

### Design Inspiration (Reference Only)

| Site | What to Study |
|------|---------------|
| brittanychiang.com | Section flow, hover effects |
| simplefolio.netlify.app | Hero → About → Projects → Contact structure |

### Component Libraries (Copy + Customize)

| Library | URL | Use For |
|---------|-----|---------|
| **HyperUI** | hyperui.dev | Navbars, cards, hero sections |
| **Preline UI** | preline.co | Full sections, forms |
| **Flowbite** | flowbite.com | Buttons, form inputs |
| **Meraki UI** | merakiui.com | Cards, profile sections |
| **Tailblocks** | tailblocks.cc | Hero, features, contact |

### Section → Component Mapping

| Section | Get From | Customize |
|---------|----------|-----------|
| **Navbar** | HyperUI | Sticky, links to sections |
| **Hero** | Preline/Tailblocks | Name, title, CTA |
| **About** | Meraki UI | Bio, skills list, photo |
| **Projects** | Flowbite/HyperUI | Card grid with `th:each` |
| **How I Built This** | Custom | Architecture diagram, ADR links |
| **Contact** | Preline | Form with validation |
| **Footer** | Any | Social links |

### Required Customizations (Makes It YOUR Work)

- [ ] Color scheme (not default Tailwind blue)
- [ ] Typography (font choices)
- [ ] Spacing and proportions
- [ ] Hover effects
- [ ] Mobile responsiveness
- [ ] Thymeleaf integration (`th:each`, `th:text`, `th:href`)

---

## Configuration Files

### application.yml (Base)

```yaml
spring:
  application:
    name: cesar-portfolio
  jpa:
    open-in-view: false
  thymeleaf:
    cache: true

management:
  endpoints:
    web:
      exposure:
        include: health,info

server:
  port: 8080
```

### application-dev.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/portfolio
    username: dev
    password: devpassword
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  thymeleaf:
    cache: false

logging:
  level:
    com.cesarvillarreal.portfolio: DEBUG
```

### application-prod.yml

```yaml
spring:
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate

logging:
  level:
    com.cesarvillarreal.portfolio: INFO
```

### application-test.yml

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;MODE=PostgreSQL
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
```

---

## Build Order (Follow This Sequence)

### Phase 1: Foundation (Day 1)

1. Create directory structure
2. Create `PortfolioApplication.java` main class
3. Create configuration files (application*.yml)
4. Create `.gitignore`
5. Verify app starts: `mvn spring-boot:run`

### Phase 2: Database Layer (Day 1-2)

1. Start PostgreSQL: `docker-compose up -d`
2. Create `Project` entity with JPA annotations
3. Create `ContactMessage` entity
4. Create repositories (extend JpaRepository)
5. Create services with business logic
6. Test database connection

### Phase 3: Web Controllers (Day 2-3)

1. Create `HomeController` with all web routes
2. Create basic Thymeleaf templates (no styling yet)
3. Verify all pages render with test data
4. Create `ApiController` for REST endpoints
5. Test API returns JSON

### Phase 4: Frontend (Day 3-5)

1. Create `base.html` layout with Tailwind CDN
2. Build navbar fragment (from HyperUI)
3. Build hero section (from Preline)
4. Build about section
5. Build project cards with `th:each`
6. Build contact form
7. Build footer
8. Add responsive breakpoints
9. Add hover effects
10. Customize colors

### Phase 5: Testing (Day 5-6)

1. Create `HomeControllerTest` with MockMvc
2. Create `ApiControllerTest`
3. Create `ProjectRepositoryTest`
4. Run coverage: `mvn test jacoco:report`
5. Achieve 70%+ coverage

### Phase 6: Polish (Day 6-7)

1. Add "How I Built This" page with architecture details
2. Add proper error handling
3. Add form validation messages
4. Test all routes manually
5. Mobile testing

### Phase 7: Deploy (Day 7+)

1. Push to GitHub
2. Enable CodeRabbit on repo
3. Create AWS RDS PostgreSQL instance
4. Create ECR repository
5. Create App Runner service
6. Configure environment variables
7. Deploy and verify

---

## Commands Reference

```bash
# === Local Development ===
docker-compose up -d                    # Start PostgreSQL
mvn spring-boot:run -Dspring-boot.run.profiles=dev  # Run app
mvn spring-boot:run                     # Run (uses default profile)

# === Testing ===
mvn test                                # Run all tests
mvn test -Dtest=HomeControllerTest      # Run specific test
mvn test jacoco:report                  # Generate coverage report
# Coverage report at: target/site/jacoco/index.html

# === Build ===
mvn clean package                       # Build JAR
mvn clean package -DskipTests           # Build without tests

# === Docker ===
docker build -t cesar-portfolio .       # Build image
docker run -p 8080:8080 cesar-portfolio # Run container

# === Database ===
docker-compose exec postgres psql -U dev -d portfolio  # Connect to DB
docker-compose down -v                  # Reset database (deletes data)

# === Useful ===
mvn dependency:tree                     # Show dependencies
mvn versions:display-dependency-updates # Check for updates
```

---

## Seed Data (4 Projects)

Create these projects in the database or via DataLoader:

### 1. Learning Management System (Featured)

```
title: "Learning Management Platform"
slug: "learning-management-platform"
shortDescription: "Full-stack LMS serving 200+ students with course management, assignments, and grading"
techStack: ["Django", "React", "PostgreSQL", "Docker", "AWS"]
githubUrl: "https://github.com/cesarvillarreal/lms"
featured: true
displayOrder: 1
```

### 2. This Portfolio (Featured)

```
title: "Portfolio Website"
slug: "portfolio-website"
shortDescription: "The site you're looking at - Spring Boot + Thymeleaf + AWS App Runner"
techStack: ["Spring Boot", "Thymeleaf", "PostgreSQL", "AWS App Runner", "GitHub Actions"]
githubUrl: "https://github.com/cesarvillarreal/portfolio"
liveUrl: "https://cesarvillarreal.dev"
featured: true
displayOrder: 2
```

### 3. Cloud Infrastructure Automation

```
title: "Cloud Infrastructure Automation"
slug: "cloud-automation"
shortDescription: "Reusable Terraform modules for AWS infrastructure provisioning"
techStack: ["Terraform", "AWS", "Python", "GitHub Actions"]
githubUrl: "https://github.com/cesarvillarreal/cloud-automation"
featured: false
displayOrder: 3
```

### 4. VEX Robotics Controller

```
title: "VEX Robotics Controller"
slug: "vex-robotics"
shortDescription: "Competition robot control system with PID tuning and autonomous routines"
techStack: ["C++", "VEXcode", "PID Control"]
githubUrl: "https://github.com/cesarvillarreal/vex-robotics"
featured: false
displayOrder: 4
```

---

## Testing Requirements

### Minimum Test Coverage: 70%

### Required Tests

**HomeControllerTest:**
- `GET /` returns 200 and contains "Cesar"
- `GET /projects` returns 200 and shows projects
- `GET /projects/{slug}` returns 200 for valid slug
- `GET /projects/{slug}` returns 404 for invalid slug
- `POST /contact` with valid data redirects
- `POST /contact` with invalid data shows errors

**ApiControllerTest:**
- `GET /api/projects` returns JSON array
- `GET /api/projects/{slug}` returns JSON object
- `GET /api/projects/{slug}` returns 404 for invalid slug

**ProjectRepositoryTest:**
- Save and retrieve project
- Find by slug
- Find featured projects
- Find all ordered by displayOrder

---

## Key ADRs (Read docs/ Before Building)

| ADR | Decision | File |
|-----|----------|------|
| 001 | Spring Boot (not Node/Python) | docs/ARCHITECTURE_DECISIONS.md |
| 002 | Thymeleaf (not React SPA) | docs/ARCHITECTURE_DECISIONS.md |
| 003 | PostgreSQL (not MongoDB) | docs/ARCHITECTURE_DECISIONS.md |
| 014 | Component Libraries (not full template) | docs/ARCHITECTURE_DECISIONS.md |
| 015 | MVP scope (what's in/out) | docs/MVP_SCOPE.md |
| 016 | Tech enhancements (tests, API, health) | docs/ARCHITECTURE_DECISIONS.md |

---

## Interview Talking Points

When asked about this project:

**"Walk me through the architecture"**
→ Standard MVC with Spring Boot, Thymeleaf for SSR, PostgreSQL for persistence, deployed on AWS App Runner with RDS.

**"How do you ensure code quality?"**
→ JUnit 5 unit tests, MockMvc integration tests, 70%+ coverage with JaCoCo, CodeRabbit reviews on every PR.

**"Did you build the frontend?"**
→ Yes, I used Tailwind CSS and assembled components from HyperUI and Preline, then customized everything and integrated with Thymeleaf templates.

**"How do you use AI tools?"**
→ Claude Code for pair programming, but I own all architectural decisions (documented in ADRs) and review all generated code.

**"Why Spring Boot over Node/Python?"**
→ Target role alignment - consulting firms and enterprises use Java/Spring heavily. Also demonstrates I can work in statically-typed, enterprise ecosystems.

---

## Don't Forget

- [ ] Real photo of yourself for about section
- [ ] Screenshots of each project
- [ ] Actual GitHub repos linked
- [ ] Contact form sends to real email (or stores in DB)
- [ ] Custom domain (cesarvillarreal.dev or similar)
- [ ] Enable CodeRabbit when repo is created
- [ ] Mobile test on real phone before launch
