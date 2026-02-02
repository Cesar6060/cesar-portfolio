-- V8: Remove Claude Code from tech stacks

DELETE FROM project_tech_stack
WHERE technology = 'Claude Code';
