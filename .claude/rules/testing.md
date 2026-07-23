---
paths:
  - "src/test/**/*.java"
  - "src/main/java/**/service/*.java"
  - "src/main/java/**/controller/*.java"
---

# Testing Rules

- Tests use H2 in-memory database (test profile), not PostgreSQL
- Some PostgreSQL-specific SQL may not work in H2 — watch for syntax differences
- Unit tests mock dependencies with Mockito — test service logic in isolation
- Integration tests use MockMvc for controller endpoints
- Run `mvn test` before committing — CI will catch failures but save time locally
- JaCoCo generates coverage reports — aim for meaningful coverage, not 100%
- Test file naming: [ClassName]Test.java in matching package under src/test/
- Each new controller endpoint needs at least one happy-path and one error-path test
- Each new service method needs a unit test
