# Backlog

This is the shared delivery backlog for the starter app. Tickets are intentionally shaped for agent ownership and parallel execution.

## Priority Order

1. Foundation and module split
2. Local persistence
3. Navigation and deep links
4. Reminder delivery
5. Feature slices
6. Server starter
7. Offline sync
8. Release hardening

## Ready Queue

### PERS-001: Define v1 persistence domain model

- Status: Ready
- Owner: Unassigned
- Write scope: persistence planning docs only
- Dependencies: FND-001
- Goal: lock the first durable storage model for tasks, journals, reminders, settings, and sync metadata
- Done when:
  - entities are named
  - repository boundaries are named
  - out-of-scope storage concerns are listed

### NAV-001: Define route model and back behavior

- Status: Ready
- Owner: Unassigned
- Write scope: navigation planning docs only
- Dependencies: FND-001
- Goal: prevent ad hoc navigation and modal handling
- Done when:
  - route types are named
  - modal rules are defined
  - back behavior is defined for Android and iOS

### REM-001: Define reminder scheduling contract

- Status: Ready
- Owner: Unassigned
- Write scope: reminder planning docs only
- Dependencies: PERS-001
- Goal: separate stored reminder intent from platform delivery mechanics
- Done when:
  - shared reminder model is defined
  - Android and iOS scheduler boundaries are named
  - reschedule triggers are listed

## Backlog By Epic

## Foundation

### FND-001: Define AGP 9 migration target structure

- Status: Done
- Owner: Unassigned
- Write scope: [foundation/agp9-migration.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/agp9-migration.md), [starter-architecture.md](/Users/maksymmoroz/startup/kolo/docs/planning/starter-architecture.md), and root Gradle planning notes
- Dependencies: none
- Goal: lock the future structure before implementation begins
- Done when:
  - target modules are named
  - current modules are mapped to future modules
  - migration phases are listed
  - risks and cut lines are explicit
  - the first implementation step is clear
  - migration order is agreed

### FND-002: Define version catalog baseline

- Status: Done
- Owner: Unassigned
- Write scope: [foundation/version-baseline.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/version-baseline.md) and version catalog planning notes
- Dependencies: FND-001
- Goal: pin supported versions and risky edges
- Done when:
  - version ranges or exact versions are documented
  - locked versus flexible versions are called out
  - compatibility constraints are documented
  - upgrade risks are flagged

### FND-003: Define shared UDF/store contract

- Status: Done
- Owner: Unassigned
- Write scope: [foundation/udf-store-contract.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/udf-store-contract.md), [starter-architecture.md](/Users/maksymmoroz/startup/kolo/docs/planning/starter-architecture.md), `shared/core/store/api/**`, and `shared/core/store/impl/**`
- Dependencies: FND-001
- Goal: lock the common `Intent -> Action -> Middleware -> Reducer -> UiState/UiEffect` model
- Done when:
  - store primitives are named
  - ViewModel boundary is documented
  - side-effect policy is explicit
  - baseline shared runtime exists in dedicated store modules

## Persistence

### PERS-001: Define v1 persistence domain model

- Status: Ready
- Owner: Unassigned
- Write scope: [workstreams/persistence.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/persistence.md)
- Dependencies: FND-001
- Goal: lock the first durable storage model
- Done when:
  - entities and repositories are named
  - storage split between Room and DataStore is explicit

### PERS-002: Define migration and schema policy

- Status: Ready
- Owner: Unassigned
- Write scope: [workstreams/persistence.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/persistence.md)
- Dependencies: PERS-001
- Goal: make migration behavior part of the baseline
- Done when:
  - schema export policy is defined
  - destructive fallback policy is defined
  - test expectations are listed

### PERS-003: Define reminder time persistence model

- Status: Ready
- Owner: Unassigned
- Write scope: [workstreams/persistence.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/persistence.md)
- Dependencies: PERS-001
- Goal: avoid timezone and DST bugs caused by a weak stored model
- Done when:
  - wall-clock semantics are defined
  - timezone data is included
  - recalculation triggers are defined

### PERS-004: Define repository and transaction boundaries

- Status: Ready
- Owner: Unassigned
- Write scope: [workstreams/persistence.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/persistence.md)
- Dependencies: PERS-001
- Goal: keep DAOs and storage details out of feature code
- Done when:
  - repository names are final
  - transaction ownership is explicit

## Navigation

### NAV-001: Define route model and back behavior

- Status: Ready
- Owner: Unassigned
- Write scope: planning docs only
- Dependencies: FND-001
- Goal: define deep links, modals, and back behavior before UI grows
- Done when:
  - route types are named
  - modal routing policy is explicit
  - predictive back and iOS swipe-back expectations are documented

### NAV-002: Define deep-link registry and validation rules

- Status: Draft
- Owner: Unassigned
- Write scope: planning docs only
- Dependencies: NAV-001
- Goal: keep deep links typed and safe
- Done when:
  - accepted hosts and schemes are listed
  - invalid input behavior is defined

## Reminders

### REM-001: Define reminder scheduling contract

- Status: Ready
- Owner: Unassigned
- Write scope: planning docs only
- Dependencies: PERS-003
- Goal: define the shared reminder scheduler abstraction
- Done when:
  - Android and iOS scheduling responsibilities are split
  - fallback behavior is defined

### REM-002: Define notification tap routing rules

- Status: Draft
- Owner: Unassigned
- Write scope: planning docs only
- Dependencies: REM-001, NAV-001
- Goal: make notification taps predictable and testable
- Done when:
  - tap payload model is defined
  - navigation outcomes are defined

## Features

### FEAT-001: Define tasks v1 slice

- Status: Draft
- Owner: Unassigned
- Write scope: planning docs only
- Dependencies: FND-003, PERS-001
- Goal: define the first end-to-end task feature slice
- Done when:
  - task state, intents, actions, and repository calls are listed

### FEAT-002: Define journaling v1 slice

- Status: Draft
- Owner: Unassigned
- Write scope: planning docs only
- Dependencies: FND-003, PERS-001
- Goal: define the first journal feature slice
- Done when:
  - journal state, intents, actions, and repository calls are listed

## Server

### SRV-001: Define Ktor server starter contract

- Status: Draft
- Owner: Unassigned
- Write scope: planning docs only
- Dependencies: FND-002
- Goal: pin the minimum useful server starter
- Done when:
  - typed routes, auth, validation, status pages, and OpenAPI expectations are documented

### SRV-002: Define app-config endpoint contract

- Status: Draft
- Owner: Unassigned
- Write scope: planning docs only
- Dependencies: SRV-001
- Goal: support force update and mobile bootstrap config
- Done when:
  - `latestVersion` and `minimumSupportedVersion` are defined

## Sync

### SYNC-001: Define offline sync boundary

- Status: Draft
- Owner: Unassigned
- Write scope: planning docs only
- Dependencies: PERS-004, SRV-001
- Goal: define what stays local and what becomes sync-aware
- Done when:
  - local dirty-state or operation-log direction is chosen

## Quality

### QLT-001: Define release checklist

- Status: Draft
- Owner: Unassigned
- Write scope: planning docs only
- Dependencies: NAV-001, REM-001, PERS-002
- Goal: make release blockers explicit early
- Current Android baseline:
  - `:androidApp:assembleRelease` and `:androidApp:bundleRelease` should stay green in health checks
  - release `mapping.txt` is retained as a build artifact
  - release bundle size is tracked in CI
  - Baseline Profiles are generated through `:androidApp:generateBaselineProfile` and should be refreshed after startup or major navigation-path changes
- Done when:
  - migrations, deep links, reminder permissions, taps, accessibility, and offline restore checks are listed

## Suggested First Iteration

This is the safest first planning wave for parallel agents:

- Agent A: `FND-001`
- Agent B: `PERS-001`
- Agent C: `NAV-001`
- Agent D: `REM-001` after `PERS-001` lands

This keeps write scopes mostly separate while building the decisions that later implementation depends on.
