# Portfolio Project: Your Manual Tasks

> Everything Claude Code **cannot** do for you, organized by when to do it.

---

## 🟡 BEFORE Starting Claude Code

### Install Prerequisites

| Tool | Check Command | Install Link |
|------|---------------|--------------|
| Java 21 | `java -version` | https://adoptium.net/ |
| Docker Desktop | `docker --version` | https://docker.com/products/docker-desktop |
| Maven | `mvn -version` | https://maven.apache.org/download.cgi |

### Gather Your Content

```
□ Your photo (professional headshot)
  → File: headshot.jpg or headshot.png
  → Tip: Good lighting, plain background

□ Project screenshots:
  □ LMS screenshot (lms-screenshot.png)
  □ Cloud project screenshot (cloud-screenshot.png)  
  □ VEX robotics screenshot (vex-screenshot.png)
  □ Any other projects you want to showcase

□ Write in a text file (Claude Code will use these):
  □ 2-3 sentence bio about yourself
  □ Real description for each project (2-3 sentences each)
  □ Your actual GitHub URLs
  □ LinkedIn URL
  □ Email for contact form
```

### Estimated Time: 1-2 hours

---

## 🟢 DURING Development

### After Phase 1 Works Locally

**Create GitHub Repository**
1. Go to https://github.com/new
2. Name: `cesar-portfolio`
3. Set to **Public** (required for free CodeRabbit)
4. Do NOT initialize with README (you already have files)

**Push Your Code**
```bash
git init
git add .
git commit -m "Initial commit"
git remote add origin git@github.com:YOUR_USERNAME/cesar-portfolio.git
git push -u origin main
```

**Enable CodeRabbit**
1. Go to https://coderabbit.ai
2. Click "Sign in with GitHub"
3. Authorize the app
4. Enable on your `cesar-portfolio` repository
5. Done - it will auto-review all future PRs

### During Phase 4 (Frontend)

**Design Decisions Only You Can Make:**
- [ ] Pick primary color (not default Tailwind blue)
- [ ] Pick accent color
- [ ] Choose fonts (or keep defaults)
- [ ] Review layout on each page
- [ ] Approve final design before moving on

### Throughout All Phases

- [ ] Review code Claude Code generates
- [ ] Test features as they're built
- [ ] Provide feedback on anything that looks wrong

---

## 🔴 AFTER MVP Complete

### AWS Infrastructure Setup

#### 1. Create RDS PostgreSQL Database
```
AWS Console → RDS → Create database

Settings:
  □ Engine: PostgreSQL 16
  □ Template: Free tier
  □ DB instance identifier: cesar-portfolio-db
  □ Master username: postgres
  □ Master password: [CREATE A STRONG PASSWORD]
  □ Public access: Yes (for initial setup)
  □ VPC security group: Create new
  
Save these values:
  → Endpoint: ________________________________
  → Port: 5432
  → Username: postgres
  → Password: ________________________________
```

#### 2. Create ECR Repository
```
AWS Console → ECR → Create repository

Settings:
  □ Repository name: cesar-portfolio
  □ Image tag mutability: Mutable
  
Save this value:
  → Repository URI: ________________________________
```

#### 3. Set Up Secrets Manager
```
AWS Console → Secrets Manager → Store a new secret

Secret type: Other type of secret

Key/Value pairs:
  □ DATABASE_URL: jdbc:postgresql://[RDS_ENDPOINT]:5432/portfolio
  □ DATABASE_USERNAME: postgres
  □ DATABASE_PASSWORD: [YOUR_RDS_PASSWORD]

Secret name: cesar-portfolio/prod
```

#### 4. Create App Runner Service
```
AWS Console → App Runner → Create service

Source:
  □ Container registry: Amazon ECR
  □ Browse → Select cesar-portfolio repository
  
Deployment settings:
  □ Automatic deployment: Yes
  
Configure service:
  □ Service name: cesar-portfolio
  □ Port: 8080
  
Environment variables:
  □ SPRING_PROFILES_ACTIVE: prod
  □ DATABASE_URL: [from Secrets Manager]
  □ DATABASE_USERNAME: [from Secrets Manager]
  □ DATABASE_PASSWORD: [from Secrets Manager]
```

#### 5. Add GitHub Actions Secrets
```
GitHub → Your Repo → Settings → Secrets and variables → Actions

Add these secrets:
  □ AWS_ACCESS_KEY_ID: [Your AWS access key]
  □ AWS_SECRET_ACCESS_KEY: [Your AWS secret key]
  □ AWS_REGION: us-east-1 (or your region)
  □ ECR_REPOSITORY: [Your ECR repository URI]
```

### Domain Setup

#### Buy Domain
- **Option A:** AWS Route 53 (easy DNS integration)
- **Option B:** Namecheap, Google Domains, etc.
- **Suggested:** cesarvillarreal.dev (~$12/year)

#### Configure DNS
```
If using Route 53:
  □ Create hosted zone for your domain
  □ Create CNAME record pointing to App Runner URL

If using external registrar:
  □ Add CNAME record: @ → [App Runner URL]
  □ Or configure nameservers to Route 53
```

### Estimated Time: 1-2 hours

---

## Final Launch Checklist

```
□ Site loads at your custom domain
□ All pages render correctly
□ Projects display with real screenshots
□ Contact form submits successfully
□ Mobile responsive (test on real phone)
□ GitHub Actions deploys on push to main
□ CodeRabbit reviews PRs

□ Share with 2-3 friends for feedback
□ Fix any issues they find
□ Add to resume/LinkedIn
```

---

## Timeline Overview

| Day | Who Does What |
|-----|---------------|
| **Day 0** | **You:** Install tools, gather content |
| **Days 1-2** | Claude Code: Foundation + Database |
| **Days 2-3** | Claude Code: Controllers + Templates |
| **Days 3-5** | Claude Code: Frontend (you pick colors) |
| **Day 5** | **You:** Create GitHub repo, enable CodeRabbit |
| **Days 5-6** | Claude Code: Tests + Polish |
| **Day 6-7** | **You:** AWS setup, domain |
| **Day 7** | **You:** Launch, test, share 🚀 |

---

## Quick Commands Reference

```bash
# Start local database
docker-compose up -d

# Run the app locally
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Run tests
mvn test

# Build for production
mvn clean package

# Build Docker image
docker build -t cesar-portfolio .

# Check database
docker-compose exec postgres psql -U dev -d portfolio
```

---

## Cost Summary

| Service | Monthly Cost |
|---------|--------------|
| App Runner | $5-15 |
| RDS PostgreSQL | Free tier year 1, then ~$15 |
| ECR | ~$1 |
| Route 53 | $0.50 |
| Domain | ~$1 (annual ÷ 12) |
| **Total** | **~$7-17/month** |

Your $100 AWS credits cover 6-12 months.

---

## Need Help?

- **AWS Setup:** https://docs.aws.amazon.com/
- **CodeRabbit:** https://docs.coderabbit.ai/
- **Spring Boot:** https://docs.spring.io/spring-boot/
- **Tailwind CSS:** https://tailwindcss.com/docs
