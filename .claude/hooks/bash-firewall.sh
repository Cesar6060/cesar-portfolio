#!/bin/bash

INPUT=$(cat)
COMMAND=$(echo "$INPUT" | jq -r '.tool_input.command // empty')

if [ -z "$COMMAND" ]; then
  exit 0
fi

BLOCKED_PATTERNS=(
  "rm -rf /"
  "rm -rf ~"
  "rm -rf \."
  "git push --force"
  "git push -f"
  "git checkout master"
  "git reset --hard"
  "DROP TABLE"
  "DROP DATABASE"
  "truncate"
  "mkfs"
  "> /dev/sda"
  "chmod 777"
  "curl.*| bash"
  "wget.*| bash"
  "aws rds delete"
  "aws apprunner delete"
  "aws ecr delete"
  "aws s3 rm"
  "docker rm"
  "docker system prune"
  "docker rmi"
)

for pattern in "${BLOCKED_PATTERNS[@]}"; do
  if echo "$COMMAND" | grep -qi "$pattern"; then
    echo "BLOCKED: Command matches dangerous pattern: $pattern" >&2
    echo "Command was: $COMMAND" >&2
    exit 2
  fi
done

exit 0
