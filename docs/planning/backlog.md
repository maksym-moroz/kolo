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

### NAV-001: Lock navigation domain model and ownership boundaries

- Status: Ready
- Owner: Unassigned
- Write scope: [workstreams/navigation.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/navigation.md), [starter-architecture.md](/Users/maksymmoroz/startup/kolo/docs/planning/starter-architecture.md), and this file
- Dependencies: FND-003
- Goal: prevent app-global, modal, and feature-local navigation from collapsing into one vague stack
- Rich description:
  - define typed app routes, modal presentation types, and feature-local flow contracts
  - define app boundary versus feature boundary ownership
  - define what must be serializable for restoration
- Done when:
  - route taxonomy is named
  - ownership boundaries are explicit
  - serialization expectations are explicit
  - modal taxonomy is listed with ownership and stackability rules
  - feature export contract is explicit
  - v1 root-stack identity model is explicit

### NAV-002: Lock back, modal, and result semantics

- Status: Ready
- Owner: Unassigned
- Write scope: [workstreams/navigation.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/navigation.md), [starter-architecture.md](/Users/maksymmoroz/startup/kolo/docs/planning/starter-architecture.md), and this file
- Dependencies: NAV-001
- Goal: make back handling and modal behavior intentional before feature flows spread
- Rich description:
  - define back precedence across modal, feature-local, and app stacks
  - define predictive back and iOS swipe-back expectations
  - define typed child-flow result handling
- Done when:
  - back resolver policy is explicit
  - modal dismissal policy is explicit
  - result model is explicit
  - resolver ownership is explicit
  - gesture policy is explicit
  - feature-local flow instance rules are explicit

### NAV-003: Lock deep-link, restoration, and guard model

- Status: Ready
- Owner: Unassigned
- Write scope: [workstreams/navigation.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/navigation.md) and this file
- Dependencies: NAV-001, NAV-002
- Goal: unify external entry, restored entry, and guarded transitions under one typed navigation model
- Rich description:
  - define deep-link registry and parser boundary
  - define restoration behavior for stacks, modals, and feature-local flows
  - define guard/interceptor handling for auth, unsaved changes, and invalid payloads
- Done when:
  - deep-link policy is explicit
  - restoration rules are explicit
  - guard model is explicit
  - precedence between fresh external entry and restored state is explicit
  - safe-root behavior is explicit
  - observability requirements are explicit

### NAV-004: Define migration path, validation matrix, and first implementation cut

- Status: Ready
- Owner: Unassigned
- Write scope: [workstreams/navigation.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/navigation.md) and this file
- Dependencies: NAV-001, NAV-002, NAV-003
- Goal: turn the navigation design into a safe rollout plan instead of a one-shot rewrite
- Rich description:
  - define the root route-engine boundary and first host integration point
  - define the first feature slice to migrate
  - define validation expectations for reducer, coordinator, back, modal, deep-link, and restoration behavior
- Done when:
  - rollout order is explicit
  - first migration cut is explicit
  - validation matrix is explicit
  - rollback path is explicit
  - legacy/new coexistence rules are explicit
  - readiness gate is explicit

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

## Debug Menu / Runtime Config Hardening

### DBG-001: Make debug-menu state truthful and environment restart conditional

- Status: Done
- Owner: Codex
- Write scope: `debugmenu/**`
- Dependencies: none
- Goal: stop the debug menu from rendering bundled fallback values as truth and only restart the app when the environment actually changes
- Done when:
  - the debug menu has explicit pre-snapshot behavior instead of seeding from bundled config
  - selecting the already-active environment does not write an override or restart the app
  - tests cover first-snapshot and same-environment behavior
- Notes:
  - this ticket intentionally stays inside the current debug-menu module even though the longer-term shape may simplify further

### DBG-002: Collapse debug-menu command handling into one honest pipeline

- Status: Done
- Owner: Codex
- Write scope: `debugmenu/src/commonMain/kotlin/com/focus/kolo/debugmenu/**`
- Dependencies: DBG-001
- Goal: remove ceremonial per-action effect fan-out and make one explicit command path own debug-menu mutations
- Done when:
  - debug-menu mutations flow through one explicit command handler path
  - the no-op action tracker is removed
  - mutation failure handling is either surfaced intentionally or deleted intentionally

### CFG-001: Move runtime-config override source lifetime ownership to the graph edge

- Status: Done
- Owner: Codex
- Write scope: `shared/core/config/impl/**`, `shared/src/*Main/**`
- Dependencies: none
- Goal: remove self-owned long-lived coroutine scope from runtime-config persistence wiring
- Done when:
  - no runtime-config repository or data source creates its own lifetime `CoroutineScope`
  - scope ownership is explicit at the graph or app-runtime edge, or the flow remains cold
  - validation covers persisted override replay
- Notes:
  - closed by investigation because the current Android DataStore override source already exposes a cold `Flow` and no longer owns a long-lived scope

### CFG-002: Make Android graph ownership explicit for debug-menu dependencies

- Status: Done
- Owner: Codex
- Write scope: `apps/reference/androidApp/**`, `shared/src/androidMain/**`
- Dependencies: CFG-001
- Goal: stop creating debug-menu dependencies ad hoc from `Context` and make Android graph ownership explicit
- Done when:
  - the debug menu consumes app-owned Android dependencies
  - no debug-menu entry point creates a fresh app graph directly from `Context`
  - the dependency handoff boundary is obvious from Android entry code

### DBG-003: Tighten debug-menu entry policy and config surface

- Status: Done
- Owner: Codex
- Write scope: `debugmenu/**`, `apps/reference/androidApp/src/debug/**`, `shared/core/config/**`
- Dependencies: DBG-001, CFG-002
- Goal: treat the debug menu as internal tooling rather than product routing and remove self-referential config noise
- Done when:
  - the debug-menu entry path is intentionally scoped for internal tooling
  - `debugMenuEnabled` either has a real consumer or is removed from the config surface
  - docs and code agree on what the debug-menu boundary actually is

### DOC-001: Align runtime-config and debug-menu docs with the minimal invariant architecture

- Status: Done
- Owner: Codex
- Write scope: `docs/planning/**`, `openspec/changes/runtime-config-debug-menu/**`
- Dependencies: DBG-002, CFG-001, CFG-002, DBG-003
- Goal: stop the durable docs from prescribing a fake feature-state machine for the debug menu
- Done when:
  - planning and OpenSpec docs describe the debug menu as an observer-plus-command surface
  - docs no longer imply that shared use cases own Android restart behavior
  - backlog sequencing reflects the hardening pass

### DBG-004: Add iOS Compose debug-menu host and internal URI entry

- Status: Done
- Owner: Codex
- Write scope: `composeApp/**`, `apps/reference/iosApp/**`, `shared/src/iosMain/**`, `shared/core/config/impl/src/iosMain/**`, and architecture docs
- Dependencies: DBG-002, CFG-002, DBG-003
- Goal: expose the runtime-config debug menu on iOS through a Compose-backed internal tooling surface instead of leaving the capability Android-only
- Done when:
  - iOS can open the debug menu through the internal `kolo://debug/menu` entry path
  - the iOS host presents a Compose-backed debug menu surface
  - iOS runtime-config overrides persist locally instead of staying in-memory only
  - durable docs stop claiming the debug menu is Android-only

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

### NAV-001: Lock navigation domain model and ownership boundaries

- Status: Ready
- Owner: Unassigned
- Write scope: [workstreams/navigation.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/navigation.md), [starter-architecture.md](/Users/maksymmoroz/startup/kolo/docs/planning/starter-architecture.md), and this file
- Dependencies: FND-003
- Goal: prevent app-global, modal, and feature-local navigation from collapsing into one vague stack
- Rich description:
  - define typed app routes, modal presentation types, and feature-local flow contracts
  - define app boundary versus feature boundary ownership
  - define what must be serializable for restoration
- Done when:
  - route taxonomy is named
  - ownership boundaries are explicit
  - serialization expectations are explicit
  - modal taxonomy is listed with ownership and stackability rules
  - feature export contract is explicit
  - v1 root-stack identity model is explicit

### NAV-002: Lock back, modal, and result semantics

- Status: Ready
- Owner: Unassigned
- Write scope: [workstreams/navigation.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/navigation.md), [starter-architecture.md](/Users/maksymmoroz/startup/kolo/docs/planning/starter-architecture.md), and this file
- Dependencies: NAV-001
- Goal: make back handling and modal behavior intentional before feature flows spread
- Rich description:
  - define back precedence across modal, feature-local, and app stacks
  - define predictive back and iOS swipe-back expectations
  - define typed child-flow result handling
- Done when:
  - back resolver policy is explicit
  - modal dismissal policy is explicit
  - result model is explicit
  - resolver ownership is explicit
  - gesture policy is explicit
  - feature-local flow instance rules are explicit

### NAV-003: Lock deep-link, restoration, and guard model

- Status: Ready
- Owner: Unassigned
- Write scope: [workstreams/navigation.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/navigation.md) and this file
- Dependencies: NAV-001, NAV-002
- Goal: unify external entry, restored entry, and guarded transitions under one typed navigation model
- Rich description:
  - define deep-link registry and parser boundary
  - define restoration behavior for stacks, modals, and feature-local flows
  - define guard/interceptor handling for auth, unsaved changes, and invalid payloads
- Done when:
  - deep-link policy is explicit
  - restoration rules are explicit
  - guard model is explicit
  - precedence between fresh external entry and restored state is explicit
  - safe-root behavior is explicit
  - observability requirements are explicit

### NAV-004: Define migration path, validation matrix, and first implementation cut

- Status: Ready
- Owner: Unassigned
- Write scope: [workstreams/navigation.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/navigation.md) and this file
- Dependencies: NAV-001, NAV-002, NAV-003
- Goal: turn the navigation design into a safe rollout plan instead of a one-shot rewrite
- Rich description:
  - define the root route-engine boundary and first host integration point
  - define the first feature slice to migrate
  - define validation expectations for reducer, coordinator, back, modal, deep-link, and restoration behavior
- Done when:
  - rollout order is explicit
  - first migration cut is explicit
  - validation matrix is explicit
  - rollback path is explicit
  - legacy/new coexistence rules are explicit
  - readiness gate is explicit

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
