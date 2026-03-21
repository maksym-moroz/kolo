## ADDED Requirements

### Requirement: Durable offline app data
The system SHALL persist tasks, journal entries, reminder definitions, and sync metadata locally so the app can reconstruct feature state after app termination or device restart without requiring a network call.

#### Scenario: Relaunch without network
- **WHEN** the user reopens the app while offline after previously creating or editing local data
- **THEN** the app loads the last committed local data from device storage and renders the relevant feature state

### Requirement: Separated storage roles
The system SHALL store relational feature data in the primary database and SHALL store small user or device preferences in a separate preferences store.

#### Scenario: Preferences update
- **WHEN** the user changes a setting such as theme, week start, or default reminder offset
- **THEN** the value is persisted without requiring a relational schema change or database migration

### Requirement: Versioned storage migration
The system SHALL version local storage schemas and SHALL migrate existing user data forward without destructive reset in release builds.

#### Scenario: App upgrade with existing data
- **WHEN** the app launches after upgrading to a version with a newer persistence schema
- **THEN** stored data is migrated to the new schema and remains readable by the updated app

### Requirement: Time-safe reminder definitions
The system SHALL persist reminder intent in a form that remains interpretable across timezone and daylight saving changes.

#### Scenario: Timezone changes before reminder fires
- **WHEN** a user creates a reminder tied to a local wall-clock time and the device timezone changes before the reminder fires
- **THEN** the system can recalculate the next delivery time from the persisted reminder definition without losing user intent

### Requirement: Storage abstraction boundary
The system SHALL expose shared repository contracts to feature layers and SHALL NOT require feature code to depend directly on database or preferences implementation details.

#### Scenario: Shared feature consumes persisted data
- **WHEN** a feature store or use case loads or saves data
- **THEN** it does so through a shared repository interface rather than direct DAO or preference-key access

### Requirement: Persistence test coverage
The system SHALL provide automated tests for storage schema evolution, repository behavior, and reminder-time calculations.

#### Scenario: Migration regression check
- **WHEN** a schema change is introduced
- **THEN** automated tests verify migration behavior against stored schema history before release
