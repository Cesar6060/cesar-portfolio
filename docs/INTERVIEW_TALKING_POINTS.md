# Portfolio Project - Interview Talking Points

> Use this guide when discussing your portfolio with recruiters, hiring managers, or in technical interviews.

---

## 30-Second Elevator Pitch

"I built a full-stack portfolio website using Spring Boot and Java 21, deployed on AWS with a complete CI/CD pipeline. It's not just a static site—it has a PostgreSQL database, email notifications via AWS SES, and automated deployments through GitHub Actions. I chose this stack intentionally to demonstrate enterprise-level Java skills that translate directly to consulting and cloud engineering roles."

---

## Project Overview

| Aspect | Details |
|--------|---------|
| **Live URL** | https://cesarvillarreal.dev |
| **GitHub** | https://github.com/Cesar6060/cesar-portfolio |
| **Stack** | Spring Boot 3.2, Java 21, Thymeleaf, PostgreSQL, AWS |
| **Infrastructure** | App Runner, RDS, ECR, SES, Cloudflare |
| **CI/CD** | GitHub Actions (test → build → deploy) |

---

## Key Technical Decisions & Why

### 1. Why Spring Boot over Node.js or Python?

**Decision:** Java 21 with Spring Boot 3.2

**Why:**
- Target roles (cloud engineering, consulting) heavily use Java/Spring in enterprise environments
- Demonstrates I can work in statically-typed, enterprise ecosystems
- Spring Boot is the industry standard for production Java applications
- Shows familiarity with dependency injection, JPA, and enterprise patterns

**What to say:**
> "I specifically chose Spring Boot because my target roles at consulting firms and enterprises use Java heavily. I wanted to demonstrate that I'm comfortable with statically-typed languages, dependency injection, and enterprise patterns—not just scripting languages."

---

### 2. Why Server-Side Rendering (Thymeleaf) over React SPA?

**Decision:** Thymeleaf templates instead of React/Vue frontend

**Why:**
- Single deployment unit (no separate frontend/backend)
- Better SEO out of the box
- Simpler architecture for a content-focused site
- Demonstrates I understand when NOT to over-engineer

**What to say:**
> "For a portfolio site, an SPA would be over-engineering. Thymeleaf gives me server-side rendering for SEO, a single deployment, and faster initial page loads. I know React well, but part of good engineering is choosing the right tool for the job."

---

### 3. Why PostgreSQL over MongoDB or SQLite?

**Decision:** PostgreSQL with AWS RDS

**Why:**
- Industry standard relational database
- ACID compliance for data integrity
- Rich querying capabilities
- Managed service (RDS) reduces operational burden
- Free tier eligible for cost optimization

**What to say:**
> "PostgreSQL is the industry standard for relational data. Using RDS means I get automated backups, patching, and high availability without managing infrastructure. For a portfolio with structured data like projects and contact messages, relational makes more sense than document stores."

---

### 4. Why AWS App Runner over EC2, ECS, or Lambda?

**Decision:** AWS App Runner for container hosting

**Why:**
- Fully managed—no cluster management like ECS
- Auto-scaling built in (scales to zero when idle)
- Simpler than Kubernetes for a single-service application
- Direct ECR integration for container deployments
- Cost-effective for low-traffic applications

**What to say:**
> "App Runner hits the sweet spot between control and simplicity. EC2 requires too much server management, ECS needs cluster configuration, and Lambda would require significant refactoring. App Runner gives me containers with auto-scaling and zero infrastructure management."

**Trade-offs acknowledged:**
- Less control than ECS/EKS
- Newer service with fewer features
- Vendor lock-in to AWS

---

### 5. Why Flyway for Database Migrations?

**Decision:** Flyway over Liquibase or manual scripts

**Why:**
- Version-controlled database changes
- Automatic migration on application startup
- SQL-based (no XML configuration)
- Industry standard in Spring ecosystem

**What to say:**
> "Database migrations are versioned just like code. Flyway runs automatically on deploy, so my schema always matches my application version. This prevents the 'it works on my machine' problem for database changes."

---

### 6. Why Cloudflare over Route 53?

**Decision:** Cloudflare for DNS and SSL

**Why:**
- Free SSL certificates
- Free DDoS protection
- Global CDN for static assets
- Faster DNS propagation than Route 53
- Cost savings (Route 53 charges per query)

**What to say:**
> "Cloudflare gives me SSL, DDoS protection, and CDN caching for free. Route 53 would work but adds cost without additional benefit for this use case. I'm using Cloudflare as a proxy in front of App Runner."

---

### 7. Why GitHub Actions over Jenkins or CircleCI?

**Decision:** GitHub Actions for CI/CD

**Why:**
- Native GitHub integration
- Free for public repositories
- YAML-based configuration (infrastructure as code)
- Marketplace actions for common tasks
- No separate CI server to maintain

**What to say:**
> "GitHub Actions is free, lives where my code is, and has excellent AWS integration. Jenkins would require a separate server to maintain. My pipeline runs tests, builds a Docker image, pushes to ECR, and App Runner auto-deploys—fully automated."

---

### 8. Why AWS SES for Email?

**Decision:** AWS SES for contact form notifications

**Why:**
- Already in AWS ecosystem
- 62,000 free emails/month
- Simple SDK integration
- Production-grade deliverability

**What to say:**
> "When someone submits the contact form, I get an email notification instantly. SES integrates cleanly with my existing AWS infrastructure and the SDK made implementation straightforward. I added proper IAM roles so the App Runner service can send emails securely."

---

## Architecture Walkthrough

```
User Request → Cloudflare (SSL/CDN) → AWS App Runner → Spring Boot App → PostgreSQL (RDS)
                                                              ↓
                                                         AWS SES (email)
```

**Deployment Flow:**
```
git push → GitHub Actions → Run Tests → Build Docker Image → Push to ECR → App Runner Auto-Deploy
```

**Key Points:**
- Zero-downtime deployments (App Runner handles rolling updates)
- Infrastructure as code (Dockerfiles, GitHub Actions YAML)
- Separation of concerns (Cloudflare for edge, App Runner for compute, RDS for data)

---

## Challenges & Solutions

### Challenge 1: Flyway Migration Compatibility

**Problem:** Used PostgreSQL-specific syntax (`unnest(ARRAY[])`) that broke H2 tests

**Solution:** Disabled Flyway in test profile, used Hibernate's `ddl-auto: create-drop` for tests instead

**What to say:**
> "I ran into a compatibility issue where my production migrations used PostgreSQL-specific syntax that H2 couldn't handle. Rather than dumbing down my migrations, I configured separate strategies—Flyway for production, Hibernate auto-DDL for tests. This keeps production migrations clean while tests remain fast."

---

### Challenge 2: App Runner IAM Permissions for SES

**Problem:** App Runner service couldn't send emails—no IAM role configured

**Solution:** Created dedicated IAM role with SES permissions, attached to App Runner instance configuration

**What to say:**
> "App Runner instances don't have AWS permissions by default. I had to create an IAM role with SES send permissions and attach it to the service. This follows the principle of least privilege—the service only has the permissions it needs."

---

### Challenge 3: Cloudflare + App Runner Custom Domain

**Problem:** Custom domain returning 404 errors

**Solution:** Configured App Runner custom domain association, added certificate validation CNAME records to Cloudflare (DNS-only mode for validation records)

**What to say:**
> "Getting the custom domain working required understanding how App Runner validates domain ownership through DNS. The validation CNAME records needed to bypass Cloudflare's proxy to work correctly."

---

## Cost Optimization Decisions

| Service | Configuration | Monthly Cost |
|---------|--------------|--------------|
| App Runner | 0.25 vCPU, 512MB RAM | ~$5-10 |
| RDS | db.t3.micro, 20GB | ~$15 (free tier eligible) |
| ECR | <1GB storage | <$1 |
| SES | <1000 emails | Free |
| Cloudflare | Free tier | $0 |
| **Total** | | **~$20-25/month** |

**What to say:**
> "I optimized for cost while maintaining production quality. App Runner scales to zero when idle, RDS uses the smallest instance, and Cloudflare provides free SSL and CDN. The whole infrastructure runs under $25/month, and I set up AWS Budget alerts to monitor spending."

---

## Questions You Might Get Asked

### "Why not just use a static site generator?"

> "A static site would work for content, but I wanted to demonstrate full-stack capabilities. The contact form saves to a database and sends real email notifications. I also have a REST API that could power a mobile app in the future. Plus, this architecture mirrors what I'd build in a real enterprise environment."

### "How would this scale?"

> "App Runner auto-scales horizontally based on traffic—I configured 1-25 instances. RDS can scale vertically or add read replicas. For high traffic, I'd add CloudFront for static asset caching and potentially move to Aurora Serverless for the database."

### "What would you do differently?"

> "If I were rebuilding, I might explore AWS CDK for infrastructure as code instead of manual CLI setup. I'd also add monitoring with CloudWatch dashboards from day one. The current setup works well, but observability could be stronger."

### "How do you handle security?"

> "Multiple layers: Cloudflare provides DDoS protection and WAF capabilities, HTTPS everywhere, database credentials in environment variables (never in code), IAM roles with least privilege, and input validation on all forms. I also use Greptile for automated code review on every PR."

### "Tell me about your testing strategy"

> "I have unit tests with JUnit 5 and MockMvc for controller integration tests. Tests run in CI before any merge to main. I use H2 in-memory database for fast test execution while production uses PostgreSQL. Code coverage is tracked with JaCoCo."

---

## Closing Statement

> "This project demonstrates that I can architect, build, and deploy a complete application using enterprise technologies. I made deliberate technology choices based on the requirements, optimized for cost and maintainability, and set up proper CI/CD practices. I'm ready to apply these same principles to larger-scale enterprise systems."

---

## Quick Reference: Tech Stack Justifications

| Technology | One-Line Justification |
|------------|----------------------|
| Spring Boot | Enterprise standard, target role alignment |
| Java 21 | Latest LTS, modern features |
| Thymeleaf | SSR for SEO, single deployment |
| PostgreSQL | Industry standard RDBMS |
| Flyway | Version-controlled migrations |
| AWS App Runner | Managed containers, auto-scaling |
| AWS RDS | Managed database, automated backups |
| AWS SES | Cost-effective email delivery |
| Docker | Consistent environments, portable |
| GitHub Actions | Free CI/CD, native integration |
| Cloudflare | Free SSL, CDN, DDoS protection |
| Tailwind CSS | Rapid UI development |
| JUnit 5 | Standard Java testing |
| Greptile | AI-powered code review |
