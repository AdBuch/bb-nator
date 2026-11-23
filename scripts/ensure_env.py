"""Adds settings that newer versions require to an existing local .env.

Only appends or fills missing values; never changes other lines. Safe to run on every start.
"""
from pathlib import Path
import secrets
import sys

APP_DB_PASSWORD_PLACEHOLDER = "replace-with-a-different-random-password"


def ensure_app_db_password(env_path: Path) -> bool:
    """Generate APP_DB_PASSWORD if it is missing, empty or still the example placeholder.

    Returns True when the file was changed. The value only uses URL-safe characters,
    so it is safe for Docker Compose and for the SQL callback that sets the role password.
    """
    if not env_path.exists():
        return False
    lines = env_path.read_text(encoding="utf-8").splitlines()
    generated = "APP_DB_PASSWORD=" + secrets.token_urlsafe(32)
    for index, line in enumerate(lines):
        if line.startswith("APP_DB_PASSWORD="):
            value = line.split("=", 1)[1].strip()
            if value and value != APP_DB_PASSWORD_PLACEHOLDER:
                return False
            lines[index] = generated
            break
    else:
        lines.append("# Added automatically: password of the database role bbnator_app.")
        lines.append(generated)
    env_path.write_text("\n".join(lines) + "\n", encoding="utf-8")
    return True


if __name__ == "__main__":
    target = Path(sys.argv[1]) if len(sys.argv) > 1 else Path(__file__).resolve().parent.parent / ".env"
    if ensure_app_db_password(target):
        print("Generated APP_DB_PASSWORD in " + str(target.name) + ".")
