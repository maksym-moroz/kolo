## 1. Documentation Structure

- [x] 1.1 Create a foundation-level document for the repository quality-tooling map
- [x] 1.2 Choose the durable source-of-truth location and link it from the planning index
- [x] 1.3 Keep `README.md` and `AGENTS.md` summary-oriented and point them at the detailed map

## 2. Quality Implementation Map

- [x] 2.1 Document root orchestration through `kolo.root`, the root `kolo {}` block, and the aggregate tasks `qualityCheck`, `qualityFix`, and `dependencyHealth`
- [x] 2.2 Document module-level quality behavior created by build-logic conventions, including Detekt, Android lint, and dependency analysis participation
- [x] 2.3 Document the source-of-truth files for root orchestration, module conventions, and internal quality helpers

## 3. CI Workflow Map

- [x] 3.1 Document which GitHub workflows cover quality, dependency health, workflow linting, commit linting, and health reporting
- [x] 3.2 Map each workflow to the root tasks or Gradle commands it executes
- [x] 3.3 Document the default local commands that correspond to CI checks

## 4. Update Rules And Validation

- [x] 4.1 Document the triggers that require the quality-tooling map to be updated after future build or workflow changes
- [x] 4.2 Align `README.md`, `AGENTS.md`, and planning docs with the new detailed map
- [x] 4.3 Validate the final documentation with `./gradlew qualityCheck`
