## 1. Foundation

- [ ] 1.1 PERS-001 Confirm the v1 persistence scope for tasks, reminders, journaling, settings, and sync metadata
- [ ] 1.2 PERS-002 Define the target module layout for persistence code and how it fits the AGP 9 module split
- [ ] 1.3 PERS-003 Add version-catalog and plugin entries for Room KMP, bundled SQLite, DataStore KMP, KSP, and persistence test libraries
- [ ] 1.4 PERS-004 Document ownership boundaries between shared repositories, platform bootstrap code, and feature modules

## 2. Shared Data Contracts

- [ ] 2.1 PERS-101 Define the shared persistence glossary and naming rules for entities, models, IDs, and timestamps
- [ ] 2.2 PERS-102 Model persisted data for tasks, journal entries, reminder definitions, settings, and sync state
- [ ] 2.3 PERS-103 Define deletion, archive, and restore semantics for persisted feature data
- [ ] 2.4 PERS-104 Define repository interfaces and transaction boundaries for shared feature code

## 3. Database Foundation

- [ ] 3.1 PERS-201 Create the Room KMP database boundary and platform-specific database path/bootstrap contract
- [ ] 3.2 PERS-202 Configure schema export and commit schema history to version control
- [ ] 3.3 PERS-203 Add the initial database version and core tables for tasks, journal entries, reminders, and sync metadata
- [ ] 3.4 PERS-204 Define DAO boundaries, write transactions, and mapping rules from storage models to shared domain models

## 4. Settings Storage

- [ ] 4.1 PERS-301 Create the DataStore KMP boundary for small user and device preferences
- [ ] 4.2 PERS-302 Define preference keys, defaults, and migration rules for theme, week start, and default reminder behavior
- [ ] 4.3 PERS-303 Implement a shared settings repository contract that hides preference-key details from feature code

## 5. Reminder Persistence

- [ ] 5.1 PERS-401 Finalize the persisted reminder time model for local wall-clock reminders versus absolute-time reminders
- [ ] 5.2 PERS-402 Define how repeat rules, timezone identifiers, last scheduled instant, and next fire calculation inputs are stored
- [ ] 5.3 PERS-403 Define reschedule triggers for app restart, device reboot, timezone change, DST boundary, and user edits
- [ ] 5.4 PERS-404 Define the handoff contract between persisted reminder data and platform scheduler implementations

## 6. Quality Gates

- [ ] 6.1 PERS-501 Add migration tests for every release-bound schema change
- [ ] 6.2 PERS-502 Add repository tests with deterministic clock and timezone injection
- [ ] 6.3 PERS-503 Add realistic persistence fixtures for tasks, journals, reminders, and settings
- [ ] 6.4 PERS-504 Add failure-path tests for corrupted data, partial writes, and unavailable scheduler dependencies

## 7. Release Readiness

- [ ] 7.1 PERS-601 Define release rules that disable destructive fallback for production builds
- [ ] 7.2 PERS-602 Add persistence-specific release checks for offline restore, migration safety, and timezone-aware reminder recalculation
- [ ] 7.3 PERS-603 Define observability hooks for persistence errors, migration failures, and reminder reschedule failures

## 8. Deferred Backlog

- [ ] 8.1 PERS-701 Evaluate full-text search support for tasks and journal entries
- [ ] 8.2 PERS-702 Evaluate attachment and media persistence for journal entries
- [ ] 8.3 PERS-703 Evaluate encryption-at-rest boundaries beyond auth and session secrets
- [ ] 8.4 PERS-704 Evaluate whether future sync needs dirty flags only or an operation log
