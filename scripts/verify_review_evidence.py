#!/usr/bin/env python3
"""Verify the mechanical evidence for a puzzle review before marking it done.

Runs the read-only checks a reviewer must confirm independently of the solver
and submitter: the canonical checkout is a clean tree with HEAD equal to
origin/main, the reviewed solution commit is present and is HEAD, the year's
test suite passes, and the runner prints the exact expected answer for the
assigned day/part. Does not modify, format, commit, push, or submit anything.

Usage:
  verify_review_evidence.py --year YYYY --day NN --part P --answer ANSWER \
      [--commit <full hash>] [--repo <checkout>] [--skip-sbt]

Exit 0 only when every enabled check passes; otherwise it prints a named defect
to stderr and exits 1. The JSON receipt on stdout records each check's result.
"""

from __future__ import annotations

import argparse
import json
import os
import re
import subprocess
import sys
from pathlib import Path
from typing import Optional

RECEIPT_FIELDS = (
    "repo",
    "year",
    "day",
    "part",
    "expectedAnswer",
    "reviewedCommit",
    "treeClean",
    "head",
    "originMain",
    "remoteParity",
    "commitMatches",
    "testsPassed",
    "runnerPassed",
    "runnerAnswer",
    "answerMatches",
    "allPassed",
)


def run(cmd: list[str], cwd: Path) -> subprocess.CompletedProcess:
    return subprocess.run(cmd, cwd=cwd, text=True, capture_output=True)


def sbt_ok(year_dir: Path, args: list[str]) -> tuple[bool, str]:
    proc = run(["sbt", "--batch", *args], cwd=year_dir)
    return proc.returncode == 0, proc.stdout + proc.stderr


def extract_runner_answer(output: str, day: int, part: int) -> Optional[str]:
    pattern = re.compile(
        rf"Day{day:02d} - part{part}:\s*([^\s]+)"
    )
    match = pattern.search(output)
    if match:
        return match.group(1)
    return None


def main(argv: Optional[list[str]] = None) -> int:
    parser = argparse.ArgumentParser(
        description="Verify review evidence for a puzzle in scala3-aoc."
    )
    parser.add_argument("--year", required=True, help="Year directory, e.g. 2015")
    parser.add_argument("--day", required=True, type=int, help="Day number, e.g. 1")
    parser.add_argument("--part", required=True, type=int, choices=(1, 2), help="Part 1 or 2")
    parser.add_argument("--answer", required=True, help="Expected runner answer")
    parser.add_argument("--commit", help="Reviewed solution commit (full hash)")
    parser.add_argument("--repo", default=os.environ.get("AOC_REPO_DIR", "."), help="Canonical checkout root")
    parser.add_argument("--skip-sbt", action="store_true", help="Skip the sbt test and runner checks")
    args = parser.parse_args(argv)

    repo = Path(args.repo).resolve()
    year_dir = repo / args.year
    if not repo.is_dir() or not (repo / ".git").is_dir():
        print(f"ERROR: {repo} is not a git checkout", file=sys.stderr)
        return 1
    if not year_dir.is_dir():
        print(f"ERROR: year project {year_dir} does not exist", file=sys.stderr)
        return 1

    receipt = {
        "repo": str(repo),
        "year": args.year,
        "day": args.day,
        "part": args.part,
        "expectedAnswer": args.answer,
        "reviewedCommit": args.commit,
    }

    head = (run(["git", "rev-parse", "HEAD"], cwd=repo).stdout or "").strip()
    origin_main = (run(["git", "rev-parse", "origin/main"], cwd=repo).stdout or "").strip()
    status = (run(["git", "status", "--porcelain"], cwd=repo).stdout or "").strip()

    receipt["head"] = head
    receipt["originMain"] = origin_main
    receipt["remoteParity"] = bool(head) and head == origin_main
    receipt["treeClean"] = status == ""
    if status:
        print(f"ERROR: working tree is not clean:\n{status}", file=sys.stderr)

    if args.commit:
        proc = run(["git", "rev-parse", "--verify", f"{args.commit}^{{commit}}"], cwd=repo)
        resolved = proc.stdout.strip() if proc.returncode == 0 else ""
        receipt["commitMatches"] = bool(resolved) and resolved == head
        if not receipt["commitMatches"]:
            print(
                f"ERROR: reviewed commit {args.commit!r} does not match HEAD {head!r}",
                file=sys.stderr,
            )
    else:
        receipt["commitMatches"] = True

    tests_passed = runner_passed = True
    runner_answer = None
    if not args.skip_sbt:
        tests_passed, test_out = sbt_ok(year_dir, ["testOnly", "*"])
        if not tests_passed:
            print("ERROR: sbt --batch 'testOnly *' failed", file=sys.stderr)
            print(test_out[-2000:], file=sys.stderr)
        runner_passed, run_out = sbt_ok(year_dir, ["run"])
        if not runner_passed:
            print("ERROR: sbt --batch run failed", file=sys.stderr)
            print(run_out[-2000:], file=sys.stderr)
        else:
            runner_answer = extract_runner_answer(run_out, args.day, args.part)
            if runner_answer is None:
                print(
                    f"ERROR: runner output has no 'Day{args.day:02d} - part{args.part}:' line",
                    file=sys.stderr,
                )

    receipt["testsPassed"] = tests_passed
    receipt["runnerPassed"] = runner_passed
    receipt["runnerAnswer"] = runner_answer
    receipt["answerMatches"] = tests_passed and runner_passed and runner_answer == args.answer
    if not receipt["answerMatches"] and runner_answer is not None:
        print(
            f"ERROR: runner answer {runner_answer!r} != expected {args.answer!r}",
            file=sys.stderr,
        )

    receipt["allPassed"] = all(
        receipt[k]
        for k in ("remoteParity", "treeClean", "commitMatches", "testsPassed", "runnerPassed", "answerMatches")
    )

    print(json.dumps({k: receipt[k] for k in RECEIPT_FIELDS}, indent=2))
    return 0 if receipt["allPassed"] else 1


if __name__ == "__main__":
    sys.exit(main())
