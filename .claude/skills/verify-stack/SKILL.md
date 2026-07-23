---
name: verify-stack
description: Run the full verification suite (tests, build, coverage) and report pass/fail with evidence. Use before declaring any task or phase complete.
---

Run these checks in order. Show the actual output for each — never assert success without evidence.

1. `mvn test` — all tests pass (uses H2 in-memory database via the test profile)
2. `mvn clean package -DskipTests` — production JAR builds cleanly
3. If database migrations were added this session: confirm the new file follows `V[next]__description.sql` naming in `src/main/resources/db/migration/` and that no existing migration file (V1–V12) was modified (`git diff --stat src/main/resources/db/migration/`)

If anything fails:

- Fix the ROOT CAUSE. Do not suppress errors, skip tests, or delete assertions to make checks pass.
- Watch for H2 vs PostgreSQL syntax differences — a test failure may be H2-specific, not a real bug. Verify against dev PostgreSQL (`docker-compose up -d`) before changing production SQL.
- Re-run the failed check after fixing.
- If the same check fails 3 times, stop and summarize the problem for the user instead of thrashing.

End with a one-line verdict: PASS (all green) or FAIL (what's still red).
