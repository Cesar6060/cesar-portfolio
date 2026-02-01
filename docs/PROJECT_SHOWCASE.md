# Project Showcase: Personal Portfolio Website

> Use this document to describe this project on your portfolio. Update as you build.

---

## One-Liner
A production-grade personal portfolio built with Spring Boot, deployed on AWS with Terraform-managed infrastructure and automated CI/CD.

---

## The Problem
Needed a personal website to showcase projects and skills for cloud engineering roles, but wanted it to demonstrate the same enterprise-grade practices I'd use professionally—not just a quick static site.

---

## The Solution
Built a full-stack portfolio website using:
- **Spring Boot** backend with server-side rendering
- **PostgreSQL** database for dynamic content management
- **AWS App Runner** for serverless container deployment
- **Terraform** for reproducible infrastructure
- **GitHub Actions** for automated testing and deployment

---

## Key Technical Decisions

### Why Spring Boot for a "Simple" Portfolio?
Could have used a static site generator, but chose Spring Boot to:
- Practice the stack used in target roles (enterprise Java)
- Demonstrate I can make pragmatic technology choices
- Enable dynamic features (admin panel, contact form)
- Show I understand the full deployment lifecycle

### Why Terraform Instead of Manual AWS Setup?
- Infrastructure is code, reviewable, version-controlled
- Reproducible across environments
- Demonstrates IaC skills valuable for consulting
- Can open-source the modules for others

### Why Server-Side Rendering Over React?
- Portfolio sites are content-focused, not interaction-heavy
- Better SEO out of the box
- Single deployable artifact simplifies operations
- Shows I choose appropriate tools (not everything needs a SPA)

---

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Route 53                              │
│                    (DNS + SSL via ACM)                       │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      AWS App Runner                          │
│              (Auto-scaling container runtime)                │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Spring Boot Application                 │   │
│  │  ┌─────────┐  ┌──────────┐  ┌─────────────────┐    │   │
│  │  │ Thyme-  │  │  Spring  │  │  Spring Data    │    │   │
│  │  │  leaf   │◄─│ Security │◄─│     JPA         │    │   │
│  │  │ Views   │  │ (OAuth2) │  │                 │    │   │
│  │  └─────────┘  └──────────┘  └────────┬────────┘    │   │
│  └──────────────────────────────────────┼──────────────┘   │
└─────────────────────────────────────────┼───────────────────┘
                                          │
                                          ▼
                    ┌─────────────────────────────────────┐
                    │          RDS PostgreSQL              │
                    │            (t3.micro)                │
                    └─────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                    GitHub Actions CI/CD                      │
│  PR ──► Test ──► Build ──► ECR Push ──► App Runner Deploy   │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                    Terraform (IaC)                           │
│  ECR │ App Runner │ RDS │ Route53 │ Secrets Manager         │
└─────────────────────────────────────────────────────────────┘
```

---

## Features

### Public
- **Home** - Hero section, featured projects, quick intro
- **Projects** - Filterable grid of work with tech stack tags
- **About** - Background, skills, experience timeline
- **Contact** - Form that stores submissions in database
- **Resume** - Web version with PDF download

### Admin (GitHub OAuth protected)
- Add/edit/delete projects without redeploying
- View and manage contact form submissions
- Simple dashboard with site stats

---

## Tech Stack

| Category | Technology | Rationale |
|----------|------------|-----------|
| Language | Java 21 | Modern features, enterprise relevance |
| Framework | Spring Boot 3.2 | Industry standard, excellent ecosystem |
| Templating | Thymeleaf | SSR, great Spring integration |
| Styling | Tailwind CSS | Utility-first, modern look, CDN = no build step |
| Database | PostgreSQL 16 | Robust, RDS free tier |
| Auth | Spring Security + GitHub OAuth | Secure, no password management |
| Container | Docker | Consistent environments |
| Registry | AWS ECR | Native AWS integration |
| Compute | AWS App Runner | Managed containers, auto-scaling |
| IaC | Terraform | Industry standard, reproducible |
| CI/CD | GitHub Actions | Native integration, generous free tier |
| Code Review | CodeRabbit | AI-powered, free for public repos |

---

## Metrics & Results

| Metric | Value |
|--------|-------|
| Lighthouse Performance | XX/100 |
| Time to First Byte | XXms |
| Monthly AWS Cost | ~$X |
| Deployment Time | X minutes |
| Test Coverage | XX% |

*(Update these after deployment)*

---

## What I Learned

### Technical
- Optimizing Spring Boot startup for container environments
- Terraform module design for reusability
- GitHub Actions secrets management for AWS deployment
- OAuth2 configuration in Spring Security 6

### Process
- Value of documenting decisions as you make them (ADRs)
- How "simple" projects become complex teaching tools
- Trade-offs between convenience and demonstration value

---

## If I Were to Do It Again

- **Would keep:** Terraform IaC, GitHub Actions, ADR documentation
- **Would consider:** Using HTMX for sprinkles of interactivity without full SPA
- **Would skip:** [Add lessons learned here]

---

## Links

- **Live Site:** https://cesarvillarreal.dev *(update)*
- **GitHub Repo:** https://github.com/cesarvillarreal/portfolio *(update)*
- **Terraform Modules:** /terraform in repo

---

## Talking Points for Interviews

1. **"Walk me through a technical decision you made recently"**
   - Use ADR-001 (Spring Boot choice) or ADR-005 (App Runner vs alternatives)

2. **"How do you approach infrastructure?"**
   - Explain Terraform module structure, state management, reproducibility

3. **"Tell me about your CI/CD experience"**
   - Walk through the GitHub Actions pipeline, testing strategy, deployment

4. **"How do you document your work?"**
   - Show the CLAUDE.md and ARCHITECTURE_DECISIONS.md approach

5. **"What would you do differently at scale?"**
   - Discuss moving to ECS for more control, adding CDN, database read replicas
