## ADDED Requirements

### Requirement: Platform hosts provide an internal debug menu for runtime config
The system SHALL provide platform-hosted debug-menu tooling surfaces for viewing and editing runtime config, and those surfaces SHALL remain separate from normal production feature surfaces.

#### Scenario: Debug builds expose debug menu entry surfaces
- **WHEN** a platform debug build includes the debug menu feature
- **THEN** the app SHALL provide an internal tooling surface for interacting with runtime config

#### Scenario: Release build hides debug menu
- **WHEN** the app is built for release
- **THEN** the debug menu SHALL not exist as a normal product-facing surface

#### Scenario: Production code does not depend on debug-menu internals
- **WHEN** production features consume runtime config
- **THEN** they SHALL depend on shared runtime-config contracts rather than debug-menu-specific classes

### Requirement: Debug menu keeps runtime-config truth in the shared repo
Platform debug menus SHALL observe the effective runtime config from the shared runtime-config repo and SHALL issue explicit override commands instead of inventing separate persisted debug-menu business state.

#### Scenario: Debug menu feature is defined
- **WHEN** the debug menu is implemented
- **THEN** its screen logic SHALL reflect the current effective `AppConfig` truthfully and keep platform-specific restart handling at the platform boundary

### Requirement: Debug menu displays effective runtime config
The debug menu SHALL display the current effective runtime config values that the app is actually using.

#### Scenario: User opens the debug menu
- **WHEN** the debug menu screen renders
- **THEN** it SHALL show the current selected environment, current feature-flag values, current version policy, and current configured URLs/constants from the effective `AppConfig`

### Requirement: Debug menu is reachable through an internal URI entry
Platform debug menus SHALL be reachable through internal URI entry paths in the first iteration.

#### Scenario: Developer opens the debug menu through the debug entry path
- **WHEN** a developer invokes the configured internal URI entry path
- **THEN** the app SHALL present the debug menu in debug builds

### Requirement: Debug menu edits persisted local overrides
The debug menu SHALL update persisted local overrides through use cases rather than mutating effective config directly.

#### Scenario: User toggles a feature flag
- **WHEN** the user changes a feature-flag value in the debug menu
- **THEN** the debug menu SHALL persist a local override through the runtime-config mutation use case path

#### Scenario: User edits version policy or URL/config values
- **WHEN** the user changes a version-policy or URL/config value in the debug menu
- **THEN** the debug menu SHALL persist a local override through the runtime-config mutation use case path

### Requirement: URL/config values are visible but read-only in the first debug-menu UI
The first debug-menu UI SHALL display URL/config values from the effective config, but it SHALL not allow editing those values in-place.

#### Scenario: User inspects URLs/constants
- **WHEN** the debug menu renders the URL/config section
- **THEN** it SHALL display the effective values without presenting an edit control for those fields

### Requirement: Debug menu supports environment selection between dev and prod
The debug menu SHALL allow the user to select either `dev` or `prod` as the active environment.

#### Scenario: User views environment choices
- **WHEN** the debug menu renders environment selection controls
- **THEN** it SHALL present both `dev` and `prod` as selectable options

#### Scenario: User selects a new environment
- **WHEN** the user selects a different environment from the one currently active
- **THEN** the debug menu SHALL persist the environment override through the runtime-config mutation path and the Android boundary SHALL restart the app cleanly

### Requirement: Debug menu can clear persisted overrides
The debug menu SHALL support clearing individual overrides and resetting all local overrides.

#### Scenario: User clears one override
- **WHEN** the user clears a specific local override from the debug menu
- **THEN** the effective `AppConfig` SHALL fall back to the next available source for that field

#### Scenario: User resets all overrides
- **WHEN** the user resets all local overrides from the debug menu
- **THEN** the effective `AppConfig` SHALL fall back to defaults plus any remote overrides
