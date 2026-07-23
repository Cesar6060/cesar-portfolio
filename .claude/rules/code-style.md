# Code Style

- Use Java 21 features where appropriate (records, pattern matching, text blocks)
- Constructor injection only — never use @Autowired on fields
- Controllers (HomeController, ApiController) stay thin — delegate to services
- Service classes (ProjectService, ContactService, EmailService) hold business logic
- Repositories extend JpaRepository — no custom implementations unless needed
- One class per file, file name matches class name
- Package structure: controller/, model/, repository/, service/
- Use records for DTOs and value objects
- Prefer Optional over null returns
- Keep methods under 30 lines
- No wildcard imports
- Thymeleaf templates use layout dialect — new pages extend layout/base
- Reusable HTML goes in templates/fragments/
- Static assets (CSS, JS, images) go in src/main/resources/static/
- Tailwind CSS via CDN — do not install locally
