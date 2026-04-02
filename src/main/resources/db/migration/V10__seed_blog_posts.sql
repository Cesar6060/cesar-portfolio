-- Seed initial blog post
INSERT INTO blog_posts (title, slug, excerpt, content, image_url, published, display_order, created_at, updated_at)
VALUES (
    'Building a Production-Ready Portfolio with Spring Boot and AWS',
    'building-portfolio-spring-boot-aws',
    'A comprehensive guide on how I built this portfolio website using Spring Boot, Thymeleaf, PostgreSQL, and deployed it on AWS App Runner with a full CI/CD pipeline.',
    'Welcome to my first blog post! In this article, I''ll walk you through the technical decisions and implementation details behind this portfolio website.

## Why Spring Boot?

When building a portfolio, many developers reach for static site generators or JavaScript frameworks. I chose Spring Boot for several reasons:

1. **Enterprise-grade foundation** - Demonstrates real-world skills employers value
2. **Server-side rendering** - Better SEO and faster initial page loads
3. **Database integration** - Dynamic content management without a headless CMS
4. **Cloud deployment experience** - Shows AWS infrastructure knowledge

## Architecture Overview

The site follows a clean MVC architecture:

- **Controllers** handle HTTP requests and delegate to services
- **Services** contain business logic and interact with repositories
- **Repositories** provide database access via Spring Data JPA
- **Thymeleaf templates** render server-side HTML with Tailwind CSS

## Cloud Infrastructure

I deployed on AWS App Runner because it provides:

- Fully managed container hosting
- Automatic scaling
- Built-in HTTPS
- Zero VPC configuration required

The database runs on RDS PostgreSQL, and I use Flyway for version-controlled schema migrations.

## CI/CD Pipeline

Every push to the master branch triggers GitHub Actions to:

1. Run the test suite
2. Build a Docker image
3. Push to ECR
4. App Runner automatically deploys the new version

This ensures zero-downtime deployments and quick iteration.

## What''s Next?

I''ll be writing more about specific technical challenges, cloud architecture patterns, and software engineering best practices. Stay tuned!',
    NULL,
    true,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);
