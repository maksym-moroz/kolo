## Why

The repo is still a thin KMP starter and has no agreed persistence contract, yet offline-first tasks, reminders, journaling, and settings all depend on durable local storage. Persistence needs to be planned before feature work so repositories, reminder logic, migrations, and release hardening are built on stable boundaries instead of ad hoc storage choices.

## What Changes

- Introduce a `local-persistence` capability that defines how the app stores relational feature data, settings, reminder definitions, and sync metadata on device.
- Establish a split between Room KMP for relational data and DataStore KMP for small user/device preferences.
- Define migration, schema versioning, testing, and release requirements for persistence changes.
- Create a ticket-shaped backlog for persistence foundation work, including setup, schema modeling, repository boundaries, reminder-time correctness, and migration coverage.

## Capabilities

### New Capabilities

- `local-persistence`: Durable offline storage contracts for tasks, journal entries, reminder definitions, settings, sync metadata, and schema evolution.

### Modified Capabilities

- None.

## Impact

- Affects shared KMP architecture and future module layout.
- Affects version catalog and build setup for Room KMP, SQLite driver, DataStore KMP, and KSP.
- Affects future feature modules for tasks, reminders, journaling, settings, and sync.
- Defines release-critical quality gates around migrations, timezone-safe reminders, and offline restore.
