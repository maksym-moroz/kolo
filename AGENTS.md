# AGENTS.md

This file is the shortest durable map of how to work effectively in this repo.

It is intentionally map-based:

- point agents to the real source of truth
- keep only the rules and facts that are easy to forget
- avoid duplicating large specs that will drift

If this file starts reading like a full design document, it is too big.

## 1. Source Of Truth Map

Start here:

1. `docs/planning/README.md`
2. `docs/planning/backlog.md`
3. `docs/planning/starter-architecture.md`
4. the relevant workstream or foundation doc for the ticket

High-value references:

- `docs/planning/foundation/agp9-migration.md`
- `docs/planning/foundation/code-style.md`
- `docs/planning/foundation/quality-tooling-map.md`
- `docs/planning/foundation/udf-store-contract.md`
- `docs/planning/foundation/version-baseline.md`
- `docs/planning/workstreams/persistence.md`
- `docs/planning/multi-agent-workflow.md`

Interpretation:

- `AGENTS.md` tells you where to look
- `docs/planning/*` tells you what the project believes and plans next
- code and build files tell you what is actually implemented
- for forward-facing docs meant to keep the repo owner in the loop, prefer HTML as the primary human-facing source-of-truth format
- if an HTML doc conflicts with code or build files, treat code and build files as the implementation truth and update the HTML doc

## 2. Repo Map

Top-level structure:

- `apps/`: app shells built on top of the shared Kolo platform
- `apps/reference/androidApp/`: Android reference app entry point
- `apps/reference/iosApp/`: iOS reference app and SwiftUI host shell
- `baselineprofile/`: Android Baseline Profile generator module
- `build-logic/`: included Gradle build for convention plugins
- `composeApp/`: KMP UI library, currently the Compose-facing UI surface
- `debugmenu/`: shared KMP debug-menu feature module
- `docs/planning/`: planning, backlog, architecture, and workstreams
- `openspec/`: change proposals, task tracking, and archive/spec artifacts
- `server/`: Ktor server module
- `shared/`: shared KMP library for app graph and remaining runtime-composition code
- `shared/core/appshell/`: extracted app-shell identity and capability contract module
- `shared/core/config/`: extracted runtime-config API and implementation modules
- `shared/core/store/`: extracted store API and runtime modules

Current reality:

- the AGP 9 migration first cut is done
- `build-logic` is the source of truth for shared Gradle plugin wiring
- `:androidApp` is still the real Android app boundary, but its host now lives under `apps/reference/androidApp`
- `baselineprofile` generates Baseline Profiles for the Android app and is not a shipping app module
- the app-shell identity and capability contract lives in `shared:core:appshell`
- shared runtime config lives in `shared:core:config:api` and `shared:core:config:impl`
- the debug menu now lives in the shared `debugmenu` KMP module, with thin Android and iOS hosts at the app edge
- `composeApp` is not the Android application module anymore
- the runtime-config read contract remains in shared Kotlin, while debug-only mutation wiring is exposed through platform graphs at the app edge
- the store contract is extracted into `shared:core:store:api` and `shared:core:store:impl`
- `shared` is now primarily the runtime-composition and app-graph layer outside the extracted core modules, but it is still broader than the eventual target split
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

- build: `apps/reference/androidApp/build.gradle.kts`
- entry point: `apps/reference/androidApp/src/main/kotlin/com/focus/kolo/MainActivity.kt`
- manifest: `apps/reference/androidApp/src/main/AndroidManifest.xml`

### Android baseline profiles

- build: `baselineprofile/build.gradle.kts`
- generator: `baselineprofile/src/main/kotlin/com/focus/kolo/baselineprofile/BaselineProfileGenerator.kt`

### App shell, runtime config, and debug menu

- app-shell contract build: `shared/core/appshell/build.gradle.kts`
- app-shell contract sources: `shared/core/appshell/src/commonMain/kotlin/com/focus/kolo/appshell/`
- config API build: `shared/core/config/api/build.gradle.kts`
- config API effective models and read contract: `shared/core/config/api/src/commonMain/kotlin/com/focus/kolo/config/`
- config impl build: `shared/core/config/impl/build.gradle.kts`
- config impl overrides: `shared/core/config/impl/src/commonMain/kotlin/com/focus/kolo/config/impl/override/`
- config impl repo/defaults: `shared/core/config/impl/src/commonMain/kotlin/com/focus/kolo/config/impl/repo/`
- config impl local and remote sources: `shared/core/config/impl/src/commonMain/kotlin/com/focus/kolo/config/impl/source/`
- platform DataStore path/source wiring: `shared/core/config/impl/src/androidMain/kotlin/com/focus/kolo/config/impl/source/local/` and `shared/core/config/impl/src/iosMain/kotlin/com/focus/kolo/config/impl/source/local/`
- config impl use cases: `shared/core/config/impl/src/commonMain/kotlin/com/focus/kolo/config/impl/usecase/`
- shared debug-menu module build: `debugmenu/build.gradle.kts`
- Android debug-menu host activity entry: `apps/reference/androidApp/src/debug/kotlin/com/focus/kolo/debugmenu/`
- shared debug-menu feature sources: `debugmenu/src/commonMain/kotlin/com/focus/kolo/debugmenu/`
- iOS debug-menu controller factory: `debugmenu/src/iosMain/kotlin/com/focus/kolo/debugmenu/`
- debug-menu deep-link manifest wiring: `apps/reference/androidApp/src/debug/AndroidManifest.xml`
- iOS debug-menu host wiring and URL handling: `apps/reference/iosApp/iosApp/`

### Shared UI library

- build: `composeApp/build.gradle.kts`
- current shared app root: `composeApp/src/commonMain/kotlin/com/focus/kolo/KoloApp.kt`
- Android preview/host-side wrapper: `composeApp/src/androidMain/kotlin/com/focus/kolo/App.kt`

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

- app shell: `apps/reference/iosApp/iosApp/iOSApp.swift`
- current screen: `apps/reference/iosApp/iosApp/ContentView.swift`

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

These are the repo defaults unless a ticket explicitly changes them.

### Build and structure

- follow `docs/planning/foundation/code-style.md` for repository-specific style rules that go beyond formatter defaults
- prefer HTML for durable forward-facing project overviews and architecture/source-of-truth documents so the repo owner can read them quickly even when most work is done by agents
- keep platform entry points thin and reusable logic in shared Kotlin
- prefer boring module boundaries over clever indirection
- keep shared Gradle setup in `build-logic`; keep module build scripts focused on namespace, target-specific compiler settings, and direct dependencies
- keep Baseline Profile generation isolated in `baselineprofile` instead of mixing generator code into `androidApp`
- keep app-shell identity and capability contracts in `shared:core:appshell` instead of `shared`
- keep shared runtime-config contracts in `shared:core:config:api`, runtime wiring in `shared:core:config:impl`, and keep platform entry and hosting details out at the Android/iOS app edge
- do not reintroduce the old KMP + Android app pairing in `composeApp`
- prefer one top-level type per file, especially for actions, effects, state, reducers, and middleware

### Dependency injection

- Metro is the DI baseline
- keep the base graph contract in `commonMain`
- keep final `@DependencyGraph` types in platform source sets when platform bindings differ
- add platform-only bindings at the platform graph edge

### Architecture

- use unidirectional data flow
- keep ViewModel thin; keep persistent state in shared feature/store layers, not platform entry points
- model one-off events as effects, not persistent state
- repositories and data aggregators must not create their own lifetime `CoroutineScope`; scope ownership and any `stateIn`/sharing policy belong at the graph or feature-runtime edge
- config repositories and override sources should expose plain `Flow` contracts unless a caller has a real need for hot shared state at its own runtime edge
- if the intended architecture or requested pattern is unclear, stop and ask a concise clarifying question instead of guessing and implementing an inferred design
- when an interface exposes a `StateFlow` or `SharedFlow`, implementations should override it directly with the mutable flow they own; do not introduce mirrored `mutableX` plus `x = mutableX.asStateFlow()` or `asSharedFlow()` fields
- use `shared:core:store:api` rather than inventing feature-local store primitives; prefer the shared generic store contract unless it is no longer sufficient for the slice
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
- the coordinating agent should decide whether a task should stay single-agent or be split across subagents
- use subagents when the runtime and user instructions allow delegation and the write scopes are clearly disjoint
- if delegation is unavailable or not allowed, keep the same ownership and write-scope discipline in a single-agent pass
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

If you need to move fast, use one of these paths:

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

- preserve the API vs impl splits for `shared:core:store:*` and `shared:core:config:*`
- `shared:core:appshell` is the narrow app-shell contract; `composeApp` should depend on it instead of `shared`
- `shared` is now mainly the runtime-composition and app-graph layer, but it is still broader than the eventual target split
- shared SDK and toolchain values, including `java-toolchain`, live in `gradle/libs.versions.toml`; keep build settings aligned with the catalog
- the convention plugins are intentionally thin; the root `build.gradle.kts` plugin block should also stay small, but the external `apply false` aliases must remain or AGP-backed convention loading breaks
- prefer root entry points like `qualityCheck`, `qualityFix`, and `dependencyHealth` over memorizing leaf tasks
- `composeApp` still has a non-blocking unused `commonTest` source-set warning under the Android-KMP layout
- Metro is only a first cut; do not assume a full app graph already exists
- the current UI is still template-level and should not be mistaken for settled architecture
