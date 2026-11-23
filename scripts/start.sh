#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."

if [ ! -f .env ]; then
  echo 'Create .env from .env.example and set a strong POSTGRES_PASSWORD first.' >&2
  exit 1
fi
if [ "$(git branch --show-current)" != "main" ]; then
  echo 'Starter only updates main. Switch to main before starting.' >&2
  exit 1
fi
if [ -n "$(git status --porcelain)" ]; then
  echo 'Uncommitted tracked/untracked changes; refusing automatic update.' >&2
  exit 1
fi
remote="$(git remote get-url origin)"
case "$remote" in
  "https://github.com/AdBuch/bb-nator"|"https://github.com/AdBuch/bb-nator.git"|"git@github.com:AdBuch/bb-nator.git") ;;
  *) echo 'Unexpected Git origin. Refusing automatic update.' >&2; exit 1 ;;
esac

echo 'Checking for updates on origin/main...'
if git fetch --prune origin main; then
  git merge --ff-only origin/main || { echo 'Fast-forward unavailable; refusing to overwrite local work.' >&2; exit 1; }
else
  echo 'Git fetch failed (possibly offline). Starting locally installed version.' >&2
fi
python3 scripts/ensure_env.py
docker compose up --build -d
bash scripts/smoke.sh
echo 'Open http://127.0.0.1:8088 · Admin token is in .update-token'
echo 'Leave this terminal open for the admin update button.'
exec python3 scripts/updater.py
