## ADDED Requirements

### Requirement: Effective runtime config is exposed as one reactive typed model
The system SHALL expose one reactive effective runtime config through `AppConfigRepo`, and production consumers SHALL read effective config from that contract instead of reading individual sources directly.

#### Scenario: Consumer observes current app config
- **WHEN** a production feature starts observing runtime config
- **THEN** it receives a typed effective `AppConfig` from `AppConfigRepo`

#### Scenario: Consumer remains unaware of debug-only mutation APIs
- **WHEN** production code depends on runtime config
- **THEN** it SHALL not require debug-only override mutation capabilities from the repo contract

### Requirement: Effective config merges defaults, remote overrides, and local overrides
The system SHALL resolve the effective app config by merging full default config with mocked remote overrides and persisted local overrides, with local overrides taking highest precedence.

#### Scenario: No remote or local overrides exist
- **WHEN** remote config returns no override and no local override is stored
- **THEN** the effective `AppConfig` SHALL equal the default config

#### Scenario: Remote override exists without a local override
- **WHEN** a remote override provides values for a subset of config fields
- **THEN** the effective `AppConfig` SHALL use remote values for those fields and default values for all others

#### Scenario: Local override exists for the same field as a remote override
- **WHEN** both remote and local overrides provide a value for the same config field
- **THEN** the effective `AppConfig` SHALL use the local override value

### Requirement: Runtime config supports the proposal-one starter categories
The system SHALL model feature flags, version policy, URLs/constants, and environment selection as typed runtime config fields.

#### Scenario: Feature flags are part of effective config
- **WHEN** the effective config is resolved
- **THEN** it SHALL include typed feature flags for `tasksEnabled`, `journalEnabled`, `remindersEnabled`, `forcedUpdateEnabled`, and `deeplinkHandlingEnabled`

#### Scenario: Version policy is part of effective config
- **WHEN** the effective config is resolved
- **THEN** it SHALL include typed version policy values for `latestVersion` and `minimumSupportedVersion`

#### Scenario: URLs/constants are part of effective config
- **WHEN** the effective config is resolved
- **THEN** it SHALL include typed URL/config values for `supportUrl` and `privacyPolicyUrl`

#### Scenario: Environment selection is part of effective config
- **WHEN** the effective config is resolved
- **THEN** it SHALL include a typed selected environment with predefined `dev` and `prod` options

### Requirement: Local overrides persist on device and can override every in-scope field
The system SHALL persist local overrides on device, and the local override layer SHALL be able to override any runtime-config field included in this proposal.

#### Scenario: App restarts after a local override is saved
- **WHEN** the app starts after a previously saved local override exists
- **THEN** the effective `AppConfig` SHALL include that persisted override

#### Scenario: Override storage uses the settings-store path
- **WHEN** local overrides are persisted on device in proposal one
- **THEN** the system SHALL store them through DataStore rather than a relational database

#### Scenario: User overrides a non-environment field
- **WHEN** a local override is saved for a feature flag, version-policy field, or URL/config field
- **THEN** the persisted local override SHALL become the effective value for that field

### Requirement: Mocked remote config participates in the architecture
The system SHALL include a remote-config source boundary in proposal one, and its initial implementation SHALL return no remote overrides.

#### Scenario: Initial remote source is used
- **WHEN** the runtime-config system resolves remote overrides in proposal one
- **THEN** the remote source SHALL return an empty override instead of performing real backend fetches

### Requirement: Non-environment changes apply immediately
The system SHALL apply persisted local override changes immediately for all in-scope fields other than environment selection.

#### Scenario: Feature flag override changes
- **WHEN** a local feature-flag override is saved
- **THEN** `AppConfigRepo` SHALL emit an updated effective `AppConfig` without requiring app restart

#### Scenario: URL or version-policy override changes
- **WHEN** a local URL/config or version-policy override is saved
- **THEN** `AppConfigRepo` SHALL emit an updated effective `AppConfig` without requiring app restart

### Requirement: Environment switching restarts the app cleanly
The system SHALL persist environment selection as a local override, and changing the selected environment SHALL trigger a clean app restart through Process Phoenix at the Android boundary while preserving all other local overrides.

#### Scenario: User switches from dev to prod
- **WHEN** the selected environment changes from `dev` to `prod`
- **THEN** the system SHALL persist the `prod` override and restart the app through Process Phoenix

#### Scenario: User switches from prod to dev
- **WHEN** the selected environment changes from `prod` to `dev`
- **THEN** the system SHALL persist the `dev` override and restart the app through Process Phoenix

#### Scenario: Other overrides survive environment switching
- **WHEN** the selected environment changes and other local overrides already exist
- **THEN** the app SHALL preserve those other local overrides across the restart
