import importlib.util
from pathlib import Path
import tempfile
import unittest

spec = importlib.util.spec_from_file_location("ensure_env", Path(__file__).with_name("ensure_env.py"))
ensure_env = importlib.util.module_from_spec(spec)
spec.loader.exec_module(ensure_env)


class EnsureEnvTest(unittest.TestCase):
    def write(self, content: str) -> Path:
        directory = tempfile.mkdtemp()
        path = Path(directory) / ".env"
        path.write_text(content, encoding="utf-8")
        return path

    def value(self, path: Path) -> str:
        for line in path.read_text(encoding="utf-8").splitlines():
            if line.startswith("APP_DB_PASSWORD="):
                return line.split("=", 1)[1]
        return ""

    def test_appends_when_missing_and_keeps_other_lines(self):
        path = self.write("POSTGRES_PASSWORD=keep-me\nHTTP_PORT=8088\n")
        self.assertTrue(ensure_env.ensure_app_db_password(path))
        text = path.read_text(encoding="utf-8")
        self.assertIn("POSTGRES_PASSWORD=keep-me", text)
        self.assertIn("HTTP_PORT=8088", text)
        self.assertGreaterEqual(len(self.value(path)), 32)

    def test_replaces_placeholder_and_empty_value(self):
        for content in ("APP_DB_PASSWORD=replace-with-a-different-random-password\n", "APP_DB_PASSWORD=\n"):
            path = self.write(content)
            self.assertTrue(ensure_env.ensure_app_db_password(path))
            self.assertNotIn(self.value(path), ("", "replace-with-a-different-random-password"))

    def test_keeps_existing_value(self):
        path = self.write("APP_DB_PASSWORD=already-set\n")
        self.assertFalse(ensure_env.ensure_app_db_password(path))
        self.assertEqual(self.value(path), "already-set")

    def test_missing_file_is_left_alone(self):
        self.assertFalse(ensure_env.ensure_app_db_password(Path(tempfile.mkdtemp()) / ".env"))

    def test_generated_value_is_url_safe(self):
        path = self.write("")
        ensure_env.ensure_app_db_password(path)
        self.assertRegex(self.value(path), r"^[A-Za-z0-9_-]+$")


if __name__ == "__main__":
    unittest.main()
