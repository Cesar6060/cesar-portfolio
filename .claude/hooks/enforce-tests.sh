#!/bin/bash

INPUT=$(cat)
STOP_HOOK_ACTIVE=$(echo "$INPUT" | jq -r '.stop_hook_active // false')

if [ "$STOP_HOOK_ACTIVE" = "true" ]; then
  exit 0
fi

# Run the test suite
mvn test -q 2>&1

if [ $? -ne 0 ]; then
  echo "Tests are failing. Please fix the failing tests before finishing." >&2
  exit 2
fi

# Run compilation check
mvn compile -q 2>&1

if [ $? -ne 0 ]; then
  echo "Compilation errors found. Please fix them before finishing." >&2
  exit 2
fi

exit 0
