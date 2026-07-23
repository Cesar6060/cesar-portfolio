# Security Rules

- Never log sensitive data (tokens, passwords, PII, database credentials)
- All user input must be validated with @Valid before use (especially ContactMessage submissions)
- Database queries only through Spring Data JPA repositories, never raw SQL in Java code
- Never expose stack traces in API responses — use @ControllerAdvice for global error handling
- Never store secrets in code — DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD, MAIL_* are Render env vars
- Never commit application-prod.yml or any file containing real credentials
- CORS must be explicitly configured — never use allowAll in production
- Validate contact form input size and type before saving to database
- EmailService must not expose internal errors to the user if the mail provider fails (email currently disabled in prod via APP_EMAIL_ENABLED=false)
