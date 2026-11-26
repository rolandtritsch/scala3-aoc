# sbt version upgrade: working around cached SemanticDB write failures

Date: 2026-11-26  
Repository: `scala3-aoc`, projects `0000/` and `2015/`  
Commit: `cc9ae13`

## Symptom

Running `sbt test` in `2015/` after a cache hit failed during compilation, before any test ran:

```
[error] java.nio.file.AccessDeniedException:
  .../target/out/jvm/scala-3.9.0/scala3-aoc-2015/meta/META-INF/semanticdb/src/main/scala/aoc2015/Main.scala.semanticdb
[error]   at dotty.tools.dotc.semanticdb.ExtractSemanticDB$.write
```

The compiler printed its "unhandled exception / file a crash report" banner, and sbt retried the
compile several times through `sbt.internal.io.Retry`. A clean build succeeded (215 tests passed),
which pointed at stale build state rather than a source error.

## Root cause

This is a known sbt 2.0.10 regression, [sbt/sbt#9877](https://github.com/sbt/sbt/issues/9877),
introduced when sbt started sealing disk-cache blobs read-only (sbt#9847):

1. sbt 2 runs with `-Dsbt.io.virtual=true`; Zinc's disk action cache stores outputs in a
   content-addressable store (CAS) under `~/.cache/sbt/v2/cas/`. Blobs are deliberately mode `0444`.
2. A cache hit materialises output paths as **symlinks** into that store. In the failing build the
   SemanticDB tree contained 117 symlinks and no regular files.
3. `semanticdbEnabled := true` makes Dotty's `ExtractSemanticDB` write `.semanticdb` files itself
   with `Files.newOutputStream`, outside Zinc's class-file management. On the next cache miss it
   wrote through the symlink into the read-only blob, producing `AccessDeniedException`.

Reproduction on the original version: `clean; compile; compile` (cache hit restores links), then
edit any source and `compile` again — the write fails.

## Why a version bump, not a build.sbt change

Upstream fixed this in [PR #9878](https://github.com/sbt/sbt/pull/9878) and the 2.0.x backport
[PR #9879](https://github.com/sbt/sbt/pull/9879). `DiskActionCacheStore.copyOnRestore` now restores
`*.semanticdb` files as writable copies instead of links, because SemanticDB writers overwrite
their previous output in place and Zinc does not move those files aside.

The patch is absent from 2.0.10 and present in 2.0.11, 2.0.12 and 2.1.1 (verified against the
published `util-cache_3` sources jars). A build.sbt workaround would have meant either replacing
the disk action cache with an in-memory store — losing incremental caching — or reimplementing
sbt's patch locally. Upgrading is smaller and keeps the fix maintained upstream, so no solution
code or `build.sbt` setting needed changing.

## Choosing the version

| Version | Result |
| --- | --- |
| 2.0.10 | Bug reproduces |
| 2.0.12 | Bug fixed; all checks green |
| 2.1.1 | Bug fixed, but `scalafixAll --check` failed with `SemanticDB not found` for all 22 test sources |

The 2.1.1 failure came from `sbt-scalafix` 0.14.9 not handling the newer test SemanticDB layout.
Bumping it to **0.14.10** resolved the incompatibility, so the final choice is sbt 2.1.1 (latest)
with scalafix 0.14.10.

## Changes

| File | Change |
| --- | --- |
| `0000/project/build.properties` | `sbt.version` 2.0.10 → 2.1.1 |
| `2015/project/build.properties` | `sbt.version` 2.0.10 → 2.1.1 |
| `0000/project/plugins.sbt` | `sbt-scalafix` 0.14.9 → 0.14.10 |
| `2015/project/plugins.sbt` | `sbt-scalafix` 0.14.9 → 0.14.10 |

The template is bumped deliberately alongside the year project: `0000/` also enables SemanticDB
and carried the same latent bug. Year projects do not depend on the template, so the two build
files were edited independently.

## Verification

Commands run inside each affected year directory:

- `sbt --server --batch 'clean; compile; testOnly *'`
- `sbt --server --batch 'scalafmtCheckAll; scalafmtSbtCheck; scalafixAll --check'`

Results:

| Project | Tests | Scalafix / formatting | Cached SemanticDB |
| --- | --- | --- | --- |
| `2015/` | 250 passed, 0 failed, 0 ignored | pass | 0 symlinks after a cache hit |
| `0000/` | 11 passed, 0 failed, 0 ignored | pass | — |

Additionally, after a clean + compile + cache hit, appending a comment to
`2015/src/main/scala/aoc2015/Main.scala` and recompiling succeeded with no `AccessDeniedException`.
The probe was reverted; no solution source was changed. `git diff --check` was clean before commit.

## Takeaway

If the failure reappears, check whether the SemanticDB outputs under `target/out/.../meta` have
become symlinks into `~/.cache/sbt/v2/cas/`. As an immediate recovery, `sbt clean` removes them;
the durable fix is to stay on an sbt release that includes the `copyOnRestore` patch.
