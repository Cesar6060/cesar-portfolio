# Architecture Decision Records (ADR)

This document tracks every technical decision made while building this portfolio site, including the reasoning, alternatives considered, and tradeoffs. This site is itself a portfolio piece demonstrating thoughtful engineering decisions.

---

## ADR-001: Backend Framework - Spring Boot

**Date:** January 2025  
**Status:** Accepted

### Context
Need a backend framework for a personal portfolio website that will be deployed on AWS.

### Decision
Use **Spring Boot 3.x with Java 21**

### Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **Spring Boot** | Industry standard for enterprise, strong ecosystem, good for practicing interview skills | Heavier than needed for a simple site, longer startup time |
| **Django (Python)** | Already familiar, faster development | Wanted to practice Java/Spring for target roles |
| **Node.js/Express** | Lightweight, fast development | Less relevant to target cloud/consulting roles |
| **Go + Fiber** | Extremely fast, small binaries | Learning curve, less ecosystem |

### Rationale
- Target roles (Capital One, Deloitte, cloud consulting) heavily use Java/Spring
- Demonstrates enterprise-grade skills even for a "simple" project
- Spring Security + OAuth2 are valuable skills to demonstrate
- AWS has excellent Java support (Corretto, App Runner optimizations)

### Tradeoffs Accepted
- Slower cold starts vs Node/Go (mitigated by App Runner's warm instances)
- More boilerplate than Django (mitigated by Lombok, Spring Boot conventions)
- Overkill for a portfolio site (but that's the point - showing I can use enterprise tools)

### Consequences
- Must use Java 21 for modern features (virtual threads, records)
- Container image will be larger (~200MB vs ~50MB for Go)
- Need to optimize startup time for serverless-style deployment

---

## ADR-002: Frontend Approach - Server-Side Rendering with Thymeleaf

**Date:** January 2025  
**Status:** Accepted

### Context
Need to decide between server-side rendering (SSR) and single-page application (SPA) for the frontend.

### Decision
Use **Thymeleaf** for server-side rendering with **Tailwind CSS** via CDN for styling.

### Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **Thymeleaf (SSR)** | Single codebase, great SEO, simpler deployment, fast initial load | Less interactive, page refreshes |
| **React SPA** | Modern, highly interactive, good for job apps | Separate build, CORS setup, SEO needs extra work, overkill |
| **HTMX + Thymeleaf** | Best of both worlds, progressive enhancement | Newer, less established |
| **Next.js** | Great DX, SSR + SPA hybrid | Would need separate backend or rewrite in Node |

### Rationale
- Portfolio sites are content-focused, not interaction-heavy
- SEO matters for discoverability (recruiters Googling my name)
- Single deployable artifact simplifies CI/CD
- Demonstrates I can choose appropriate tech (not everything needs React)
- Tailwind via CDN avoids build complexity while looking modern

### Tradeoffs Accepted
- Less impressive to frontend-focused roles (but targeting backend/cloud roles)
- Page transitions aren't as smooth as SPA
- Limited interactivity without JavaScript additions

### Future Consideration
Could add HTMX later for enhanced interactivity without full SPA complexity.

---

## ADR-003: Database - PostgreSQL

**Date:** January 2025  
**Status:** Accepted

### Context
Need a database to store projects, contact form submissions, and potentially blog posts.

### Decision
Use **PostgreSQL 16**

### Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **PostgreSQL** | Industry standard, AWS RDS support, powerful features | Requires managed service or container |
| **SQLite** | Zero config, embedded, free | Not production-grade for web apps, no AWS managed option |
| **MySQL** | Widely used, RDS support | PostgreSQL has better features, more modern |
| **MongoDB** | Flexible schema, good for content | Overkill, SQL skills more relevant for target roles |
| **No database (static)** | Simplest, cheapest | Can't have contact form, admin panel, or dynamic content |

### Rationale
- RDS PostgreSQL has a 12-month free tier (t3.micro)
- Industry standard - demonstrates real-world database skills
- Spring Data JPA has excellent PostgreSQL support
- Enables dynamic features (admin panel, contact form, analytics)

### Tradeoffs Accepted
- Monthly cost after free tier (~$15/month for t3.micro)
- Requires connection pooling consideration for serverless
- More complex than static site

---

## ADR-004: Authentication - GitHub OAuth2

**Date:** January 2025  
**Status:** Accepted

### Context
Need authentication for an admin panel to manage projects and view contact submissions without redeploying.

### Decision
Use **Spring Security with GitHub OAuth2** for admin authentication.

### Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **GitHub OAuth2** | No passwords, demonstrates OAuth2 skills, free | Tied to GitHub account |
| **AWS Cognito** | Managed, feature-rich, uses AWS credits | More complex setup, overkill for single admin |
| **Simple username/password** | Easy to implement | Password management, less secure, less impressive |
| **No auth (redeploy to update)** | Simplest | Poor DX, can't view contact submissions |
| **Magic link (email)** | Passwordless, secure | Requires email service setup |

### Rationale
- Only one admin user (me), so GitHub OAuth is perfect
- Demonstrates OAuth2 implementation skills
- Zero password management overhead
- Spring Security OAuth2 client makes this straightforward

### Tradeoffs Accepted
- Can only log in with GitHub (acceptable since I'm the only admin)
- Requires GitHub OAuth app registration
- Callback URL must be configured per environment

---

## ADR-005: Deployment Platform - AWS App Runner

**Date:** January 2025  
**Status:** Accepted

### Context
Need to deploy a containerized Spring Boot application with minimal operational overhead.

### Decision
Use **AWS App Runner** with containers stored in **ECR**.

### Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **App Runner** | Fully managed, auto-scaling, simple, supports containers | Less control, newer service |
| **ECS Fargate** | More control, mature | More complex setup, need ALB |
| **EC2** | Full control, familiar | Must manage instance, patching, scaling |
| **Elastic Beanstalk** | Easy deployment, managed | Aging service, less modern |
| **Lambda + API Gateway** | Pay per request, scales to zero | Cold starts hurt Spring Boot, architectural changes needed |
| **Heroku** | Very easy | Expensive, not AWS (can't use credits) |

### Rationale
- Fully managed = less ops overhead for a portfolio site
- Native container support fits our Dockerized app
- Auto-scaling handles traffic spikes (if site goes viral)
- Uses AWS credits efficiently
- Simpler than ECS but more robust than Elastic Beanstalk

### Tradeoffs Accepted
- Less control than ECS/EC2
- Minimum cost even at zero traffic (~$5/month)
- Newer service, less community knowledge

### Cost Estimate
- App Runner: ~$5-15/month
- ECR: ~$1/month
- Total compute: ~$6-16/month

---

## ADR-006: Infrastructure as Code - Terraform

**Date:** January 2025  
**Status:** Accepted

### Context
Need to provision AWS resources. Could do manually or with IaC.

### Decision
Use **Terraform** to define all AWS infrastructure.

### Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **Terraform** | Cloud-agnostic, industry standard, declarative | Learning curve, state management |
| **AWS CDK** | Native AWS, TypeScript/Java | AWS-only, more code |
| **CloudFormation** | Native AWS, no state file | Verbose YAML/JSON, AWS-only |
| **Pulumi** | Real programming languages | Less adoption than Terraform |
| **Manual (Console)** | Quick for simple setups | Not reproducible, error-prone, can't showcase |

### Rationale
- Terraform is the industry standard for IaC (valuable skill for consulting)
- Makes infrastructure itself a portfolio piece
- Reproducible deployments
- Can open-source the Terraform modules
- State can be stored in S3 for team scenarios

### Tradeoffs Accepted
- Initial setup overhead
- Need to manage state file
- Learning curve if not familiar

### Modules to Create
```
terraform/
├── modules/
│   ├── ecr/
│   ├── app-runner/
│   ├── rds/
│   ├── route53/
│   └── secrets-manager/
├── environments/
│   ├── prod/
│   └── staging/
└── main.tf
```

---

## ADR-007: CI/CD - GitHub Actions

**Date:** January 2025  
**Status:** Accepted

### Context
Need automated testing and deployment pipeline.

### Decision
Use **GitHub Actions** for CI/CD.

### Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **GitHub Actions** | Native to GitHub, free tier generous, easy setup | Vendor lock-in to GitHub |
| **AWS CodePipeline** | Native AWS, uses credits | More complex, less intuitive |
| **Jenkins** | Highly customizable, self-hosted | Overkill, requires maintenance |
| **CircleCI** | Good DX, powerful | Another service to manage |
| **GitLab CI** | Great if using GitLab | Not using GitLab |

### Rationale
- 2000 free minutes/month is plenty for a portfolio site
- Native GitHub integration (already using GitHub)
- Easy to set up and understand
- YAML config is portable knowledge
- Can showcase workflow files as part of the project

### Tradeoffs Accepted
- Tied to GitHub (acceptable, already committed to GitHub)
- Less powerful than Jenkins for complex scenarios

### Pipeline Stages
```
PR → Test → (merge) → Build → Push to ECR → Deploy to App Runner
```

---

## ADR-008: Code Review - CodeRabbit

**Date:** January 2025  
**Status:** Accepted

### Context
Working solo but want code review benefits and to demonstrate professional practices.

### Decision
Use **CodeRabbit** (free tier for public repos) for AI-powered code review.

### Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **CodeRabbit** | Free for public repos, good signal-to-noise, learns over time | Limited to public repos for free |
| **Greptile** | Higher bug detection (82% vs 44%) | $30/month, noisier reviews |
| **GitHub Copilot** | Already in GitHub, includes PR review | $10-39/month, less specialized |
| **No code review** | Free, simple | Miss bugs, less professional |

### Rationale
- Free for public repositories
- Balanced feedback (not too noisy)
- PR summaries help document changes
- Shows I use professional tooling even for personal projects

### Tradeoffs Accepted
- Repository must be public
- Lower bug detection than Greptile (44% vs 82%)
- AI reviews aren't perfect

---

## ADR-009: Styling - Tailwind CSS via CDN

**Date:** January 2025  
**Status:** Accepted

### Context
Need CSS framework for styling the frontend.

### Decision
Use **Tailwind CSS via CDN** (no build step).

### Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **Tailwind CDN** | No build step, instant setup, modern look | Larger file size, no purging |
| **Tailwind (compiled)** | Optimized output, full features | Requires Node.js build step |
| **Bootstrap** | Familiar, comprehensive | Looks generic, heavier |
| **Custom CSS** | Full control, minimal size | Time-consuming, inconsistent |
| **No framework** | Minimal | Poor DX, inconsistent design |

### Rationale
- CDN version works immediately with Thymeleaf
- No Node.js required in the project
- Modern utility-first approach
- Good enough for a portfolio site
- Can upgrade to compiled version later if needed

### Tradeoffs Accepted
- Larger CSS payload (~300KB vs ~10KB compiled)
- No custom Tailwind configuration
- All utilities included, not tree-shaken

---

## ADR-010: Local Development - Docker Compose

**Date:** January 2025  
**Status:** Accepted

### Context
Need PostgreSQL for local development without installing it directly.

### Decision
Use **Docker Compose** for local PostgreSQL instance.

### Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **Docker Compose** | Isolated, reproducible, matches prod | Requires Docker Desktop |
| **Install PostgreSQL locally** | Direct access, no Docker needed | Pollutes local machine, version conflicts |
| **H2 in-memory** | Zero setup, embedded | Doesn't match production, SQL differences |
| **Remote dev database** | Real cloud DB | Latency, cost, internet required |

### Rationale
- Docker Compose provides identical PostgreSQL version to production
- Easy to spin up/down
- No local installation pollution
- docker-compose.yml is self-documenting

### Tradeoffs Accepted
- Requires Docker Desktop installed
- Slight overhead vs H2 for quick tests

---

## ADR-011: Build Tool - Maven

**Date:** January 2025  
**Status:** Accepted

### Context
Need a build tool for the Spring Boot project. Both Maven and Gradle are industry standard options.

### Decision
Use **Maven** for build and dependency management.

### Alternatives Considered

| Option | Pros | Cons |
|--------|------|------|
| **Maven** | Already practicing, enterprise standard, explicit/verbose, massive ecosystem | Slower than Gradle, XML verbosity |
| **Gradle** | Faster builds, more concise, modern | Learning curve, less common in target enterprises |

### Rationale
- Already practicing Maven - no context switching needed
- Target roles (Capital One, Deloitte, enterprise consulting) heavily use Maven
- Explicit XML structure is easier to understand and debug
- Vast documentation and Stack Overflow coverage
- Spring Boot has excellent Maven support

### Tradeoffs Accepted
- Slower builds than Gradle (acceptable for small project)
- More verbose pom.xml vs build.gradle
- No incremental compilation benefits

### Consequences
- Use `mvn` commands instead of `./gradlew`
- pom.xml is the source of truth for dependencies
- Can use Maven Wrapper (mvnw) for reproducible builds

---

## ADR-012: Architecture Pattern - MVC

**Date:** January 2025  
**Status:** Accepted

### Context
Need to decide on an architectural pattern for structuring the application code.

### Decision
Use **MVC (Model-View-Controller)** pattern, which is the natural fit for Spring Boot + Thymeleaf server-side rendering.

### Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         BROWSER                                  │
│                    (HTTP Request/Response)                       │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      CONTROLLER                                  │
│              HomeController, ProjectController                   │
│                                                                  │
│   • Receives HTTP requests                                       │
│   • Validates input                                              │
│   • Calls services                                               │
│   • Returns view name + model data                               │
└─────────────────────────────────────────────────────────────────┘
                              │
              ┌───────────────┴───────────────┐
              ▼                               ▼
┌──────────────────────────────────────┐    ┌──────────────────────┐
│              MODEL                   │    │        VIEW          │
│                                      │    │                      │
│  Entities (JPA):                     │    │  Thymeleaf Templates │
│  • Project.java                      │    │  • index.html        │
│  • ContactMessage.java               │    │  • projects.html     │
│                                      │    │  • about.html        │
│  Services (Business Logic):          │    │  • contact.html      │
│  • ProjectService                    │    │                      │
│  • ContactService                    │    │  Receives model data │
│                                      │    │  and renders HTML    │
│  Repositories (Data Access):         │    │                      │
│  • ProjectRepository                 │    │                      │
│  • ContactMessageRepository          │    │                      │
└──────────────────────────────────────┘    └──────────────────────┘
```

### Alternatives Considered

| Pattern | Pros | Cons | Fit |
|---------|------|------|-----|
| **MVC** | Simple, well-understood, perfect for SSR, Spring's native pattern | Less separation than hexagonal | ✅ Best fit |
| **REST API + SPA** | Decoupled frontend, good for mobile apps | Two codebases, CORS, overkill for content site | ❌ |
| **Hexagonal/Clean Architecture** | Great separation, testable, domain-focused | Complex for simple CRUD, overengineered | ❌ |
| **CQRS** | Separate read/write models, scalable | Massive overkill, adds complexity | ❌ |
| **Microservices** | Independent deployment, scalability | Way overengineered for portfolio site | ❌ |

### Rationale
- **Simplicity:** MVC is the simplest pattern that solves the problem
- **Spring Boot native:** Framework is designed around MVC with `@Controller`, `Model`, and view resolution
- **Thymeleaf integration:** Templates naturally serve as the View layer
- **Appropriate complexity:** A portfolio site doesn't need hexagonal architecture
- **Understandable:** Easy to explain in interviews, easy for others to contribute

### Layer Responsibilities

| Layer | Responsibility | Spring Annotations |
|-------|----------------|-------------------|
| **Controller** | HTTP handling, input validation, view selection | `@Controller`, `@GetMapping`, `@PostMapping` |
| **Service** | Business logic, transaction management | `@Service`, `@Transactional` |
| **Repository** | Data access, queries | `@Repository`, extends `JpaRepository` |
| **Entity** | Domain objects, JPA mapping | `@Entity`, `@Table` |
| **DTO** | Data transfer, form binding | Records, `@Valid` |
| **View** | HTML rendering with dynamic data | Thymeleaf templates |

### Code Flow Example

```
1. Browser requests GET /projects/my-cool-project
2. ProjectController.getProject() receives request
3. Controller calls ProjectService.findBySlug("my-cool-project")
4. Service calls ProjectRepository.findBySlug()
5. Repository queries PostgreSQL, returns Optional<Project>
6. Service returns Project to Controller
7. Controller adds Project to Model, returns "project-detail"
8. Thymeleaf renders project-detail.html with Project data
9. HTML response sent to browser
```

### Tradeoffs Accepted
- Less "pure" separation than hexagonal (acceptable for project size)
- Controllers can become bloated if not careful (mitigated by thin controller rule)
- Tight coupling to Spring (acceptable, we're committed to Spring Boot)

### Design Rules to Follow
1. **Thin Controllers:** Only HTTP concerns, delegate to services immediately
2. **Fat Services:** All business logic lives here
3. **No logic in repositories:** Just data access
4. **DTOs for forms:** Don't bind directly to entities
5. **One service per domain concept:** ProjectService, ContactService

---

## Decision Summary

| Category | Decision | Key Reason |
|----------|----------|------------|
| Architecture | MVC Pattern | Simplicity, Spring's native pattern |
| Backend | Spring Boot 3.x + Java 21 | Target role relevance |
| Build Tool | Maven | Enterprise standard, already practicing |
| Frontend | Tailwind CSS + Component Libraries | Build ourselves, own the work |
| Database | PostgreSQL 16 | Industry standard, RDS free tier |
| Monitoring | Actuator + CloudWatch | Health checks, observability |
| Testing | JUnit 5 + MockMvc + JaCoCo | 70%+ coverage, professional practice |
| API | REST endpoints (`/api/projects`) | Shows API design skills |
| Deployment | AWS App Runner | Managed, simple, uses credits |
| IaC | Manual Console (no Terraform) | Right-sized for single personal site |
| CI/CD | GitHub Actions | Free, native integration |
| Code Review | CodeRabbit | Free for public, balanced feedback |
| AI Tooling | Claude Code | Modern practice, decisions still human-owned |
| Auth | GitHub OAuth | ⏸️ Phase 2 - deferred |

---

## Cost Summary

| Service | Monthly Cost | Notes |
|---------|--------------|-------|
| App Runner | $5-15 | Scales with traffic |
| RDS PostgreSQL | $0 (year 1), ~$15 after | t3.micro free tier |
| ECR | ~$1 | Container storage |
| Route 53 | $0.50 + $12/year | Hosted zone + domain |
| Secrets Manager | ~$0.40 | 1 secret |
| **Total** | **~$7-17/month** | Covered by $100 credits for 6-12 months |

---

## Revision History

| Date | ADR | Change |
|------|-----|--------|
| Jan 2025 | 001-010 | Initial decisions documented |
| Jan 2025 | 011 | Added Maven as build tool |
| Jan 2025 | 012 | Added MVC pattern decision |
| Jan 2025 | 013 | Added AI-assisted development approach |
| Jan 2025 | 014 | Added visual design / template decision |
| Jan 2025 | 015 | Added MVP-first approach, simplified scope |
| Jan 2025 | 016 | Added technical enhancements (tests, API, health, metrics) |

---

## ADR-013: AI-Assisted Development with Claude Code

**Date:** January 2025  
**Status:** Accepted

### Context
Modern development increasingly involves AI coding assistants. Need to decide whether and how to use these tools, and how to maintain code ownership and quality.

### Decision
Use **Claude Code** as a pair programming tool while maintaining full ownership of all architectural decisions, with decisions documented in ADRs before implementation.

### How We Use AI Assistance

```
┌─────────────────────────────────────────────────────────────────┐
│                    HUMAN-OWNED DECISIONS                         │
│                                                                  │
│  • Architecture (MVC, what services exist)                      │
│  • Technology choices (Spring Boot, PostgreSQL, etc.)           │
│  • Trade-off analysis (documented in ADRs)                      │
│  • Code review and acceptance                                    │
│  • Security considerations                                       │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    AI-ASSISTED EXECUTION                         │
│                                                                  │
│  • Boilerplate generation (entities, repositories)              │
│  • Template scaffolding                                         │
│  • Test generation                                              │
│  • Documentation drafting                                        │
│  • Syntax and API lookup                                        │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    HUMAN VALIDATION                              │
│                                                                  │
│  • Code review before commit                                    │
│  • Test execution and verification                              │
│  • Manual testing of features                                    │
│  • Security review                                              │
└─────────────────────────────────────────────────────────────────┘
```

### Alternatives Considered

| Approach | Pros | Cons |
|----------|------|------|
| **AI pair programming (Claude Code)** | Fast iteration, handles boilerplate, stays current | Must validate output, can hallucinate |
| **GitHub Copilot only** | Inline completions, unobtrusive | Less context-aware, no conversation |
| **No AI assistance** | Full manual control | Slower, more tedious boilerplate |
| **AI generates everything** | Fastest | No learning, no ownership, risky |

### Rationale
- **Efficiency:** AI handles repetitive boilerplate (entities, CRUD, config) faster
- **Learning:** Conversation with AI helps explore options and understand trade-offs
- **Modern practice:** Reflects how teams actually work in 2025
- **Documented ownership:** ADRs prove every decision was human-made and reasoned

### Quality Gates

Every AI-generated piece of code must pass:

1. **Understanding check:** Can I explain every line?
2. **ADR alignment:** Does it match documented decisions?
3. **Test coverage:** Are there tests (AI-generated or manual)?
4. **Security review:** No hardcoded secrets, proper validation?
5. **Code review:** Would I approve this in a PR?

### What We Document

| AI-Assisted | Human-Decided |
|-------------|---------------|
| Initial file scaffolding | Which files to create (ADR-012) |
| Entity boilerplate | Database schema design (ADR-003) |
| Controller structure | Route design (CLAUDE.md) |
| Test templates | What to test |
| Config file syntax | What config values mean |

### Tradeoffs Accepted
- Some code patterns may reflect AI style (acceptable, still readable)
- Must spend time validating AI output (faster than writing from scratch)
- AI may suggest outdated patterns (mitigated by specifying versions in CLAUDE.md)

### How to Discuss in Interviews

**Frame it as:**
> "I used Claude Code as a pair programming assistant, similar to how many teams now use Copilot. The key difference in my approach: I documented every architectural decision in ADRs *before* generating code, ensuring I own the design. The AI accelerated implementation, but every technology choice—Spring Boot, MVC pattern, App Runner deployment—was a deliberate decision I can explain and defend."

**Demonstrate ownership by:**
- Walking through any ADR
- Explaining trade-offs for any decision
- Modifying generated code on the spot if asked
- Discussing alternatives you considered

---

## ADR-014: Visual Design - Component Library Approach

**Date:** January 2025  
**Status:** Accepted

### Context
Need a visual design for the portfolio. Options range from using a full template to building from scratch.

### Decision
**Build the frontend ourselves** using Tailwind CSS component libraries for individual pieces, with professional portfolios as design inspiration.

### Why NOT Use a Full Template

| Approach | Problem |
|----------|---------|
| Full template (Simplefolio, etc.) | Can't claim frontend work in interviews |
| Copy someone's design | Looks identical to thousands of others |
| Build 100% from scratch | Time-consuming, may look amateur |

### Our Approach: Component Assembly

```
┌─────────────────────────────────────────────────────────────┐
│                    DESIGN INSPIRATION                        │
│  (Reference for layout/structure, don't copy code)          │
│                                                              │
│  • Brittany Chiang - section flow, hover effects            │
│  • Simplefolio - Hero → About → Projects → Contact          │
│  • Ian Dunkerley - minimal, single-page                     │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   COMPONENT LIBRARIES                        │
│  (Grab individual pieces, customize heavily)                │
│                                                              │
│  • HyperUI (hyperui.dev) - Hero, cards, navbars             │
│  • Preline UI (preline.co) - Full sections, dark mode       │
│  • Flowbite (flowbite.com) - Forms, buttons                 │
│  • Meraki UI (merakiui.com) - Beautiful cards               │
│  • Tailblocks (tailblocks.cc) - Ready sections              │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    YOUR CUSTOM WORK                          │
│  (This is what you own and can claim)                       │
│                                                              │
│  • Assemble components into cohesive layout                 │
│  • Customize colors, spacing, typography                    │
│  • Add hover effects and animations                         │
│  • Integrate with Thymeleaf (th:each, th:text)             │
│  • Make responsive for mobile                               │
│  • Add dark mode (optional)                                 │
└─────────────────────────────────────────────────────────────┘
```

### Component Sources by Section

| Section | Library | What to Grab |
|---------|---------|--------------|
| **Navbar** | HyperUI or Preline | Sticky header with links |
| **Hero** | Tailblocks or Preline | Name, tagline, CTA button |
| **About** | Meraki UI | Bio section with skills |
| **Project Cards** | Flowbite or HyperUI | Card grid layout |
| **How I Built This** | Custom | Timeline or accordion |
| **Contact** | Preline | Form with validation |
| **Footer** | Any | Social links, copyright |

### What Makes It YOUR Work

You must customize:
- [ ] Color scheme (not default Tailwind blue)
- [ ] Typography (font choices, sizes)
- [ ] Spacing and layout proportions
- [ ] Hover effects and transitions
- [ ] Responsive breakpoints
- [ ] Thymeleaf integration (`th:each` for projects)
- [ ] Dark mode toggle (bonus)

### Interview Talking Point

> "I built the frontend using Tailwind CSS. I studied professional portfolio designs like Brittany Chiang's for layout inspiration, then assembled individual components from libraries like HyperUI and Preline. I customized the styling, integrated everything with Thymeleaf templates for server-side rendering, and ensured mobile responsiveness. The component libraries accelerated development, but the final design, customization, and all integration work is mine."

### Rationale
- **Legitimate ownership:** You write and customize all the HTML/CSS
- **Professional quality:** Component libraries provide polished starting points
- **Time efficient:** Don't reinvent buttons and navbars
- **Learning:** You understand Tailwind deeply by customizing
- **Unique result:** Customization makes it distinctly yours

### Tradeoffs Accepted
- Takes more time than using a full template (worth it)
- Need to learn Tailwind CSS utilities (good skill to have)
- Must ensure visual consistency across components (good practice)

---

## ADR-015: MVP-First Approach

**Date:** January 2025  
**Status:** Accepted

### Context
Project scope was growing with database, admin panel, OAuth, and many features. Risked never launching.

### Decision
Launch an **MVP without a database** - static content, minimal features, then iterate.

### What's In MVP

| Feature | In MVP? |
|---------|---------|
| Home page | ✅ Yes |
| Projects page (hardcoded) | ✅ Yes |
| About page | ✅ Yes |
| "How I Built This" page | ✅ Yes |
| Contact (mailto link) | ✅ Yes |
| Database | ❌ No |
| Admin panel | ❌ No |
| GitHub OAuth | ❌ No |
| Blog | ❌ No |

### Rationale
- **Ship beats perfect:** A live simple site > a planned complex site
- **Validate first:** See if the site helps with job search before adding features
- **Reduce complexity:** No database = no RDS costs, no connection management
- **Focus on content:** The projects and story matter more than features

### Tradeoffs Accepted
- Must redeploy to update content (acceptable for infrequent updates)
- No contact form storage (mailto works fine)
- No analytics initially (can add Plausible later)

### Phase 2 (Post-Launch)
After site is live:
1. Add analytics
2. Add database + admin if needed
3. Add blog if writing content
4. Add dark mode

---

## ADR-016: Additional Technical Enhancements

**Date:** January 2025  
**Status:** Accepted

### Context
To demonstrate professional-grade development practices and show depth beyond basic CRUD, adding several enhancements that recruiters and interviewers will notice.

### Enhancements Added

| Enhancement | Effort | Impact | Why It Matters |
|-------------|--------|--------|----------------|
| Health endpoint (`/actuator/health`) | 10 min | High | Industry standard, shows ops awareness |
| Unit + integration tests | 2-3 hrs | Very High | Non-negotiable for enterprise roles |
| REST API (`/api/projects`) | 30 min | High | Shows you can build APIs, not just views |
| CloudWatch logging/metrics | 1-2 hrs | Medium | Demonstrates observability thinking |

### Implementation Details

**1. Health Check Endpoint**
```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      show-details: when_authorized
```
- Exposes `/actuator/health` for load balancer checks
- App Runner uses this for deployment health
- Shows understanding of production operations

**2. Unit + Integration Tests**
```
src/test/java/
├── controller/
│   ├── HomeControllerTest.java      # MockMvc tests
│   └── ProjectControllerTest.java
├── service/
│   └── ProjectServiceTest.java      # Unit tests
└── integration/
    └── ProjectApiIntegrationTest.java  # @SpringBootTest
```
- Aim for 70%+ coverage on business logic
- Integration tests verify full request/response cycle
- Shows testing discipline

**3. REST API for Projects**
```java
@RestController
@RequestMapping("/api")
public class ProjectApiController {
    
    @GetMapping("/projects")
    public List<ProjectDto> getProjects() { ... }
    
    @GetMapping("/projects/{slug}")
    public ProjectDto getProject(@PathVariable String slug) { ... }
}
```
- Returns JSON, not HTML
- Shows you can build APIs for frontend teams
- Enables future React/mobile client

**4. CloudWatch Integration**
```xml
<!-- Add to pom.xml -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-cloudwatch2</artifactId>
</dependency>
```
- Structured logging with correlation IDs
- Request latency metrics
- Error rate tracking
- Shows observability mindset

### Interview Talking Points

**"How do you ensure code quality?"**
→ "I write unit tests for business logic and integration tests for API endpoints. This project has 70%+ coverage with JUnit 5 and MockMvc."

**"How do you monitor applications in production?"**
→ "I integrated CloudWatch for metrics and structured logging. The health endpoint at /actuator/health is used by App Runner for deployment verification."

**"Can you build REST APIs?"**
→ "Yes, this project has both server-rendered views AND a JSON API at /api/projects. I designed it to support future mobile or SPA clients."
