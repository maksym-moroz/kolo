This is a Kotlin Multiplatform repo for the Kolo platform baseline and reference app.

Current targets:

- Android reference app host in `apps/reference/androidApp`
- Android baseline profile generator in `baselineprofile`
- shared debug menu feature in `debugmenu`, hosted only from debug-only Android and internal iOS entry points
- iOS reference app host in `apps/reference/iosApp`
- shared Kotlin and app graph code in `shared`
- shared app-shell identity and capability contract in `shared:core:appshell`
- shared runtime-config contract and implementation in `shared:core:config:api` and `shared:core:config:impl`
- shared store contract and runtime in `shared:core:store:api` and `shared:core:store:impl`
- shared Compose-facing UI library in `composeApp`
- Ktor server in `server`
- shared Gradle conventions in `build-logic`

## Start Here

If you need repo context before editing, read these in order:

1. `docs/planning/README.md`
2. `docs/planning/backlog.md`
3. `docs/planning/starter-architecture.md`
4. the relevant workstream or foundation doc for the task

## Bootstrap From Clone

Prerequisites:

- JDK 17
- Android SDK with API 36 installed
- Xcode for the iOS host app
- Node.js and npm for repository-local commit tooling

From a fresh clone:

```shell
git clone <your-remote-url>
cd kolo
npm install
git config core.hooksPath .githooks
```

## Common Commands

Run the repository quality gate:

```shell
./gradlew qualityCheck
```

Apply repository formatting:

```shell
./gradlew qualityFix
```

Run the broader default validation used for build, shared Kotlin, and store changes:

```shell
./gradlew test :androidApp:assembleDebug :androidApp:assembleRelease :androidApp:bundleRelease :shared:compileKotlinIosSimulatorArm64
```

Build Android:

```shell
./gradlew :androidApp:assembleDebug
```

Build Android release:

```shell
./gradlew :androidApp:assembleRelease
```

Build the Play-facing Android release bundle:

```shell
./gradlew :androidApp:bundleRelease
```

Run the server:

```shell
./gradlew :server:run
```

Open the iOS host app in Xcode:

```shell
open apps/reference/iosApp/iosApp.xcodeproj
```

Generate dependency analysis reports:

```shell
./gradlew dependencyHealth
```

Generate Android baseline profiles with the configured Gradle-managed device:

```shell
./gradlew :androidApp:generateBaselineProfile
```

Open the Android debug menu on a connected debug build:

```shell
adb shell am start -W -a android.intent.action.VIEW -d 'kolo://debug/menu' com.focus.kolo
```

## Repo Map

- `apps/`: app shells built on top of the shared Kolo platform
- `apps/reference/androidApp/`: Android reference app boundary and entry point
- `apps/reference/iosApp/`: iOS reference app and SwiftUI shell
- `baselineprofile/`: Android Baseline Profile generator module targeting `androidApp`
- `build-logic/`: included Gradle build for shared convention plugins
- `composeApp/`: Compose Multiplatform UI library consumed by the Android and iOS hosts
- `debugmenu/`: shared debug-menu feature module consumed by Android and iOS hosts
- `docs/planning/`: backlog, architecture, workstreams, and foundation docs
- `openspec/`: change proposals, tasks, and archive artifacts
- `server/`: Ktor server module
- `shared/`: shared Kotlin, Metro app graph, and remaining runtime-composition code
- `shared/core/appshell/`: app-shell identity and capability contract
- `shared/core/config/api/`: runtime-config models and read contract
- `shared/core/config/impl/`: runtime-config implementation, persistence, and use cases
- `shared/core/store/api/`: public store contract
- `shared/core/store/impl/`: store runtime implementation

## Build Notes

- `build-logic` is the source of truth for shared Gradle plugin wiring
- root repository orchestration now lives in the `kolo.root` convention plugin and the root `kolo {}` block only declares which modules participate in aggregate checks
- the base Android-KMP library convention owns the common plugin stack, toolchain, and default Android SDK values for library modules
- the durable implementation map for CI and linting lives in `docs/planning/foundation/quality-tooling-map.md`
- `apps/reference/androidApp` is the current Android host location for the `:androidApp` module
- `baselineprofile` owns Baseline Profile generation for Android startup and critical user journeys
- app-shell identity and capability contracts now live under `shared/core/appshell/`
- the current shared app root lives in `composeApp/src/commonMain/kotlin/com/focus/kolo/KoloApp.kt`
- shared runtime config now lives under `shared/core/config/api/` and `shared/core/config/impl/`
- the shared `debugmenu` module is wired at the app edge: Android attaches it with `debugImplementation`, and the iOS host presents it through an internal URI path
- `composeApp` is not the Android application module
- `shared` is now mainly the runtime-composition and app-graph layer outside the extracted core modules and will likely still be split further later
- the root build exposes `qualityCheck`, `qualityFix`, and `dependencyHealth`
- shared SDK and toolchain versions live in `gradle/libs.versions.toml`

## Commit Convention

This repo uses Conventional Commits.

Examples:

- `feat(shared): add reminder scheduling contract`
- `fix(androidApp): correct back press handling`
- `docs(planning): clarify persistence workstream`
- `chore(build-logic): align plugin wiring`

Breaking changes must use `!` or a `BREAKING CHANGE:` footer, for example:

- `feat(shared)!: rename store action API`

To enable the local commit message hook once per clone:

```shell
npm install
git config core.hooksPath .githooks
```

GitHub Actions also lint commit messages on pull requests and pushes to `main`.

## Versioning

The repo uses Semantic Versioning for the shared release version.

- Gradle source of truth: `kolo.version.name` and `kolo.version.code` in `gradle.properties`
- Android reads both values from Gradle properties
- Server reads `kolo.version.name` from Gradle properties
- iOS `MARKETING_VERSION` is currently mirrored in `apps/reference/iosApp/Configuration/Config.xcconfig`

Use these bump rules:

- patch: backwards-compatible bug fix
- minor: backwards-compatible feature
- major: breaking change

Current baseline:

- release version: `0.1.0`
- build number: `1`
