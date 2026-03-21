# Persistence Workstream

This document turns the persistence strategy into a workstream another agent can pick up without re-reading prior conversation.

## Outcome

Create a persistence foundation for an offline-first KMP starter app that supports:

- tasks
- reminders
- journaling
- settings
- future sync metadata

## Current Recommendation

Use:

- Room KMP for relational app data
- bundled SQLite driver by default
- DataStore KMP for preferences

Keep persistence in shared Kotlin behind repositories. Platform code should only handle file paths, scheduler bridges, and other platform-only concerns.

## What Must Be True

1. The app can reconstruct local state after relaunch without a network call.
2. Schema changes can be migrated forward safely in release builds.
3. Reminder definitions survive timezone and DST changes without losing user intent.
4. Feature code does not depend on DAOs or preference keys directly.
5. Repository tests and migration tests exist before release candidates.

## Proposed Storage Split

### Room KMP

Use Room KMP for:

- task records
- task completion state
- task notes if kept relational
- journal entries
- reminder definitions
- reminder scheduling metadata
- sync metadata such as dirty state or local sync markers

### DataStore KMP

Use DataStore KMP for:

- theme
- week start
- default reminder offset
- local feature flags or small device settings

## V1 Entity Direction

These names are a starting point, not final code contracts:

- `TaskEntity`
- `TaskReminderEntity`
- `JournalEntryEntity`
- `SettingsSnapshot` or equivalent preferences model
- `SyncStateEntity`

Likely repository boundaries:

- `TaskRepository`
- `ReminderRepository`
- `JournalRepository`
- `SettingsRepository`
- `SyncStateRepository`

## Reminder Time Model

Persist reminder intent explicitly.

Recommended fields:

- logical reminder ID
- target local date
- target local time
- timezone ID
- repeat rule
- next scheduled instant if cached
- scheduling state
- last scheduler sync timestamp if useful

Avoid relying on only:

- bare `LocalDateTime`
- bare epoch milliseconds

Those are not enough to preserve wall-clock reminder intent.

## Migration Policy

Use these rules:

- export Room schemas from the start
- store schema history in version control
- destructive fallback is allowed only in local development, not release builds
- every release-bound schema change needs a migration test

## Repository Policy

Repositories own:

- mapping between storage and domain models
- transaction boundaries
- local-first read behavior
- preparation for future sync metadata

Feature stores should not:

- call DAOs directly
- know preference keys
- calculate SQL-specific behavior

## Risks

### Risk: Schema churn before feature shapes stabilize

Mitigation:

- keep v1 schema narrow
- defer search, attachments, and advanced sync metadata unless needed

### Risk: Reminder bugs caused by weak time modeling

Mitigation:

- persist timezone-aware reminder intent
- test DST and timezone transitions explicitly

### Risk: Room KMP setup complexity across targets

Mitigation:

- keep the target list explicit
- define bootstrap boundaries early
- avoid hiding setup decisions in feature modules

## Ticket Slice

### PERS-001: Define v1 persistence domain model

- Status: Ready
- Owner: Unassigned
- Write scope: this file and [backlog.md](/Users/maksymmoroz/startup/kolo/docs/planning/backlog.md)
- Dependencies: FND-001
- Goal: define the first durable model for local app data
- Done when:
  - Room-owned entities are named
  - DataStore-owned settings are named
  - repository boundaries are listed

### PERS-002: Define migration and schema policy

- Status: Ready
- Owner: Unassigned
- Write scope: this file and [backlog.md](/Users/maksymmoroz/startup/kolo/docs/planning/backlog.md)
- Dependencies: PERS-001
- Goal: make migration policy explicit before schema implementation
- Done when:
  - schema export policy is explicit
  - destructive fallback rule is explicit
  - migration test requirement is explicit

### PERS-003: Define reminder time persistence model

- Status: Ready
- Owner: Unassigned
- Write scope: this file and [backlog.md](/Users/maksymmoroz/startup/kolo/docs/planning/backlog.md)
- Dependencies: PERS-001
- Goal: lock a safe time model for reminders
- Done when:
  - stored fields are listed
  - recalculation triggers are listed
  - invalid assumptions are called out

### PERS-004: Define repository and transaction boundaries

- Status: Ready
- Owner: Unassigned
- Write scope: this file and [backlog.md](/Users/maksymmoroz/startup/kolo/docs/planning/backlog.md)
- Dependencies: PERS-001
- Goal: keep persistence implementation details behind shared repositories
- Done when:
  - repository ownership is listed
  - transaction ownership is listed
  - feature-layer restrictions are listed

## Deferred Questions

- Does journaling need attachments in v1?
- Do recurring reminders need exception dates in v1?
- Is sync metadata just dirty state initially, or an operation log?
- Do any feature fields require encryption at rest, or only auth/session secrets?
