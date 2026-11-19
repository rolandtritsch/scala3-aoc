#!/usr/bin/env python3
"""Compatibility entry point; implementation belongs to the company package."""
import os
from pathlib import Path
import runpy
root=Path(os.environ.get("AOC_COMPANY_PACKAGE_DIR","/paperclip/company-packages/pcc-aoc-solver-scala"))
runpy.run_path(str(root/"skills/aoc-review/scripts/"/'verify_review_block.py'),run_name="__main__")
