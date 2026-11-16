#!/usr/bin/env python3
"""Local-only, token-protected updater. Never put Docker socket in application containers."""
from __future__ import annotations
import hmac
import json
import os
from pathlib import Path
import secrets
import subprocess
import threading
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

ROOT = Path(__file__).resolve().parent.parent
TOKEN_FILE = ROOT / ".update-token"
PORT = 8766
LOCK = threading.Lock()
REPOSITORY = "AdBuch/bb-nator"
DEFAULT_HTTP_PORT = "8088"


def token() -> str:
    if not TOKEN_FILE.exists():
        fd = os.open(TOKEN_FILE, os.O_WRONLY | os.O_CREAT | os.O_EXCL, 0o600)
        with os.fdopen(fd, "w") as out:
            out.write(secrets.token_urlsafe(48) + "\n")
    return TOKEN_FILE.read_text().strip()


def run(*args: str, timeout: int = 300) -> str:
    result = subprocess.run(args, cwd=ROOT, capture_output=True, text=True,
                            timeout=timeout, check=False)
    if result.returncode:
        raise RuntimeError("Local update command failed: " + args[0])
    return result.stdout.strip()


def allowed_origins() -> set[str]:
    port = DEFAULT_HTTP_PORT
    env = ROOT / ".env"
    if env.exists():
        for line in env.read_text().splitlines():
            if line.startswith("HTTP_PORT="):
                candidate = line.split("=", 1)[1].strip()
                if candidate.isdigit() and 1 <= int(candidate) <= 65535:
                    port = candidate
    return {"http://127.0.0.1:" + port, "http://localhost:" + port}


def verify_repo() -> None:
    origin = run("git", "remote", "get-url", "origin")
    if origin not in ("https://github.com/" + REPOSITORY,
                      "https://github.com/" + REPOSITORY + ".git",
                      "git@github.com:" + REPOSITORY + ".git"):
        raise RuntimeError("Unexpected Git remote; refusing update")
    if run("git", "branch", "--show-current") != "main":
        raise RuntimeError("Updater requires main branch")
    if run("git", "status", "--porcelain"):
        raise RuntimeError("Uncommitted changes; refusing update")


def check_update() -> dict:
    verify_repo()
    run("git", "fetch", "--prune", "origin", "main", timeout=120)
    current = run("git", "rev-parse", "HEAD")[:12]
    latest = run("git", "rev-parse", "origin/main")[:12]
    return {"current": current, "latest": latest,
            "updateAvailable": current != latest,
            "message": "Neue Version verfügbar." if current != latest else "Bereits aktuell."}


class Handler(BaseHTTPRequestHandler):
    def log_message(self, _fmt: str, *_args: object) -> None:
        pass  # No credentials or token logging.

    def send_json(self, status: int, payload: dict) -> None:
        body = json.dumps(payload).encode()
        self.send_response(status)
        origin = self.headers.get("Origin", "")
        if origin in allowed_origins():
            self.send_header("Access-Control-Allow-Origin", origin)
            self.send_header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
            self.send_header("Access-Control-Allow-Headers", "X-Update-Token")
            self.send_header("Vary", "Origin")
        self.send_header("Cache-Control", "no-store")
        self.send_header("X-Content-Type-Options", "nosniff")
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def allowed(self) -> bool:
        origin = self.headers.get("Origin", "")
        provided = self.headers.get("X-Update-Token", "")
        return (origin in allowed_origins() and
                hmac.compare_digest(provided, token()))

    def do_OPTIONS(self) -> None:
        if self.headers.get("Origin", "") not in allowed_origins():
            self.send_json(403, {"message": "Origin not allowed"})
        else:
            self.send_json(204, {})

    def do_GET(self) -> None:
        if not self.allowed():
            self.send_json(403, {"message": "Forbidden"})
            return
        if self.path != "/check":
            self.send_json(404, {"message": "Unknown route"})
            return
        if not LOCK.acquire(blocking=False):
            self.send_json(409, {"message": "Update already running"})
            return
        try:
            self.send_json(200, check_update())
        except (RuntimeError, subprocess.TimeoutExpired, OSError):
            self.send_json(409, {"message": "Git update check failed; inspect local repository"})
        finally:
            LOCK.release()

    def do_POST(self) -> None:
        if not self.allowed():
            self.send_json(403, {"message": "Forbidden"})
            return
        if self.path != "/apply" or self.headers.get("Content-Length", "0") not in ("0", ""):
            self.send_json(404, {"message": "Unsupported update request"})
            return
        if not LOCK.acquire(blocking=False):
            self.send_json(409, {"message": "Update already running"})
            return
        try:
            result = check_update()
            if result["updateAvailable"]:
                run("git", "merge", "--ff-only", "origin/main", timeout=60)
                run("docker", "compose", "up", "--build", "-d", timeout=900)
            after = check_update()
            after["message"] = "Installation abgeschlossen; Oberfläche neu laden."
            self.send_json(200, after)
        except (RuntimeError, subprocess.TimeoutExpired, OSError):
            self.send_json(409, {"message": "Update failed. Check local Git and Docker logs."})
        finally:
            LOCK.release()


if __name__ == "__main__":
    token()
    print("BB-NATOR local updater: http://127.0.0.1:" + str(PORT), flush=True)
    print("Admin token: see .update-token (file permissions restricted)", flush=True)
    ThreadingHTTPServer(("127.0.0.1", PORT), Handler).serve_forever()
