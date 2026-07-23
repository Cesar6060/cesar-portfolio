#!/bin/bash

INPUT=$(cat)
FILE_PATH=$(echo "$INPUT" | jq -r '.tool_input.file_path // empty')

if [ -z "$FILE_PATH" ]; then
  exit 0
fi

if [ -f "$FILE_PATH" ]; then
  case "$FILE_PATH" in
    *.java)
      if command -v google-java-format &> /dev/null; then
        google-java-format --replace "$FILE_PATH" 2>/dev/null
      fi
      ;;
    *.html|*.css|*.js|*.json|*.md)
      if command -v npx &> /dev/null; then
        npx prettier --write "$FILE_PATH" 2>/dev/null
      fi
      ;;
    *.yml|*.yaml)
      if command -v npx &> /dev/null; then
        npx prettier --write "$FILE_PATH" 2>/dev/null
      fi
      ;;
    *.sql)
      # Leave SQL migrations alone — formatting changes can break Flyway checksums
      ;;
  esac
fi

exit 0
