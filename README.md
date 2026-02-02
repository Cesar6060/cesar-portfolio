# Cesar Villarreal - Portfolio

[![CI](https://github.com/Cesar6060/cesar-portfolio/actions/workflows/ci.yml/badge.svg)](https://github.com/Cesar6060/cesar-portfolio/actions/workflows/ci.yml)
[![Deploy](https://github.com/Cesar6060/cesar-portfolio/actions/workflows/deploy.yml/badge.svg)](https://github.com/Cesar6060/cesar-portfolio/actions/workflows/deploy.yml)

**Live Site:** [cesarvillarreal.dev](https://cesarvillarreal.dev)

A full-stack portfolio website built with enterprise Java technologies, demonstrating cloud architecture, CI/CD best practices, and production-grade infrastructure.

---

## Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                            INTERNET                                  │
└─────────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────────┐
│                     CLOUDFLARE (DNS + SSL + CDN)                     │
│                                                                      │
│   • Free SSL/TLS encryption                                         │
│   • DDoS protection                                                  │
│   • Global CDN for static assets                                    │
└─────────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────────┐
│                           AWS CLOUD                                  │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────┐    │
│  │                    AWS App Runner                           │    │
│  │                                                             │    │
│  │   ┌─────────────────────────────────────────────────────┐  │    │
│  │   │            Spring Boot Application                   │  │    │
│  │   │                                                      │  │    │
│  │   │  • Java 21 + Spring Boot 3.2                        │  │    │
│  │   │  • Thymeleaf server-side rendering                  │  │    │
│  │   │  • REST API endpoints                                │  │    │
│  │   │  • Flyway database migrations                       │  │    │
│  │   └─────────────────────────────────────────────────────┘  │    │
│  │                                                             │    │
│  │   Auto-scaling: 1-25 instances                             │    │
│  │   CPU: 0.25 vCPU | Memory: 512 MB                          │    │
│  └────────────────────────────────────────────────────────────┘    │
│                          │                    │                     │
│                          ▼                    ▼                     │
│  ┌──────────────────────────────┐  ┌─────────────────────────┐    │
│  │         AWS RDS              │  │       AWS SES           │    │
│  │                              │  │                         │    │
│  │  PostgreSQL 16               │  │  Email notifications    │    │
│  │  db.t3.micro                 │  │  Contact form alerts    │    │
│  │  20 GB storage               │  │                         │    │
│  └──────────────────────────────┘  └─────────────────────────┘    │
│                                                                      │
│  ┌──────────────────────────────┐                                   │
│  │         AWS ECR              │                                   │
│  │                              │                                   │
│  │  Docker image repository     │                                   │
│  │  Auto-deploy on push         │                                   │
│  └──────────────────────────────┘                                   │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Tech Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 (LTS) | Runtime |
| Spring Boot | 3.2.1 | Application framework |
| Spring Data JPA | - | Database ORM |
| Thymeleaf | - | Server-side templating |
| Flyway | 10.4.1 | Database migrations |

### Frontend
| Technology | Purpose |
|------------|---------|
| Thymeleaf | HTML templates with layout dialect |
| Tailwind CSS | Utility-first styling (CDN) |
| Vanilla JS | Minimal interactivity |

### Database
| Technology | Environment |
|------------|-------------|
| PostgreSQL 16 | Production (AWS RDS) |
| PostgreSQL 16 | Development (Docker) |
| H2 | Testing (in-memory) |

### Infrastructure
| Service | Purpose |
|---------|---------|
| AWS App Runner | Container hosting with auto-scaling |
| AWS RDS | Managed PostgreSQL database |
| AWS ECR | Docker image registry |
| AWS SES | Transactional email |
| Cloudflare | DNS, SSL, CDN, DDoS protection |

### CI/CD & Quality
| Tool | Purpose |
|------|---------|
| GitHub Actions | Automated testing and deployment |
| Greptile | AI-powered code review |
| JUnit 5 | Unit and integration testing |
| JaCoCo | Code coverage reporting |
| Docker | Containerization |

---

## Features

- **Project Showcase** - Dynamic project cards with tech stack tags, GitHub links, and live demos
- **Contact Form** - Form submissions saved to database with instant email notifications via AWS SES
- **Responsive Design** - Mobile-first design with Tailwind CSS
- **SEO Optimized** - Server-side rendering for search engine visibility
- **Health Monitoring** - Spring Actuator endpoints for infrastructure health checks

---

## Local Development

### Prerequisites
- Java 21
- Maven 3.9+
- Docker & Docker Compose

### Quick Start

```bash
# Clone the repository
git clone https://github.com/Cesar6060/cesar-portfolio.git
cd cesar-portfolio

# Start PostgreSQL database
docker-compose up -d

# Run the application
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Open in browser
open http://localhost:8080
```

### Available Profiles

| Profile | Database | Use Case |
|---------|----------|----------|
| `dev` | Local PostgreSQL (Docker) | Development |
| `test` | H2 in-memory | Automated testing |
| `prod` | AWS RDS | Production |

---

## Project Structure

```
cesar-portfolio/
├── src/
│   ├── main/
│   │   ├── java/com/cesarvillarreal/portfolio/
│   │   │   ├── controller/     # Web & API controllers
│   │   │   │   ├── HomeController.java
│   │   │   │   └── ApiController.java
│   │   │   ├── model/          # JPA entities
│   │   │   │   ├── Project.java
│   │   │   │   └── ContactMessage.java
│   │   │   ├── repository/     # Data access layer
│   │   │   └── service/        # Business logic
│   │   │       ├── ProjectService.java
│   │   │       ├── ContactService.java
│   │   │       └── EmailService.java
│   │   └── resources/
│   │       ├── templates/      # Thymeleaf templates
│   │       │   ├── layout/     # Base layouts
│   │       │   ├── fragments/  # Reusable components
│   │       │   └── *.html      # Page templates
│   │       ├── static/         # CSS, JS, images
│   │       ├── db/migration/   # Flyway SQL migrations
│   │       └── application*.yml # Configuration
│   └── test/                   # Unit & integration tests
├── docs/                       # Documentation
├── .github/workflows/          # CI/CD pipelines
├── docker-compose.yml          # Local development
├── Dockerfile                  # Production build
└── pom.xml                     # Maven configuration
```

---

## Testing

```bash
# Run all tests
mvn test

# Run tests with coverage report
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### Test Strategy
- **Unit Tests** - Service layer logic with mocked dependencies
- **Integration Tests** - Controller endpoints with MockMvc
- **Database Tests** - Repository queries against H2

---

## CI/CD Pipeline

### On Pull Request (CI)
1. Checkout code
2. Set up Java 21
3. Run `mvn test`
4. Report results

### On Push to Master (Deploy)
1. Run full test suite
2. Build Docker image (`--platform linux/amd64`)
3. Authenticate with AWS ECR
4. Push image with commit SHA and `latest` tags
5. App Runner auto-deploys on ECR push
6. Flyway runs migrations on startup

```yaml
# Simplified workflow
git push origin master
  → GitHub Actions triggered
    → Tests pass
      → Docker build & push to ECR
        → App Runner detects new image
          → Zero-downtime deployment
            → Flyway migrations run
              → Live at cesarvillarreal.dev
```

---

## Database Migrations

Migrations are managed by Flyway and run automatically on application startup.

```
src/main/resources/db/migration/
├── V1__initial_schema.sql       # Tables: projects, contact_messages
├── V2__seed_projects.sql        # Initial project data
├── V3__update_github_urls.sql   # GitHub URL corrections
├── V4__update_project_details.sql # Enhanced descriptions
├── V5__update_project_images.sql  # Image paths
├── V6__add_ai_tools_to_tech_stack.sql
├── V7__update_tech_stacks.sql
└── V8__remove_claude_code.sql
```

### Creating New Migrations

```bash
# Create a new migration file
touch src/main/resources/db/migration/V9__description.sql

# Flyway will run it automatically on next startup
```

---

## API Endpoints

### Web Routes
| Method | Path | Description |
|--------|------|-------------|
| GET | `/` | Home page with featured projects |
| GET | `/about` | About page |
| GET | `/projects` | All projects |
| GET | `/projects/{slug}` | Project detail |
| GET | `/contact` | Contact form |
| POST | `/contact` | Submit contact form |
| GET | `/how-i-built-this` | Architecture showcase |

### REST API
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/projects` | List all projects (JSON) |
| GET | `/api/projects/{slug}` | Single project (JSON) |

### Health Check
| Method | Path | Description |
|--------|------|-------------|
| GET | `/actuator/health` | Application health status |

---

## Environment Variables

### Required for Production

| Variable | Description |
|----------|-------------|
| `DATABASE_URL` | JDBC connection string |
| `DATABASE_USERNAME` | Database user |
| `DATABASE_PASSWORD` | Database password |
| `SPRING_PROFILES_ACTIVE` | Set to `prod` |

### Optional

| Variable | Default | Description |
|----------|---------|-------------|
| `NOTIFICATION_EMAIL` | cesarvillarreal11@gmail.com | Contact form notifications |

---

## Cost Breakdown

| Service | Configuration | Est. Monthly Cost |
|---------|--------------|-------------------|
| App Runner | 0.25 vCPU, 512MB | $5-10 |
| RDS | db.t3.micro, 20GB | $15* |
| ECR | <1GB storage | <$1 |
| SES | <1000 emails | Free |
| Cloudflare | Free tier | $0 |
| **Total** | | **~$20-25** |

*Free tier eligible for 12 months

---

## Documentation

| Document | Description |
|----------|-------------|
| [Architecture Decisions](docs/ARCHITECTURE_DECISIONS.md) | Technical decision records |
| [AWS Setup](docs/AWS_SETUP.md) | Infrastructure configuration |
| [Interview Talking Points](docs/INTERVIEW_TALKING_POINTS.md) | Project discussion guide |

---

## License

MIT License - See [LICENSE](LICENSE) for details.

---

## Contact

**Cesar Villarreal**
- Website: [cesarvillarreal.dev](https://cesarvillarreal.dev)
- GitHub: [@Cesar6060](https://github.com/Cesar6060)
- LinkedIn: [cesar-b-villarreal](https://linkedin.com/in/cesar-b-villarreal)
- Email: cesarvillarreal11@gmail.com
