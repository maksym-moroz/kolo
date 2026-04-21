# App Factory Workflow

This document describes the intended flow for launching a new app from the Kolo baseline.

## Goal

Create a new app shell with minimal hand wiring and predictable inherited quality.

## First-Cut Workflow

1. Create a new shell under `apps/<app-id>/`
2. Add `app-definition.yaml`
3. Copy or template the Android and iOS host shells from `apps/reference/`
4. Rename package ids, bundle ids, and display metadata
5. Select enabled capabilities
6. Bind app-specific integrations at the shell edge
7. Wire observability keys and environment config
8. Run baseline validation

## Baseline Validation

At minimum, a new shell should prove:

- Android host assembles
- shared Kotlin compiles for iOS
- capability composition is valid
- smoke flows run for the chosen feature set

## Reference Rule

`apps/reference/` stays as the truth source for shell structure until the creation flow is automated further.

Do not optimize this with a generator until the shell contract has survived at least one additional real app.
