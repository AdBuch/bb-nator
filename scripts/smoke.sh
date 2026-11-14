#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://127.0.0.1:8088}"
echo "Checking BB-NATOR at ${BASE_URL} ..."
for attempt in $(seq 1 60); do
  if curl -fsS --connect-timeout 3 --max-time 5 "${BASE_URL}/api/v1/system/status" | grep -q '"status":"READY"'; then
    if curl -fsS --connect-timeout 3 --max-time 5 "${BASE_URL}/" | grep -qi '<app-root'; then
      denied="$(curl -s -o /dev/null -w '%{http_code}' --connect-timeout 3 --max-time 5 "${BASE_URL}/api/v1/apprentices")"
      if [ "${denied}" != "403" ]; then
        echo "SMOKE FAIL: unauthenticated business route returned ${denied}, expected 403" >&2
        exit 1
      fi
      echo "SMOKE PASS: API status and Angular index respond; other API routes are denied"
      exit 0
    fi
  fi
  sleep 3
done
echo "SMOKE FAIL: frontend or API did not respond" >&2
exit 1
