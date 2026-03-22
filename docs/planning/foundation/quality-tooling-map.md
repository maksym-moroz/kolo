# Quality Tooling Map

This document is the durable map for how CI, formatting, static analysis, linting, and dependency health are wired in this repo.

Use it when you need to:

- understand what `qualityCheck`, `qualityFix`, or `dependencyHealth` actually do
- add or remove a module from repository quality checks
- change module-level Detekt, Android lint, or dependency-analysis behavior
- locate the baseline profile generation entry point and its owning module
- update GitHub workflows without drifting from local developer commands

## Source Of Truth

Root orchestration:

- root build entry: [build.gradle.kts](/Users/maksymmoroz/startup/kolo/build.gradle.kts)
- root editor config: [.editorconfig](/Users/maksymmoroz/startup/kolo/.editorconfig)
- root convention plugin: [KoloRootConventionPlugin.kt](/Users/maksymmoroz/startup/kolo/build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/root/KoloRootConventionPlugin.kt)
- root extension: [KoloRootExtension.kt](/Users/maksymmoroz/startup/kolo/build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/root/KoloRootExtension.kt)

Module quality behavior:

- shared helpers: [QualityConventions.kt](/Users/maksymmoroz/startup/kolo/build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/convention/internal/QualityConventions.kt)
- Spotless ktlint overrides: [KoloRootConventionPlugin.kt](/Users/maksymmoroz/startup/kolo/build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/root/KoloRootConventionPlugin.kt)
- shared Detekt overrides: [config/detekt/detekt.yml](/Users/maksymmoroz/startup/kolo/config/detekt/detekt.yml)
- Android app convention: [KoloAndroidApplicationConventionPlugin.kt](/Users/maksymmoroz/startup/kolo/build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/android/KoloAndroidApplicationConventionPlugin.kt)
- base Android-KMP library convention: [KoloAndroidKotlinMultiplatformLibraryConventionPlugin.kt](/Users/maksymmoroz/startup/kolo/build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/android/KoloAndroidKotlinMultiplatformLibraryConventionPlugin.kt)
- server convention: [KoloServerJvmConventionPlugin.kt](/Users/maksymmoroz/startup/kolo/build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/server/KoloServerJvmConventionPlugin.kt)

Workflow definitions:

- quality workflow: [quality.yml](/Users/maksymmoroz/startup/kolo/.github/workflows/quality.yml)
- dependency analysis workflow: [dependency-health.yml](/Users/maksymmoroz/startup/kolo/.github/workflows/dependency-health.yml)
- health workflow: [health.yml](/Users/maksymmoroz/startup/kolo/.github/workflows/health.yml)
- workflow lint: [actionlint.yml](/Users/maksymmoroz/startup/kolo/.github/workflows/actionlint.yml)
- commit message lint: [commitlint.yml](/Users/maksymmoroz/startup/kolo/.github/workflows/commitlint.yml)
- baseline profile generator: [baselineprofile/build.gradle.kts](/Users/maksymmoroz/startup/kolo/baselineprofile/build.gradle.kts)
- baseline profile test: [BaselineProfileGenerator.kt](/Users/maksymmoroz/startup/kolo/baselineprofile/src/main/kotlin/com/focus/kolo/baselineprofile/BaselineProfileGenerator.kt)

## Root Orchestration

The root project owns three aggregate entry points:

- `qualityCheck`
- `qualityFix`
- `dependencyHealth`

These are exposed by `kolo.root`, not hand-built in the root script.

The root [build.gradle.kts](/Users/maksymmoroz/startup/kolo/build.gradle.kts) does two things:

- applies `kolo.root` and `spotless`
- declares which modules participate in aggregate checks through the `kolo {}` block

Current root participation lists:

- `qualityModules`
  - `:androidApp`
  - `:composeApp`
  - `:server`
  - `:shared`
  - `:shared:core:store:api`
  - `:shared:core:store:impl`
- `dependencyHealthModules`
  - `:androidApp`
  - `:server`

Aggregate task behavior:

- `qualityCheck`
  - runs root `spotlessCheck`
  - runs `:shared:core:store:api:apiCheck`
  - runs each participating module's `:module:qualityCheck`
- `qualityFix`
  - runs root `spotlessApply`
- `dependencyHealth`
  - runs `:module:projectHealth` only for modules listed in `dependencyHealthModules`

## Module Quality Behavior

Each module-level `qualityCheck` is created by build-logic conventions.

Shared behavior from [QualityConventions.kt](/Users/maksymmoroz/startup/kolo/build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/convention/internal/QualityConventions.kt):

- registers a module-local `qualityCheck` task
- applies Detekt
- configures Detekt source scope, reports, base path, JVM target, and shared config overrides from `config/detekt/detekt.yml`
- wires Detekt into the module `check` task
- wires Detekt into the module `qualityCheck` task

Repository-wide style policy from [.editorconfig](/Users/maksymmoroz/startup/kolo/.editorconfig):

- ktlint uses the `android_studio` code-style baseline with a shared `140` character line-length limit for Kotlin and Gradle Kotlin files
- ktlint disables trailing commas in declarations and call sites so IDE formatting, Spotless, and review diffs stay consistent
- ktlint allows `@Composable` functions to use UpperCamelCase names
- ktlint keeps expression-body functions on the same line as `=` when the first expression line still fits by setting `ktlint_function_signature_body_expression_wrapping = default`
- ktlint forces class and constructor signatures to multiline from `1` parameter and function signatures from `2` parameters, matching the formatting baseline used in `~/work/autodoc/autodoc-android`
- ktlint keeps chained calls like `.map` and `.catch` on clean continuation lines by combining the Android Studio code style with the shared chain-method continuation overrides
- ktlint disables `no-line-break-before-assignment` and `multiline-expression-wrapping` so multiline expressions do not fight the formatter
- Spotless applies the same ktlint editor-config overrides directly in the root convention plugin so Gradle formatting stays aligned with the repo policy
- Detekt keeps the same `140` character line-length limit, leaves function naming to ktlint to avoid conflicting Compose rules, and ignores data classes in `LongParameterList` so constructor layout stays formatter-led

Convention-specific additions:

- Android application convention
  - adds `lintDebug` to module `qualityCheck`
  - applies dependency analysis
- base Android-KMP library convention
  - adds `lintAnalyzeAndroidHostTest` to module `qualityCheck` when present
- server JVM convention
  - applies dependency analysis
- compose/shared conventions
  - inherit the base Android-KMP library quality behavior

What stays module-local:

- `namespace`
- target declarations such as `iosArm64()` and `iosSimulatorArm64()`
- source-set dependencies
- Android-KMP-specific options such as `withHostTest {}` or `androidResources { enable = true }`

Baseline Profile generation is separate from aggregate quality checks:

- `androidApp` applies the Baseline Profile plugin and packages generated rules in release artifacts
- `baselineprofile` is a dedicated `com.android.test` producer module and is generated on demand rather than through `qualityCheck`

## Workflow To Task Map

`quality.yml`

- runs `./gradlew qualityCheck`
- this is the main CI entry point for formatting, Detekt, module lint, and API check coverage

`dependency-health.yml`

- runs `./gradlew dependencyHealth`
- uploads dependency-analysis reports for the participating modules

`health.yml`

- measures:
  - `:shared:core:store:impl:jvmTest`
  - `:androidApp:assembleDebug`
  - `:androidApp:assembleRelease`
  - `:androidApp:bundleRelease`
  - `:shared:compileKotlinIosSimulatorArm64`
- publishes timing plus Android release AAB-size summary artifacts
- uploads:
  - debug APK
  - unsigned release APK
  - release AAB
  - release `mapping.txt` for R8 output inspection

`actionlint.yml`

- validates the GitHub workflow files themselves

`commitlint.yml`

- validates commit messages in PRs and on `main`

## Default Local Commands

Use these before changing the quality system:

- `./gradlew qualityCheck`
- `./gradlew qualityFix`
- `./gradlew dependencyHealth`
- `./gradlew help`
- `./gradlew test :androidApp:assembleDebug :androidApp:assembleRelease :androidApp:bundleRelease :shared:compileKotlinIosSimulatorArm64`
- `./gradlew :androidApp:generateBaselineProfile`

## Update Rules

Update this document when any of these change:

- root aggregate task behavior in `kolo.root`
- module participation in `kolo { qualityModules / dependencyHealthModules }`
- module convention responsibilities for Detekt, Android lint, or dependency analysis
- workflow commands, triggers, or artifact outputs
- source-of-truth file locations in `build-logic`

Also update the summaries in:

- [README.md](/Users/maksymmoroz/startup/kolo/README.md)
- [AGENTS.md](/Users/maksymmoroz/startup/kolo/AGENTS.md)
- [docs/planning/README.md](/Users/maksymmoroz/startup/kolo/docs/planning/README.md) when the planning entry points need to change
