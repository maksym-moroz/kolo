## Why

The repository now has a real CI and linting baseline, but that baseline landed as a set of Gradle, build-logic, and workflow changes rather than as an explicit OpenSpec record. Backfilling the proposal creates a durable change record for the already-implemented quality system so future work can reason from an agreed baseline instead of code archaeology alone.

## What Changes

- Establish a repository quality baseline built around root aggregate tasks `qualityCheck`, `qualityFix`, and `dependencyHealth`.
- Add module-level `qualityCheck` tasks through build-logic convention plugins so Detekt, Android lint, and dependency analysis are wired by module type.
- Add root Spotless formatting for Kotlin, Gradle Kotlin DSL, and misc repository files.
- Add GitHub workflows for repository quality, dependency health, workflow linting, commit message linting, and periodic health reporting.
- Align agent-facing and planning docs with the implemented quality baseline.

## Capabilities

### New Capabilities

- `quality-baseline`: Repository-level formatting, static analysis, linting, and dependency-health entry points with CI coverage.

### Modified Capabilities

- None.

## Impact

- Affects the root build, build-logic conventions, and `.github/workflows`.
- Affects local developer validation commands and CI entry points.
- Affects future changes to module participation in quality and dependency-health checks.
