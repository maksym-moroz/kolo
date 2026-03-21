## Context

The repository already implements a multi-layer quality baseline:

- root orchestration lives in the `kolo.root` convention plugin and the root `kolo {}` block
- module-level quality entry points are created by build-logic conventions
- formatting is owned by Spotless at the root
- Detekt is wired per module through shared build-logic helpers
- Android lint and dependency analysis are enabled only where they make sense
- GitHub Actions workflows run top-level entry points such as `qualityCheck` and `dependencyHealth`

This is already functional, but the knowledge is distributed across:

- `build.gradle.kts`
- `build-logic/convention/**`
- `.github/workflows/**`
- `README.md`
- `AGENTS.md`

That makes the current setup understandable only after code inspection. The change here is not to redesign the tooling, but to create a durable explanation of the implemented shape for future agents.

## Goals / Non-Goals

**Goals:**

- Add one durable map that explains how the implemented CI and linting baseline is wired.
- Make the root-versus-module quality boundary explicit.
- Document the mapping between local commands and GitHub workflows.
- Define what docs need to be updated when quality tooling changes.

**Non-Goals:**

- Changing which tasks CI runs.
- Replacing Spotless, Detekt, Android lint, or dependency analysis.
- Redesigning the build-logic package layout again.
- Introducing new repository quality gates in this change.

## Decisions

### 1. Put the detailed map in planning docs, not only in README or AGENTS

The detailed implementation map should live in `docs/planning/foundation/` as a durable cross-cutting foundation doc, while `README.md` and `AGENTS.md` stay summary-oriented and link to it.

Why:

- the topic is foundational and cross-cutting
- README should stay lightweight for repo entry
- AGENTS should remain a short map, not a full operating manual

Alternatives considered:

- keep all detail in `AGENTS.md`: too dense and harder to maintain
- keep all detail in `README.md`: not specific enough for agent implementation work

### 2. Document the system by execution boundary

The map should explain the quality system in this order:

1. root orchestration (`kolo.root`, `qualityCheck`, `qualityFix`, `dependencyHealth`)
2. module-level convention behavior (`qualityCheck`, Detekt, Android lint, dependency analysis)
3. workflow mapping in `.github/workflows`
4. local validation commands and update rules

Why:

- this matches how the system actually runs
- it clarifies which concerns are root-only versus module-owned
- it helps agents debug the correct layer first

Alternatives considered:

- organize by tool vendor (Spotless, Detekt, Gradle, GitHub Actions): less useful for repo maintenance
- organize by file tree alone: too implementation-shaped and weaker as an operational guide

### 3. Make source-of-truth files explicit

The map should point directly at:

- root build entry points
- root convention plugin and extension
- module convention plugins
- internal quality helpers
- GitHub workflow files

Why:

- future agents need precise jump points
- the build-logic package split is now feature-based, so durable links matter

### 4. Define update triggers for documentation

The map should explicitly state that it must be updated when:

- root quality entry points change
- a module enters or leaves `qualityCheck` or `dependencyHealth`
- workflow tasks or triggers change
- convention-plugin responsibility boundaries move

Why:

- quality docs drift easily because the build keeps working until someone needs to extend it
- these triggers are concrete enough to be followed in normal maintenance

## Risks / Trade-offs

- [Docs become a stale mirror of code] → Keep the map focused on execution boundaries and source-of-truth file links rather than copying implementation line by line.
- [Too much duplication across README, AGENTS, and planning docs] → Keep the detailed explanation in one foundation doc and reduce the others to summaries plus links.
- [Agents still miss workflow-to-task relationships] → Include a direct workflow-to-local-command mapping section.

## Migration Plan

1. Create a new foundation-level quality-tooling map document.
2. Document the implemented root orchestration, module conventions, and workflow mapping.
3. Link the map from `README.md`, `AGENTS.md`, and planning index docs where appropriate.
4. Validate Markdown formatting and repository quality checks.

Rollback strategy:

- revert the new documentation files and links without affecting build behavior

## Open Questions

- Should future CI architecture changes get their own planning workstream, or is a foundation-level map sufficient as long as the system stays modest?
