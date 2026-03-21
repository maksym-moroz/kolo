## Why

The repo already has a working CI and linting baseline, but the implementation knowledge is split across GitHub workflows, the root `kolo.root` plugin, module convention plugins, and a few summary docs. Agents can follow the current commands, but extending or debugging the quality setup still requires reverse-engineering how the pieces fit together.

## What Changes

- Introduce a durable `quality-tooling-map` capability that documents how CI, formatting, static analysis, linting, and dependency health are implemented in this repo.
- Add an agent-facing map that explains the relationship between root orchestration, module-level `qualityCheck` tasks, build-logic conventions, and GitHub workflows.
- Define the source-of-truth locations and update rules for future changes to quality tooling so docs stay aligned with code.
- Align README, planning docs, and AGENTS guidance so they point at the detailed map instead of duplicating partial explanations.

## Capabilities

### New Capabilities

- `quality-tooling-map`: Durable documentation for how repository quality tooling and CI workflows are wired and how agents should update them.

### Modified Capabilities

- None.

## Impact

- Affects agent-facing documentation and planning docs.
- Affects how future contributors navigate `build.gradle.kts`, `kolo.root`, module `qualityCheck` tasks, and `.github/workflows`.
- Reduces the chance of quality-tooling regressions caused by incomplete or stale operational knowledge.
