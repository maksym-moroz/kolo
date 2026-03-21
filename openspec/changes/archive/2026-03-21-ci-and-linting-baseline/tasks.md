## 1. Root Quality Baseline

- [x] 1.1 Establish root aggregate tasks for `qualityCheck`, `qualityFix`, and `dependencyHealth`
- [x] 1.2 Configure root Spotless formatting for Kotlin, Gradle Kotlin DSL, and misc repository files
- [x] 1.3 Keep the root build as the explicit place where participating modules are listed for aggregate quality tasks

## 2. Module Convention Wiring

- [x] 2.1 Add shared build-logic helpers for module-level Detekt wiring and `qualityCheck` task registration
- [x] 2.2 Wire Android application modules so `qualityCheck` includes Android lint and dependency analysis where appropriate
- [x] 2.3 Wire Android-KMP library and server conventions so quality behavior follows module type instead of per-module duplication

## 3. CI Workflow Coverage

- [x] 3.1 Add a repository quality workflow that runs `./gradlew qualityCheck`
- [x] 3.2 Add a dependency health workflow that runs `./gradlew dependencyHealth`
- [x] 3.3 Add supporting workflow lint, commitlint, and health-reporting workflows for the implemented baseline

## 4. Validation And Documentation

- [x] 4.1 Validate the implemented baseline with local Gradle quality and cross-platform build commands
- [x] 4.2 Update repo and planning docs so future agents can find the implemented quality baseline
- [x] 4.3 Record the implemented baseline in OpenSpec as a completed change
