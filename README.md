# Cesar Villarreal - Portfolio Website

Personal portfolio website showcasing my work as a Cloud Engineer, Technical Consultant, and Software Engineer.

## Tech Stack

| Layer | Technology |
|-------|------------|
| **Backend** | Java 21, Spring Boot 3.2, Thymeleaf |
| **Styling** | Tailwind CSS |
| **Database** | PostgreSQL 16 (AWS RDS in production) |
| **ORM** | Spring Data JPA |
| **Migrations** | Flyway |
| **Hosting** | AWS App Runner |
| **CI/CD** | GitHub Actions |
| **Code Review** | Greptile |
| **Testing** | JUnit 5, JaCoCo |
| **Domain/DNS** | Cloudflare |

## Quick Start

```bash
# Start database
docker-compose up -d

# Run application
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Open browser
open http://localhost:8080
```

## Project Structure

```
src/
├── main/
│   ├── java/com/cesarguerra/portfolio/
│   │   ├── controller/    # Web and API controllers
│   │   ├── model/         # JPA entities
│   │   ├── repository/    # Data access
│   │   └── service/       # Business logic
│   └── resources/
│       ├── templates/     # Thymeleaf templates
│       ├── static/        # CSS, JS, images
│       └── db/migration/  # Flyway migrations
└── test/                  # Unit and integration tests
```

## Testing

```bash
mvn test                    # Run tests
mvn test jacoco:report      # Generate coverage report
```

Coverage report available at `target/site/jacoco/index.html`

## Deployment

Pushes to `main` trigger automatic deployment via GitHub Actions:
1. Run tests
2. Build Docker image
3. Push to AWS ECR
4. Deploy to AWS App Runner
5. Run Flyway migrations

## Documentation

- [Architecture Decisions](docs/ARCHITECTURE_DECISIONS.md)
- [MVP Scope](docs/MVP_SCOPE.md)
- [Project Plan](docs/PROJECT_PLAN.md)

## License

MIT
