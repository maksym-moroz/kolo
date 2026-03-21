## Context

The current repository is still close to the default Kotlin Multiplatform template: shared code is minimal, no persistence libraries are configured, and there is no agreed data boundary for tasks, reminders, journaling, or settings. At the same time, the target product is explicitly offline-first, which makes persistence a foundation concern rather than a later optimization.

There are also structural constraints that shape the design:

- The app is Kotlin Multiplatform-first, so feature storage contracts must live in shared code.
- AGP 9 migration is already a known concern for the repo, so persistence work should fit a future `androidApp + KMP libraries` split instead of reinforcing the current template layout.
- Reminder behavior is time-sensitive across timezone and DST changes, which means storage decisions are not just schema decisions.
- Future sync is expected, but the immediate goal is local usefulness before backend complexity.

## Goals / Non-Goals

**Goals:**

- Define a persistence foundation that supports offline-first tasks, reminders, journaling, and settings.
- Keep persistence contracts in shared Kotlin so feature stores and use cases are platform-agnostic.
- Use boring, testable defaults: Room KMP for relational data and DataStore KMP for preferences.
- Make schema evolution and migration testing part of the initial plan instead of post-hoc cleanup.
- Preserve reminder intent across app restarts, timezone changes, and DST boundaries.
- Produce a backlog that can be executed incrementally without blocking the entire starter on sync or auth.

**Non-Goals:**

- Implementing persistence code in this change.
- Designing the full sync protocol or server persistence model.
- Finalizing encryption, biometrics, or export/import flows.
- Designing every feature UI state or navigation contract.

## Decisions

### 1. Use separate stores for relational data and preferences

The starter will use Room KMP as the primary app database for tasks, journal entries, reminder definitions, and sync metadata. DataStore KMP will be used for small user/device preferences such as theme, week start, locale-adjacent settings, and default reminder offsets.

Why:

- Tasks, journals, reminders, and sync metadata are relational and versioned.
- Preferences change frequently but do not justify relational schema churn.
- This keeps settings migration simpler and avoids bloating the primary database.

Alternatives considered:

- SQLDelight for the primary database: viable, but not aligned with the requested stack.
- Storing everything in Room: increases schema churn for small preference changes.
- Storing everything in DataStore: too weak for relational queries, transactions, and migration discipline.

### 2. Keep database bootstrap split between platform path resolution and shared builder configuration

The persistence plan assumes a platform-specific database path provider via `expect`/`actual`, while the Room builder configuration stays in shared code. The common builder configuration will use the bundled SQLite driver and shared coroutine/query configuration.

Why:

- File location is platform-specific.
- Driver selection, schema configuration, and builder policies should stay common.
- This keeps persistence testable and aligned across Android and iOS.

Alternatives considered:

- Full platform-specific builders: too much duplication.
- Platform SQLite drivers by default: acceptable, but less consistent than the bundled driver.

### 3. Hide DAOs and storage details behind shared repositories

Feature code will depend on repositories such as `TaskRepository`, `JournalRepository`, `ReminderRepository`, `SettingsRepository`, and `SyncStateRepository`, not on DAOs or preference keys directly.

Why:

- Feature reducers and use cases should remain portable across UI choices.
- Repositories are the right place for transaction boundaries, mapping, and local-first semantics.
- This keeps future sync and conflict logic from leaking into feature code.

Alternatives considered:

- Exposing DAOs directly to features: faster at first, but creates irreversible coupling.
- A generic repository abstraction: usually too vague and becomes an anemic wrapper.

### 4. Persist reminder intent, not just a computed fire timestamp

Reminder storage will preserve user intent as explicit fields such as local date, local time, timezone identifier, repeat rule, and scheduling status. Derived values such as next scheduled instant may be cached, but they are not the only source of truth.

Why:

- Bare `LocalDateTime` is insufficient across timezone and DST changes.
- Storing only an epoch timestamp loses the distinction between wall-clock reminders and absolute reminders.
- Platform schedulers need a stable handoff model that can be recalculated after reboot, timezone change, or restore.

Alternatives considered:

- Persist only `Instant`: too lossy for local wall-clock semantics.
- Persist only rule text and calculate everything at runtime: workable, but harder to inspect and debug.

### 5. Treat schema history and migration tests as release gates

Every persistence schema change will export schema history to version control. Migration tests will be required for release-bound schema changes, and destructive fallback will remain a development-only option.

Why:

- Migration safety is part of the product contract for a starter app that claims offline-first durability.
- Schema files create reviewable artifacts and reduce accidental breakage.
- This avoids a common failure mode where migrations are postponed until data already matters.

Alternatives considered:

- Deferring migration tests until later: faster initially, but risky once features land.
- Relying on destructive reset in early releases: acceptable for local prototypes, not for a real starter template.

### 6. Plan persistence work in phases that unblock feature development early

The backlog is organized to land persistence in a sequence:

1. Module and dependency foundation.
2. Shared data model and schema contracts.
3. Room and DataStore wiring.
4. Repository and transaction boundaries.
5. Reminder-time correctness and scheduler handoff.
6. Migration, fixtures, and release hardening.

Why:

- It creates a usable persistence spine early without forcing sync or notifications to be solved all at once.
- It gives feature work a clear insertion point.

## Risks / Trade-offs

- [AGP 9 migration friction] -> Keep persistence planning compatible with a future module split and avoid doubling down on the current wizard layout.
- [Room KMP + KSP complexity across targets] -> Keep target list explicit, export schemas, and add platform/bootstrap work early rather than late.
- [Timezone and DST bugs in reminders] -> Persist reminder intent explicitly and add deterministic time-zone tests before release.
- [Over-modeling the schema too early] -> Start with a boring v1 model for tasks, reminders, journals, settings, and sync state only.
- [DAO leakage into feature code] -> Require repository-only access from shared stores and use cases.
- [Future sync needs different metadata] -> Reserve explicit sync state storage now, but keep server protocol decisions out of scope.

## Migration Plan

1. Confirm the persistence capability scope and target modules.
2. Align dependency catalog and build plugins with Room KMP, bundled SQLite, DataStore KMP, and required test tooling.
3. Define shared persistence models, identifiers, and repository interfaces.
4. Add database/bootstrap boundaries and preferences storage boundaries.
5. Add migration tests, repository tests, time-based reminder tests, and seed fixtures.
6. Integrate feature slices onto repositories after the persistence spine is stable.

Rollback strategy:

- Before production data exists, rollback is a repository revert plus schema cleanup.
- Once release candidates exist, rollback must preserve forward-migrated user data and cannot rely on destructive reset.

## Open Questions

- Do journal entries need rich-text or attachment support in the first persistence model?
- Will recurring reminders support exceptions, snooze history, or completion history in v1?
- Do we need local full-text search at launch, or is exact/tag filtering enough initially?
- What sync metadata is necessary now versus later: dirty flags only, or operation log as well?
- Is secure storage limited to auth/session secrets, or do any persisted feature fields require encryption at rest?
