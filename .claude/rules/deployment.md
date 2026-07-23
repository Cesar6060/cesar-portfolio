# Deployment Rules

- Deploy branch is **master** — pushing to master triggers automatic deployment
- CD pipeline (`deploy.yml`): GitHub Actions → mvn test → curl Render deploy hook → Render builds the Docker image from the repo and swaps it in once healthy
- Render auto-deploy is OFF — deploys happen only through the workflow, so nothing ships without tests passing
- Never push directly to master — use feature branches and PRs
- Before merging to master, ensure `mvn test` passes locally
- Flyway migrations run automatically on application startup (against Neon) — never run them manually
- Never edit existing Flyway migration files (V1–V12) — checksums will break
- New migrations: create V[next]__description.sql in src/main/resources/db/migration/
- After deployment, verify at https://cesarvillarreal.dev
- Purge Cloudflare cache if CSS/JS/image changes aren't showing
- Hosting: Render service `cesar-portfolio` (srv-d9g0m8741pts73bupasg); DB: Neon project `lively-leaf-06148630` — check status in their dashboards (deploy-keys.env is deleted; no local API keys)
- Email is disabled in prod (`APP_EMAIL_ENABLED=false`) — Render free tier blocks outbound SMTP
