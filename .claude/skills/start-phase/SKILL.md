---
name: start-phase
description: Interview the user and write a phase spec before any implementation begins. Run at the start of each new phase or feature.
disable-model-invocation: true
argument-hint: [phase-number-or-name]
---

You are starting a new phase: $ARGUMENTS

Do NOT write any implementation code during this skill. Follow these steps:

1. Read the most recent file in `docs/handoffs/` for current project state (if the folder doesn't exist yet, skip this).
2. Read ONLY the section of `BUILD_PROGRESS.md` for this phase (search for the phase heading — do not read the whole file).
3. Use subagents (Explore) to investigate the parts of the codebase this phase touches. Report what exists already and what patterns to follow.
4. Interview the user with AskUserQuestion. Dig into the hard parts: edge cases, UI/UX decisions, data model tradeoffs, whether a change needs a new Flyway migration, deployment implications (master auto-deploys). Don't ask obvious questions. Keep going until scope is unambiguous.
5. Write the spec to `docs/specs/phase-<N>-<name>.md` with:
   - **Goal** — one paragraph
   - **Out of scope** — explicit list
   - **Backend tasks** — entities, Flyway migrations (`V[next]__description.sql`), services, controllers (with URL patterns and validation), as a checklist
   - **Frontend tasks** — Thymeleaf templates (extending layout/base), fragments, static assets, as a checklist
   - **Verification** — the exact checks that prove the phase works end to end (specific test cases, `mvn test`, a manual flow to click through locally and at cesarvillarreal.dev after deploy)

   While writing the checklists, mark items that are independent of each other (touch different files, no shared state) with `[P]` — the implementation session can hand these to parallel subagents.

6. Tell the user to review the spec, then start a FRESH session (or /clear) and give them BOTH prompts below, letting them choose:

   **Interactive (default):**
   "Read docs/specs/phase-<N>-<name>.md and implement it on a feature branch. Work through the checklist in order; items marked [P] may be dispatched to parallel subagents. Run /verify-stack before marking anything complete, and /finish-phase when the checklist is done."

   **Unattended (overnight — only for specs with no open questions):**
   "Read docs/specs/phase-<N>-<name>.md and implement it on a feature branch, unattended. Do not ask questions: make the most reasonable choice and record every assumption in an '## Assumptions' section appended to the spec. If a checklist item requires a decision with irreversible consequences, skip it and note why instead of guessing. Items marked [P] may go to parallel subagents. Run /verify-stack until PASS, then /finish-phase. STOP after opening the PR — never merge, merging deploys to production."
