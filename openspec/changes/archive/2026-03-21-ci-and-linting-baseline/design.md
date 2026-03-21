## Context

The repository quality baseline is already implemented. Root orchestration now lives in the `kolo.root` plugin, module-level Detekt and quality task wiring live in build-logic conventions, Spotless is configured at the root, and GitHub workflows call stable aggregate tasks instead of bespoke command lists.

This is cross-cutting because it spans:

- root Gradle orchestration
- module convention plugins
- GitHub Actions workflows
- agent-facing documentation and local validation habits

The purpose of this design is to capture the implemented architecture, not to invent a new one.

## Goals / Non-Goals

**Goals:**

- Record the implemented quality baseline as an OpenSpec design artifact.
- Make the root-versus-module execution boundary explicit.
- Preserve the aggregate task model and workflow entry points now in use.
- Define the intended ownership split between root orchestration and module conventions.

**Non-Goals:**

- Replacing Spotless, Detekt, Android lint, or dependency analysis.
- Redesigning GitHub workflows in this change.
- Introducing a new CI provider or release pipeline.
- Expanding the quality baseline beyond what is already implemented.

## Decisions

### 1. Use root aggregate tasks as the public entry points

The quality baseline is centered on three root tasks:

- `qualityCheck`
- `qualityFix`
- `dependencyHealth`

Why:

- local validation and CI should share the same entry points
- root orchestration reduces duplicated workflow commands
- module participation can change without renaming the public commands

Alternatives considered:

- invoke individual tool tasks directly in CI: too brittle and harder to evolve
- create one monolithic shell script entry point: weaker Gradle integration and task visibility

### 2. Put module quality behavior in convention plugins

Module-level `qualityCheck` tasks are created by build-logic conventions, with Detekt always wired in and Android lint or dependency analysis added only where appropriate.

Why:

- quality behavior should follow module type, not manual copy/paste in each module
- the repo already uses build-logic as the source of truth for shared plugin wiring
- this keeps future modules aligned with less per-module setup

Alternatives considered:

- configure all quality behavior from the root project: too much type branching in one place
- configure every module build file independently: too much duplication

### 3. Keep Spotless at the root

Spotless remains root-owned because it formats repository-wide file sets, not just per-module source roots.

Why:

- Kotlin Gradle DSL files and misc repository files are root concerns
- `qualityFix` and `qualityCheck` need stable root formatting entry points

### 4. Match workflows to aggregate tasks

GitHub Actions workflows call root tasks or specific health measurements rather than reimplementing quality logic.

Why:

- this keeps CI closer to local validation behavior
- task ownership remains inside Gradle/build-logic

## Risks / Trade-offs

- [Root orchestration drifts from module conventions] → Keep module-quality behavior inside build-logic and only let the root declare participation lists.
- [Workflow commands drift from local commands] → Keep workflows mapped to root aggregate tasks and document the mapping.
- [Quality participation changes become invisible] → Use the root `kolo {}` block as the explicit participation list and document it.

## Migration Plan

1. Record the implemented baseline as a completed change.
2. Sync the resulting capability spec into `openspec/specs`.
3. Keep future quality-system changes layered on top of this recorded baseline.

Rollback strategy:

- documentation-only rollback; the implemented code baseline already exists independently

## Open Questions

- Should future CI-only enhancements extend `quality-baseline`, or should they be split into narrower capabilities once release automation grows further?
