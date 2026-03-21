## ADDED Requirements

### Requirement: Durable quality tooling map
The system SHALL provide a durable agent-facing map that explains how repository quality tooling is implemented across root orchestration, module conventions, and CI workflows.

#### Scenario: Agent needs the quality system entry points
- **WHEN** an agent needs to understand how formatting, static analysis, linting, or dependency analysis are wired
- **THEN** the documentation identifies the root orchestration layer, the module convention layer, and the relevant source-of-truth files

### Requirement: Workflow to task mapping
The system SHALL document which repository workflows run which root or module quality entry points and how those workflows relate to local validation commands.

#### Scenario: Agent compares local validation with CI
- **WHEN** an agent needs to verify whether a local command matches CI behavior
- **THEN** the documentation shows the mapping between local commands such as `./gradlew qualityCheck` or `./gradlew dependencyHealth` and the GitHub workflow files that run them

### Requirement: Quality tooling update rules
The system SHALL document when the quality-tooling map and summary docs must be updated after build or workflow changes.

#### Scenario: Quality entry point changes
- **WHEN** a change adds, removes, or rewires a root quality task, participating module, or CI workflow
- **THEN** the documentation states that the detailed map and linked summary docs must be updated to reflect the new behavior
