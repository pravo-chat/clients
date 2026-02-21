#!/usr/bin/env bash
set -euo pipefail

# Настраивает git hooks для репозитория, чтобы они хранились в clients/.githooks
#
# Использование (из корня репо):
#   bash clients/setup-git-hooks.sh

REPO_ROOT="$(git rev-parse --show-toplevel 2>/dev/null || true)"
if [[ -z "${REPO_ROOT}" ]]; then
  echo "Not a git repository. Run this inside the repo."
  exit 1
fi

HOOKS_DIR="$REPO_ROOT/clients/.githooks"

git -C "$REPO_ROOT" config core.hooksPath "$HOOKS_DIR"
echo "✅ git hooksPath set to: $HOOKS_DIR"
echo "Now hooks in that directory will run automatically (e.g. pre-push)."





