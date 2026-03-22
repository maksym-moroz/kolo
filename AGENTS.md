# AGENTS.md

This file is the shortest durable map of how to work effectively in this repo.

It is intentionally map-based:

- point agents to the real source of truth
- keep only the rules and facts that are easy to forget
- avoid duplicating large specs that will drift

If this file starts reading like a full design document, it is too big.

## 1. Source Of Truth Map

Read these in order when starting work:

1. `docs/planning/README.md`
2. `docs/planning/backlog.md`
3. `docs/planning/starter-architecture.md`
4. the relevant workstream or foundation doc for the ticket

High-value deep docs:

- `docs/planning/foundation/agp9-migration.md`
- `docs/planning/foundation/code-style.md`
- `docs/planning/foundation/quality-tooling-map.md`
- `docs/planning/foundation/udf-store-contract.md`
- `docs/planning/foundation/version-baseline.md`
- `docs/planning/workstreams/persistence.md`
- `docs/planning/multi-agent-workflow.md`

Rule:

- `AGENTS.md` tells you where to look
- `docs/planning/*` tells you what the project believes
- code and build files tell you what is actually implemented

## 2. Repo Map

Current top-level structure:

- `androidApp/`: Android application entry point
- `baselineprofile/`: Android Baseline Profile generator module
- `build-logic/`: included Gradle build for convention plugins
- `composeApp/`: KMP UI library, currently the Compose-facing UI surface
- `debugmenu/`: shared KMP debug-menu feature module
- `docs/planning/`: planning, backlog, architecture, and workstreams
- `iosApp/`: iOS host app and SwiftUI host shell
- `openspec/`: change proposals, task tracking, and archive/spec artifacts
- `server/`: Ktor server module
- `shared/`: shared KMP library for app graph and remaining common code
- `shared/core/config/`: extracted runtime-config API and implementation modules
- `shared/core/store/`: extracted store API and runtime modules

Current architectural reality:

- AGP 9 migration first cut is done
- `build-logic` is the source of truth for shared Gradle plugin wiring
- `androidApp` is the real Android app boundary
- `baselineprofile` generates Baseline Profiles for the Android app and is not a shipping app module
- shared runtime config now lives in `shared:core:config:api` and `shared:core:config:impl`
- the debug menu now lives in the shared `debugmenu` KMP module, with thin Android and iOS hosts at the app edge
- `composeApp` is not the Android application module anymore
- the runtime-config read contract remains in shared Kotlin, while debug-only mutation wiring is exposed through platform graphs at the app edge
- the store contract has already been extracted into `shared:core:store:api` and `shared:core:store:impl`
- `shared` is still broad outside the extracted store modules and has not yet been split into the rest of `shared/core/*` and `shared/feature/*`
- the root build exposes `qualityCheck`, `qualityFix`, and `dependencyHealth`
- some planning docs still describe the pre-`androidApp` or future split state; prefer code and build files when they disagree

## 3. Code Ownership Map

If a task touches these areas, start here:

### Build logic and plugin wiring

- included build: `build-logic/settings.gradle.kts`
- convention plugin build: `build-logic/convention/build.gradle.kts`
- base KMP Android library convention: `build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/android/KoloAndroidKotlinMultiplatformLibraryConventionPlugin.kt`
- Android app convention: `build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/android/KoloAndroidApplicationConventionPlugin.kt`
- Android Compose library convention: `build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/android/KoloAndroidComposeLibraryConventionPlugin.kt`
- Compose KMP convention: `build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/compose/KoloComposeMultiplatformConventionPlugin.kt`
- shared KMP convention: `build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/shared/KoloSharedMultiplatformConventionPlugin.kt`
- server convention: `build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/server/KoloServerJvmConventionPlugin.kt`
- convention helpers: `build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/convention/internal/`

### Android app boundary

- build: `androidApp/build.gradle.kts`
- entry point: `androidApp/src/main/kotlin/com/focus/kolo/MainActivity.kt`
- manifest: `androidApp/src/main/AndroidManifest.xml`

### Android baseline profiles

- build: `baselineprofile/build.gradle.kts`
- generator: `baselineprofile/src/main/kotlin/com/focus/kolo/baselineprofile/BaselineProfileGenerator.kt`

### Runtime config and debug menu

- config API build: `shared/core/config/api/build.gradle.kts`
- config API effective models and read contract: `shared/core/config/api/src/commonMain/kotlin/com/focus/kolo/config/`
- config impl build: `shared/core/config/impl/build.gradle.kts`
- config impl overrides: `shared/core/config/impl/src/commonMain/kotlin/com/focus/kolo/config/impl/override/`
- config impl repo/defaults: `shared/core/config/impl/src/commonMain/kotlin/com/focus/kolo/config/impl/repo/`
- config impl local and remote sources: `shared/core/config/impl/src/commonMain/kotlin/com/focus/kolo/config/impl/source/`
- platform DataStore path/source wiring: `shared/core/config/impl/src/androidMain/kotlin/com/focus/kolo/config/impl/source/local/` and `shared/core/config/impl/src/iosMain/kotlin/com/focus/kolo/config/impl/source/local/`
- config impl use cases: `shared/core/config/impl/src/commonMain/kotlin/com/focus/kolo/config/impl/usecase/`
- shared debug-menu module build: `debugmenu/build.gradle.kts`
- Android debug-menu host activity entry: `androidApp/src/debug/kotlin/com/focus/kolo/debugmenu/`
- shared debug-menu feature sources: `debugmenu/src/commonMain/kotlin/com/focus/kolo/debugmenu/`
- iOS debug-menu controller factory: `debugmenu/src/iosMain/kotlin/com/focus/kolo/debugmenu/`
- debug-menu deep-link manifest wiring: `androidApp/src/debug/AndroidManifest.xml`
- iOS debug-menu host wiring and URL handling: `iosApp/iosApp/`

### Shared UI library

- build: `composeApp/build.gradle.kts`
- current UI entry: `composeApp/src/androidMain/kotlin/com/focus/kolo/App.kt`

### Shared store modules

- API build: `shared/core/store/api/build.gradle.kts`
- API sources: `shared/core/store/api/src/commonMain/kotlin/com/focus/kolo/store/`
- impl build: `shared/core/store/impl/build.gradle.kts`
- impl runtime sources: `shared/core/store/impl/src/commonMain/kotlin/com/focus/kolo/store/impl/`
- impl tests: `shared/core/store/impl/src/commonTest/kotlin/com/focus/kolo/store/`

### Shared Kotlin and DI

- build: `shared/build.gradle.kts`
- shared service map: `shared/src/commonMain/kotlin/com/focus/kolo/AppGraph.kt`
- Metro graphs:
  - `shared/src/androidMain/kotlin/com/focus/kolo/AndroidAppGraph.kt`
  - `shared/src/iosMain/kotlin/com/focus/kolo/IosAppGraph.kt`

### iOS host

- app shell: `iosApp/iosApp/iOSApp.swift`
- current screen: `iosApp/iosApp/ContentView.swift`

### Server

- build: `server/build.gradle.kts`
- entry point: `server/src/main/kotlin/com/focus/kolo/Application.kt`

### Versions and root build

- version catalog: `gradle/libs.versions.toml`
- convention build: `build-logic/convention/`
- root external plugin classpath aliases: `build.gradle.kts`
- root convention plugin: `build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/root/KoloRootConventionPlugin.kt`
- root extension: `build-logic/convention/src/main/kotlin/com/focus/kolo/buildlogic/root/KoloRootExtension.kt`
- root quality tasks: `qualityCheck`, `qualityFix`, `dependencyHealth` exposed by `kolo.root`
- settings and included build wiring: `settings.gradle.kts`

## 4. Common Practice Map

These are the current project defaults unless a ticket explicitly changes them.

### Build and structure

- follow `docs/planning/foundation/code-style.md` for repository-specific style rules that go beyond formatter defaults
- keep platform entry points thin
- keep reusable logic in shared Kotlin
- prefer boring module boundaries over clever indirection
- keep shared Gradle setup in `build-logic` convention plugins rather than repeating plugin stacks in module build files
- keep module build scripts focused on namespace, target-specific compiler settings, and direct dependencies
- keep Baseline Profile generation isolated in `baselineprofile` instead of mixing generator code into `androidApp`
- keep shared runtime-config contracts in `shared:core:config:api`, runtime wiring in `shared:core:config:impl`, and keep platform entry and hosting details out at the Android/iOS app edge
- do not reintroduce the old KMP + Android app pairing in `composeApp`
- never put several top-level classes, interfaces, or objects in one Kotlin file
- prefer one top-level type per file, especially for actions, effects, state, reducers, and middleware

### Dependency injection

- Metro is the DI baseline
- keep the base graph contract in `commonMain`
- keep final `@DependencyGraph` types in platform source sets when platform bindings differ
- add platform-only bindings at the platform graph edge

### Architecture

- use unidirectional data flow
- keep ViewModel thin
- persistent state belongs in shared feature/store layers, not platform entry points
- one-off events should not be modeled as persistent state
- repositories and data aggregators must not create their own lifetime `CoroutineScope`; scope ownership and any `stateIn`/sharing policy belong at the graph or feature-runtime edge
- config repositories and override sources should expose plain `Flow` contracts unless a caller has a real need for hot shared state at its own runtime edge
- if the intended architecture or requested pattern is unclear, stop and ask a concise clarifying question instead of guessing and implementing an inferred design
- when an interface exposes a `StateFlow` or `SharedFlow`, implementations must override that property directly with the mutable flow they own instead of keeping separate mutable and read-only mirror fields
- never introduce `private val mutableX` plus `override val x = mutableX.asStateFlow()` or `asSharedFlow()` patterns in implementation code
- use `shared:core:store:api` rather than inventing feature-local store primitives
- prefer the shared generic store contract over feature-specific store types unless the generic store is no longer sufficient for the slice
- feature slices, including internal tooling like the Android debug menu, should follow the same `IntentMapper -> Store -> Middleware -> Reducer -> UiState/UiEffect` shape rather than bypassing the shared store contract
- action effects belong behind middleware and should consume the store action stream there, not be launched directly from a ViewModel
- depend on `shared:core:store:impl` only where runtime wiring is actually needed
- structure packages so they can be extracted into future modules with minimal redesign

### Persistence direction

- Room KMP is the intended app database
- DataStore KMP is the intended settings store
- reminder time must be timezone-safe
- migration tests are part of the baseline, not an optional later hardening task

### Release hardening

- Baseline Profiles are part of the Android release-performance baseline
- keep the profile generator focused on startup and the most important user journeys
- regenerate profiles after meaningful Android startup or navigation-path changes

### Runtime config

- production code should read runtime config from the shared repo contract, not from debug-only mutation surfaces
- keep effective config read models in `shared:core:config:api` and keep override, mutation, and merge mechanics in `shared:core:config:impl`
- within config impl, keep overrides under `impl/override`, source adapters under `impl/source`, repo assembly under `impl/repo`, and mutation entry points under `impl/usecase`
- local runtime-config overrides belong in DataStore-backed settings storage, not the database
- environment switching is restart-sensitive and should preserve other overrides

### Multi-agent work

- one ticket, one owner, one declared write scope
- if write scope overlaps, do not parallelize
- update planning docs when architecture or sequencing changes

### GitHub presentation

- emojis are allowed in GitHub-facing markdown and workflow/job/step names
- do not use emojis in commit messages
- keep commit messages conventional and machine-friendly even if GitHub UI text is more expressive

## 5. Ticket Map

Current sequence from the backlog:

1. `PERS-001` to `PERS-004`: persistence foundation
2. `NAV-001`: route model and back behavior
3. `REM-001`: reminder scheduling contract
4. first feature slices on top of the shared store contract

When picking up a ticket:

1. find it in `docs/planning/backlog.md`
2. read every linked file in its write scope
3. check dependencies before editing
4. keep changes inside the declared write scope unless the ticket is updated first

## 6. Validation Map

Default validation expectations by area:

- build-logic or plugin wiring changes: `./gradlew help` first, then `./gradlew qualityCheck`, then `./gradlew test :androidApp:assembleDebug :androidApp:assembleRelease :androidApp:bundleRelease :shared:compileKotlinIosSimulatorArm64`
- root/module/build changes: `./gradlew qualityCheck`, then `./gradlew test :androidApp:assembleDebug :androidApp:assembleRelease :androidApp:bundleRelease :shared:compileKotlinIosSimulatorArm64`
- store API or runtime changes: `./gradlew qualityCheck`, then `./gradlew test :androidApp:assembleDebug :shared:compileKotlinIosSimulatorArm64`
- shared Kotlin changes: `./gradlew qualityCheck`, then add `:shared:compileKotlinIosSimulatorArm64`
- server changes: keep `./gradlew test`
- planning/doc-only changes: no build required, but keep links and status coherent

If you change build logic, module wiring, or shared Kotlin, prefer validating Android plus iOS Kotlin compilation, not just JVM.

## 7. Fast Start Paths

If you need to move fast, use one of these entry paths:

### I need architecture context

Read:

1. `docs/planning/starter-architecture.md`
2. `docs/planning/foundation/README.md`

### I need to know what to work on next

Read:

1. `docs/planning/backlog.md`
2. `docs/planning/README.md`

### I need persistence context

Read:

1. `docs/planning/workstreams/persistence.md`
2. `docs/planning/starter-architecture.md`

### I need DI context

Read:

1. this file
2. `shared/src/commonMain/kotlin/com/focus/kolo/AppGraph.kt`
3. the platform graph in the source set you are changing

### I need Gradle or build-logic context

Read:

1. `gradle/libs.versions.toml`
2. `settings.gradle.kts`
3. `build-logic/convention/build.gradle.kts`
4. the specific convention plugin for the module you are changing

## 8. Keep This File Fresh

Update `AGENTS.md` when any of these change:

- top-level module structure
- current primary entry points
- DI approach
- default validation commands
- backlog sequence in a way that changes where agents should start

Do not update `AGENTS.md` for:

- ordinary feature details
- deep persistence rules
- one-off implementation notes
- temporary debugging notes

Those belong in `docs/planning/*` or in the ticket/workstream docs.

## 9. Current Sharp Edges

Agents should know these before editing:

- the store contract has already been extracted into `shared:core:store:api` and `shared:core:store:impl`
- preserve the API vs impl split when extending store code
- the runtime-config contract is now split between `shared:core:config:api` and `shared:core:config:impl`; keep pure models and read contracts out of `shared`
- `shared` is still broad outside the extracted store modules and will likely be split further later
- `build-logic` is now the source of truth for shared plugin wiring; prefer changing conventions there before copying build setup into modules
- the debug menu is isolated in the shared `debugmenu` module, with Android debug-entry wiring in `androidApp` and SwiftUI host glue in `iosApp`
- shared SDK and toolchain values, including `java-toolchain`, now live in `gradle/libs.versions.toml`; keep toolchain-related build settings aligned with the catalog
- the current convention plugins are intentionally thin; the shared base KMP Android library convention owns the common plugin stack, toolchain, and default Android SDK values, while modules still keep local Android-KMP target settings such as namespace and source-set options
- the root `build.gradle.kts` plugin block should stay small, but the external `apply false` aliases still need to stay there; removing them breaks convention-plugin loading for AGP-backed plugins
- the root quality entry points are `qualityCheck`, `qualityFix`, and `dependencyHealth`; prefer those over memorizing individual lint or formatting tasks
- `composeApp` still has a non-blocking unused `commonTest` source-set warning under the Android-KMP layout
- Metro is integrated, but only as a small first cut; do not assume a full app graph already exists
- the current UI is still template-level and should not be mistaken for settled architecture
