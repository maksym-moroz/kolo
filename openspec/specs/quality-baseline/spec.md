## ADDED Requirements

### Requirement: Root quality entry points
The system SHALL expose stable root tasks for repository quality validation, quality fixes, and dependency health analysis.

#### Scenario: Developer runs repository quality checks
- **WHEN** a developer or CI job runs the root quality command
- **THEN** the repository executes the aggregate quality task rather than requiring callers to know each underlying formatting, static-analysis, or lint task individually

### Requirement: Module-level quality integration
The system SHALL create module-level quality tasks through build-logic conventions and SHALL wire module-appropriate analysis into those tasks.

#### Scenario: Module participates in quality baseline
- **WHEN** a supported module applies the relevant repository convention plugin
- **THEN** the module exposes a `qualityCheck` task that includes the quality tools appropriate for that module type

### Requirement: CI uses repository quality entry points
The system SHALL provide workflow coverage for repository quality, dependency health, and related linting concerns using stable repository entry points.

#### Scenario: CI validates repository quality
- **WHEN** the quality workflow runs in GitHub Actions
- **THEN** it executes the repository quality entry point rather than a drifting ad hoc command list
