---
name: finish-phase
description: Close out a completed phase — verify, adversarial review, update progress docs, push the feature branch, open the PR to master, and write the handoff. Run when the phase checklist is done.
disable-model-invocation: true
argument-hint: [phase-number-or-name]
---

You are finishing phase: $ARGUMENTS

Work through these steps in order. Do not skip ahead — each step gates the next.

1. **Branch check.** Run `git branch --show-current`. You must be on a feature branch, NEVER master — master auto-deploys to cesarvillarreal.dev on push. If you're on master, stop and tell the user; do not create a branch and move commits yourself without asking.

2. **Verify.** Run `/verify-stack`. It must end in PASS. If it fails, fix the root cause and re-run — do not proceed with red checks.

3. **Review pass.** Launch TWO subagents in parallel, both against the full phase diff (`git diff master...HEAD`):
   - the `code-reviewer` agent — reviews the changes for bugs, style, and project conventions
   - the `adversarial-tester` agent — actively tries to break the new feature (bad input, validation gaps, info leaks)

   Fix anything code-reviewer marks CRITICAL and anything adversarial-tester reports as BROKEN, then re-run `/verify-stack`. IMPORTANT/SUSPICIOUS findings may be deferred, but must be listed in the PR body — never silently dropped.

4. **Spec completeness.** Read the phase spec in `docs/specs/` (if one exists) and confirm every checklist item is actually done. Anything not done either gets finished now or explicitly moved to the next phase — call it out, don't silently drop it.

5. **Update progress docs.** Check off the completed items in `BUILD_PROGRESS.md` and the phase spec.

6. **Commit and push.** Commit any remaining changes with a clear message, then `git push -u origin <branch>`.

7. **Open the PR** against master with `gh pr create`. The PR body must include:
   - **Summary** — what the phase delivers, one short paragraph
   - **Spec** — link/path to `docs/specs/phase-<N>-<name>.md`
   - **Verification** — the actual `/verify-stack` results (paste the verdict and key output, not just "tests pass")
   - **Review** — code-reviewer's verdict, adversarial-tester's HELD/BROKEN summary, and any deferred findings
   - **Migrations** — list any new Flyway files (`V[n]__*.sql`) and confirm no existing migration was edited; write "None" if no schema changes
   - **Deploy note** — reminder that merging auto-deploys via GitHub Actions → Render deploy hook (Render builds the Docker image from the repo), and whether Cloudflare cache needs purging after (any CSS/JS/image changes)

8. **Handoff.** Run `/handoff` so the next session starts clean.

9. **Tell the user** the PR URL and what happens on merge: CI runs `mvn test`, then curls the Render deploy hook; Render builds the Docker image from the repo and swaps it in once healthy, and Flyway runs any new migrations on startup (against Neon). Remind them to verify at https://cesarvillarreal.dev after merging (and purge Cloudflare cache if static assets changed).

Never merge the PR yourself — merging deploys to production. That decision is the user's. This applies doubly in unattended runs: opening the PR is the finish line.
