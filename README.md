# Cesar Villarreal - Portfolio Website

Personal portfolio website built with Spring Boot, demonstrating full-stack development skills.

## Tech Stack

- **Backend:** Spring Boot 3.2, Java 21
- **Frontend:** Thymeleaf, Tailwind CSS
- **Database:** PostgreSQL 16
- **Deployment:** AWS App Runner, RDS
- **CI/CD:** GitHub Actions

## Quick Start

```bash
# Start database
docker-compose up -d

# Run application
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Open browser
open http://localhost:8080
```

## Documentation

- [Architecture Decisions](docs/ARCHITECTURE_DECISIONS.md) - Why I made these choices
- [MVP Scope](docs/MVP_SCOPE.md) - What's in and out
- [Project Plan](docs/PROJECT_PLAN.md) - Build timeline
- [How I Built This](docs/PROJECT_SHOWCASE.md) - Technical deep-dive

## Testing

```bash
mvn test                    # Run tests
mvn test jacoco:report      # Coverage report
```

## License

MIT
