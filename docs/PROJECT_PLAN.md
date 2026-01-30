# Personal Website Project Plan
## Tech Stack: Spring Boot + Thymeleaf + PostgreSQL + AWS

---

## Phase 1: Project Setup & Local Development Environment
**Duration: 1-2 hours**

### 1.1 Prerequisites
- [ ] Java 21 (Amazon Corretto recommended for AWS)
- [ ] Gradle 8.x
- [ ] Docker Desktop
- [ ] IntelliJ IDEA or VS Code with Spring extensions
- [ ] Git

### 1.2 Initialize Spring Boot Project
**Dependencies to include:**
- Spring Web
- Spring Data JPA
- Spring Security
- Thymeleaf
- PostgreSQL Driver
- Spring Boot DevTools
- Lombok
- Validation

**Build Tool:** Maven (pom.xml)

### 1.3 Project Structure
```
cesar-portfolio/
├── src/main/java/com/cesarguerra/portfolio/
│   ├── PortfolioApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   └── WebConfig.java
│   ├── controller/
│   │   ├── HomeController.java
│   │   ├── ProjectController.java
│   │   └── ContactController.java
│   ├── service/
│   │   ├── ProjectService.java
│   │   └── ContactService.java
│   ├── repository/
│   │   ├── ProjectRepository.java
│   │   └── MessageRepository.java
│   ├── model/
│   │   ├── Project.java
│   │   └── ContactMessage.java
│   └── dto/
│       └── ContactForm.java
├── src/main/resources/
│   ├── templates/
│   │   ├── layout/
│   │   │   └── base.html
│   │   ├── index.html
│   │   ├── projects.html
│   │   ├── about.html
│   │   └── contact.html
│   ├── static/
│   │   ├── css/
│   │   ├── js/
│   │   └── images/
│   └── application.yml
├── src/test/java/
├── docker-compose.yml          # Local PostgreSQL
├── Dockerfile
├── build.gradle
└── README.md
```

### 1.4 Docker Compose for Local Dev
Set up PostgreSQL locally without installing it:
```yaml
services:
  postgres:
    image: postgres:16
    environment:
      POSTGRES_DB: portfolio
      POSTGRES_USER: dev
      POSTGRES_PASSWORD: devpassword
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
```

### 1.5 Deliverables
- [ ] Project compiles and runs locally
- [ ] Can connect to local PostgreSQL
- [ ] Basic "Hello World" page renders

---

## Phase 2: Core Pages & Features
**Duration: 3-4 hours**

### 2.1 Thymeleaf Layout System
Create a reusable base template with:
- Navigation header
- Footer
- Meta tags for SEO

### 2.2 Pages to Build

| Page | Route | Features |
|------|-------|----------|
| Home | `/` | Hero section, brief intro, featured projects |
| About | `/about` | Bio, skills, experience timeline |
| Projects | `/projects` | Grid of projects with filters |
| Project Detail | `/projects/{slug}` | Full project description, tech stack, links |
| Contact | `/contact` | Contact form |
| Resume | `/resume` | Downloadable PDF + web version |

### 2.3 Database Models

**Project Entity:**
```java
- id (Long)
- title (String)
- slug (String) 
- description (Text)
- techStack (List<String>)
- githubUrl (String)
- liveUrl (String)
- imageUrl (String)
- featured (Boolean)
- createdAt (LocalDateTime)
```

**ContactMessage Entity:**
```java
- id (Long)
- name (String)
- email (String)
- message (Text)
- createdAt (LocalDateTime)
- read (Boolean)
```

### 2.4 Styling Approach
**Option A: Tailwind CSS** (via CDN for simplicity)
**Option B: Bootstrap 5** (quick, professional look)
**Option C: Custom CSS** (full control, more work)

**Recommendation:** Tailwind CSS via CDN - modern look, minimal setup

### 2.5 Deliverables
- [ ] All pages render with placeholder content
- [ ] Navigation works between pages
- [ ] Projects load from database
- [ ] Contact form submits (stores in DB)
- [ ] Responsive design works on mobile

---

## Phase 3: Authentication (Optional Admin Panel)
**Duration: 2-3 hours**

### 3.1 Why Authentication?
You might want a simple admin panel to:
- Add/edit projects without redeploying
- View contact form submissions
- Mark messages as read

### 3.2 Implementation: Spring Security + OAuth2

**Option A: GitHub OAuth (Recommended)**
- No passwords to manage
- Shows you know OAuth2
- Free

**Option B: Simple username/password**
- Easier to set up
- Less impressive for interviews

### 3.3 Security Config
```java
- Public routes: /, /about, /projects/**, /contact, /resume
- Protected routes: /admin/**
- OAuth2 login with GitHub
```

### 3.4 Admin Features
| Feature | Route |
|---------|-------|
| Dashboard | `/admin` |
| Manage Projects | `/admin/projects` |
| View Messages | `/admin/messages` |

### 3.5 Deliverables
- [ ] GitHub OAuth login works
- [ ] Admin can add/edit/delete projects
- [ ] Admin can view contact messages
- [ ] Proper role-based access control

---

## Phase 4: Containerization & AWS Prep
**Duration: 2-3 hours**

### 4.1 Dockerfile
```dockerfile
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 4.2 Environment Configuration
Create profiles:
- `application.yml` - defaults
- `application-dev.yml` - local development
- `application-prod.yml` - production (AWS)

### 4.3 AWS Resources Needed
| Resource | Service | Purpose |
|----------|---------|---------|
| Compute | App Runner | Runs your container |
| Database | RDS PostgreSQL | Production database |
| Domain | Route 53 | DNS management |
| SSL | ACM | Free HTTPS certificate |
| Secrets | Secrets Manager | DB credentials, OAuth secrets |
| Container Registry | ECR | Store Docker images |

### 4.4 Deliverables
- [ ] Docker image builds successfully
- [ ] App runs in container locally
- [ ] Environment variables externalized
- [ ] No secrets in code

---

## Phase 5: AWS Deployment
**Duration: 3-4 hours**

### 5.1 Initial AWS Setup
1. Create ECR repository
2. Push Docker image to ECR
3. Create RDS PostgreSQL instance (t3.micro - free tier)
4. Set up Secrets Manager for credentials
5. Create App Runner service

### 5.2 Infrastructure as Code (Optional but Impressive)
Use Terraform to define:
- ECR repository
- RDS instance
- App Runner service
- Route 53 records
- ACM certificate

### 5.3 Domain Setup
1. Register domain in Route 53 (~$12/year for .com)
2. Request ACM certificate
3. Configure App Runner custom domain
4. Set up DNS records

### 5.4 Cost Estimate (Monthly)
| Service | Estimated Cost |
|---------|---------------|
| App Runner | $5-15 |
| RDS t3.micro | Free (12 months) |
| Route 53 | $0.50 + $12/year domain |
| ECR | ~$1 |
| Secrets Manager | ~$0.40 |
| **Total** | **~$7-17/month** |

Your $100 credits cover 6-12 months easily.

### 5.5 Deliverables
- [ ] Site live at your domain
- [ ] HTTPS working
- [ ] Database connected
- [ ] Secrets properly managed

---

## Phase 6: CI/CD Pipeline
**Duration: 2-3 hours**

### 6.1 GitHub Actions Workflow

**On push to `main`:**
1. Run tests
2. Build application
3. Build Docker image
4. Push to ECR
5. Deploy to App Runner

### 6.2 Workflow File Structure
```
.github/
└── workflows/
    ├── ci.yml          # Run on all PRs
    └── deploy.yml      # Run on main branch
```

### 6.3 Branch Strategy
- `main` - production (auto-deploys)
- `develop` - integration branch
- `feature/*` - feature branches

### 6.4 Deliverables
- [ ] Tests run on every PR
- [ ] Automatic deployment on merge to main
- [ ] Build status badges in README

---

## Phase 7: Code Review & Quality
**Duration: 1 hour**

### 7.1 Set Up CodeRabbit
1. Make repository public (for free tier)
2. Install CodeRabbit GitHub App
3. Configure review settings

### 7.2 Additional Quality Tools
| Tool | Purpose |
|------|---------|
| CodeRabbit | AI code review |
| SonarCloud | Code quality metrics (free for public repos) |
| Dependabot | Dependency updates |
| GitHub Actions | Run Checkstyle/SpotBugs |

### 7.3 Deliverables
- [ ] CodeRabbit reviewing PRs
- [ ] Quality gates passing
- [ ] No critical security issues

---

## Phase 8: Final Polish & Launch
**Duration: 2-3 hours**

### 8.1 Content
- [ ] Write real project descriptions
- [ ] Add actual screenshots
- [ ] Update resume/bio
- [ ] Add professional headshot

### 8.2 SEO & Performance
- [ ] Add meta tags (title, description, Open Graph)
- [ ] Optimize images
- [ ] Test page speed (Lighthouse)
- [ ] Add sitemap.xml
- [ ] Add robots.txt

### 8.3 Analytics (Optional)
- Google Analytics or Plausible (privacy-friendly)

### 8.4 Final Checklist
- [ ] All links work
- [ ] Mobile responsive
- [ ] Contact form delivers messages
- [ ] 404 page exists
- [ ] Favicon added

---

## Timeline Summary

| Phase | Task | Time |
|-------|------|------|
| 1 | Project Setup | 1-2 hrs |
| 2 | Core Pages | 3-4 hrs |
| 3 | Authentication | 2-3 hrs |
| 4 | Containerization | 2-3 hrs |
| 5 | AWS Deployment | 3-4 hrs |
| 6 | CI/CD Pipeline | 2-3 hrs |
| 7 | Code Review Setup | 1 hr |
| 8 | Polish & Launch | 2-3 hrs |
| **Total** | | **16-23 hrs** |

Spread over 2-3 weekends or 1-2 weeks of evening work.

---

## Next Steps

Ready to start? Here's the order:
1. **Now:** I'll generate the initial Spring Boot project files
2. **You:** Set up prerequisites (Java 21, Docker, IDE)
3. **Together:** Build out each phase iteratively

Let me know when you're ready to begin Phase 1!
