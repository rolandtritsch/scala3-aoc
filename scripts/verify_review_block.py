#!/usr/bin/env python3
"""Validate an aoc-review-result block before marking a review issue done.

Reads the final review comment body from stdin and verifies that it carries a
fenced aoc-review-result JSON block whose values match the review issue and
the reviewed attempt. Exits 0 on success and non-zero naming the exact defect
otherwise.

The block must look like:

```aoc-review-result
{"reviewIssueId":"<issue UUID>","year":2015,"day":1,"part":1,"solutionCommit":"<full hash>","accepted":true,"repairRequired":false}
```

Usage:
  verify_review_block.py --issue-id <UUID> --title "<issue title>" \
      [--commit <solution commit>] < comment.md

A defective block (missing, unparsable, or mismatched values) keeps the cycle
waiting, so the reviewer must run this check and only mark the review done on
exit 0.
"""

from __future__ import annotations

import argparse
import json
import re
import sys
from typing import Optional

BLOCK_FENCE = "aoc-review-result"
REQUIRED_FIELDS = (
    "reviewIssueId",
    "year",
    "day",
    "part",
    "solutionCommit",
    "accepted",
    "repairRequired",
)


def extract_block(body: str) -> Optional[str]:
    """Return the JSON inside the aoc-review-result fence, or None."""
    pattern = re.compile(
        r"```" + BLOCK_FENCE + r"\s*\n(.*?)\n```", re.DOTALL
    )
    match = pattern.search(body)
    if not match:
        return None
    return match.group(1).strip()


def parse_title(title: str) -> Optional[dict]:
    """Extract year, day, part from a title like 'AoC 2015 Day01 part1: ...'."""
    match = re.search(
        r"(?P<year>20\d\d)\s+Day(?P<day>\d+)\s+part(?P<part>\d+)", title
    )
    if not match:
        return None
    return {
        "year": int(match.group("year")),
        "day": int(match.group("day")),
        "part": int(match.group("part")),
    }


def main(argv: Optional[list[str]] = None) -> int:
    parser = argparse.ArgumentParser(
        description="Validate an aoc-review-result block from stdin."
    )
    parser.add_argument("--issue-id", required=True, help="Review issue UUID")
    parser.add_argument("--title", required=True, help="Review issue title")
    parser.add_argument(
        "--commit", help="Expected reviewed solution commit (full hash)"
    )
    args = parser.parse_args(argv)

    body = sys.stdin.read()
    raw = extract_block(body)
    if raw is None:
        print(
            f"ERROR: missing fenced ```{BLOCK_FENCE}``` block in comment body",
            file=sys.stderr,
        )
        return 1

    try:
        block = json.loads(raw)
    except json.JSONDecodeError as exc:
        print(f"ERROR: aoc-review-result block is not valid JSON: {exc}", file=sys.stderr)
        return 1

    if not isinstance(block, dict):
        print("ERROR: aoc-review-result block must be a JSON object", file=sys.stderr)
        return 1

    missing = [f for f in REQUIRED_FIELDS if f not in block]
    if missing:
        print(
            f"ERROR: aoc-review-result block missing required field(s): {', '.join(missing)}",
            file=sys.stderr,
        )
        return 1

    if block["reviewIssueId"] != args.issue_id:
        print(
            f"ERROR: reviewIssueId {block['reviewIssueId']!r} does not match --issue-id {args.issue_id!r}",
            file=sys.stderr,
        )
        return 1

    title_parts = parse_title(args.title)
    if title_parts is None:
        print(
            f"ERROR: cannot parse year/day/part from title {args.title!r}",
            file=sys.stderr,
        )
        return 1

    for field in ("year", "day", "part"):
        if block[field] != title_parts[field]:
            print(
                f"ERROR: block {field}={block[field]!r} does not match title {field}={title_parts[field]!r}",
                file=sys.stderr,
            )
            return 1

    for field in ("year", "day", "part"):
        if not isinstance(block[field], int) or isinstance(block[field], bool):
            print(
                f"ERROR: block {field} must be an integer, got {type(block[field]).__name__}",
                file=sys.stderr,
            )
            return 1

    if not re.fullmatch(r"[0-9a-f]{40}", block["solutionCommit"]):
        print(
            f"ERROR: solutionCommit {block['solutionCommit']!r} is not a full 40-char hex hash",
            file=sys.stderr,
        )
        return 1

    if args.commit and block["solutionCommit"] != args.commit:
        print(
            f"ERROR: solutionCommit {block['solutionCommit']!r} does not match --commit {args.commit!r}",
            file=sys.stderr,
        )
        return 1

    for field in ("accepted", "repairRequired"):
        if not isinstance(block[field], bool):
            print(
                f"ERROR: block {field} must be a boolean, got {type(block[field]).__name__}",
                file=sys.stderr,
            )
            return 1

    print("OK: aoc-review-result block is valid")
    return 0


if __name__ == "__main__":
    sys.exit(main())
