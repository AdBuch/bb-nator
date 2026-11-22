import importlib.util
from pathlib import Path
import unittest
from unittest.mock import patch

spec = importlib.util.spec_from_file_location("local_updater", Path(__file__).with_name("updater.py"))
updater = importlib.util.module_from_spec(spec)
spec.loader.exec_module(updater)


class UpdaterTests(unittest.TestCase):
    def test_allowed_origins_are_local_only(self):
        with patch.object(updater, "ROOT", Path("/no-such-directory")):
            origins = updater.allowed_origins()
        self.assertIn("http://127.0.0.1:8088", origins)
        self.assertNotIn("https://attacker.invalid", origins)

    def test_non_main_branch_cannot_update(self):
        def cmd(*args, **kwargs):
            if args[1:3] == ("remote", "get-url"):
                return "https://github.com/AdBuch/bb-nator.git"
            if args[1:3] == ("branch", "--show-current"):
                return "release/0.2-learning-path"
            return ""
        with patch.object(updater, "run", side_effect=cmd):
            with self.assertRaises(RuntimeError):
                updater.verify_repo()

    def test_update_commands_are_static(self):
        # No user-provided path/branch/command is accepted by the handler.
        self.assertEqual(updater.REPOSITORY, "AdBuch/bb-nator")


if __name__ == "__main__":
    unittest.main()
