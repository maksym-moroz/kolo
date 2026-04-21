# Starter Architecture Baseline

This document captures the current recommended baseline for the Kolo starter app as of March 21, 2026.

## Product Shape

Target app:

- tasks and subtasks
- reminders and repeat rules
- journaling
- settings
- offline-first behavior from the first useful build

The app should be locally useful before auth, sync, or cloud backup exist.

## Current Repo Reality

The repo is no longer just the default Kotlin Multiplatform wizard shape.

Current implemented structure:

- `apps/reference/androidApp` is the current Android host location for the `:androidApp` application module
- `baselineprofile` is the Android Baseline Profile generator module
- `build-logic` is the included build for shared convention plugins
- `composeApp` is a KMP UI library, not the Android app module
- `debugmenu` is the shared KMP debug-menu feature module
- `apps/reference/iosApp` is the current iOS host shell
- `shared` is now primarily the runtime-composition and app-graph KMP layer
- `shared:core:appshell` now holds the app-shell identity and capability contract
- `shared:core:config:api` and `shared:core:config:impl` now hold the runtime-config contract and implementation
- the debug menu now has platform-hosted internal tooling surfaces on Android and iOS and remains isolated from release-facing product UI
- `shared:core:store:api` and `shared:core:store:impl` are already extracted
- `server` remains the Ktor server starter

That means the first AGP 9 migration cut has landed. The remaining structural work is mostly deeper shared-module decomposition and feature extraction rather than re-establishing the app boundary.

## Primary Recommendation

Use shared Kotlin for the full domain, data, store, and platform-abstraction layers.

For UI, the MVP recommendation is full Compose Multiplatform unless there is already a strong product reason to keep SwiftUI on iOS. If iOS-native polish becomes a strategic concern later, the architecture should still support swapping the iOS presentation layer while keeping the shared domain and data stack intact.

## Architectural Principles

1. Shared code owns business rules, persistence, networking contracts, and unidirectional data flow.
2. ViewModel is a thin adapter, not the center of business logic.
3. Each feature renders from one immutable `UiState`.
4. One-off outputs use `UiEffect`, not persistent state.
5. Navigation passes IDs and route arguments, not full models.
6. Reminder logic is timezone-safe by design.
7. Room is the app database only, not the server persistence model.
8. Preferences are not stored in the main database unless they are relational.
9. Platform integrations are isolated behind `expect`/`actual` or thin platform wrappers.
10. Release readiness includes migrations, deep links, notification taps, back behavior, and accessibility.
11. Android release readiness includes current Baseline Profiles for startup and critical user journeys.

## UI Direction

### MVP Recommendation

Choose full Compose Multiplatform for the starter.

Why:

- one shared UI surface for faster bootstrap
- simpler shared previews, testing, and navigation model
- less duplicated presentation code while the product is still fluid

### Long-Term Alternate

Choose Compose on Android and SwiftUI on iOS if:

- iOS-native feel is a product differentiator
- the team already has strong SwiftUI capacity
- platform-specific UI divergence is expected

## Build And Module Direction

The AGP 9 migration first cut is already in place. The remaining module work is to continue decomposing `shared` into narrower core and feature modules without regressing the current platform boundaries or the new `platform + reference app shell` split.

Target module direction:

- `apps/reference/androidApp`: current Android reference app host
- `composeApp`: shared Compose UI surface
- `debugmenu`: shared debug-menu feature module with thin Android and iOS hosts
- `apps/reference/iosApp`: current iOS reference app host
- `apps/<app-id>`: future app shell location once the platform baseline is reused beyond the reference app
- `shared:core:model`: IDs, primitives, and serializable contracts
- `shared:core:appshell`: app identity, capability list, and shell definition contract
- `shared:core:config:api`: runtime-config models and read-side contracts
- `shared:core:config:impl`: runtime-config merge logic, persistence, and mutation use cases
- `shared:core:common`: clocks, dispatchers, logging, result types, utilities
- `shared:core:network`: Ktor client setup, DTOs, resources, error mapping
- `shared:core:database`: Room KMP database, DAOs, mappers, schema history
- `shared:core:preferences`: DataStore KMP settings storage
- `shared:core:domain`: repositories and use cases
- `shared:core:store`: Redux/UDF primitives
- `shared:feature:tasks`
- `shared:feature:journal`
- `shared:feature:reminders`
- `shared:feature:settings`
- `server`: Ktor server starter

This is a direction, not a locked final tree. The important part is the separation between reusable KMP platform libraries and thin app shells.

## Stack Baseline

Use boring versions with official support where possible:

- Java toolchain `17`
- Kotlin `2.3.20`
- AGP `9.1.x`
- Compose Multiplatform `1.10.x` patch compatible with chosen Kotlin
- Lifecycle/ViewModel `2.10.0`
- Room `2.8.4`
- bundled SQLite `2.6.2`
- DataStore `1.2.1`
- Ktor `3.4.1`
- navigation-compose `2.9.2`
- Metro `0.11.x`

## UDF / Redux Direction

Keep these in shared Kotlin:

- `Intent`
- `Action`
- `Reducer`
- `Middleware`
- `Store`
- `UiState`
- `UiEffect`

Recommended flow:

1. UI emits `Intent`
2. intent mapper or feature entry point converts to `Action`
3. `Store` dispatches to middleware chain
4. middleware performs side effects and emits follow-up actions
5. reducer produces new immutable `UiState`
6. `UiEffect` is emitted for navigation, snackbars, permission prompts, and notification taps

Keep ViewModel as:

- lifecycle owner bridge
- state collector
- intent forwarder
- restoration edge

Do not let ViewModel become the side effect engine.

Internal tooling note:

- internal Android tooling surfaces such as the debug menu may observe a shared read model and dispatch explicit commands without inventing a separate feature-local persisted state machine
- internal iOS tooling surfaces should follow the same observer-plus-command shape, with URL entry and modal presentation owned by the SwiftUI host

Implemented first cut:

- shared markers for `UiIntent`, `UiAction`, `UiState`, and `UiEffect`
- generic `IntentMapper`, `Reducer`, `Middleware`, `StoreScope`, and `Store`
- `DefaultStore` with serialized action processing via a channel
- `StoreFactory` exposed through the shared Metro app graph

Current location:

- API: `shared/core/store/api/src/commonMain/kotlin/com/focus/kolo/store/`
- implementation: `shared/core/store/impl/src/commonMain/kotlin/com/focus/kolo/store/impl/`

Future multi-module destination:

- `shared:core:store:*` is now the actual shape
- `shared:core:config:*` is now the actual shape for runtime config

## DI Direction

Use Metro as the DI baseline.

Current integration shape:

- shared base graph contract in `commonMain`
- platform-specific final `@DependencyGraph` types in `androidMain` and `iosMain`
- platform-only bindings supplied at the platform graph edge

This follows Metro's multiplatform guidance and keeps common code free from platform-specific binding details.

## Persistence Direction

Use:

- Room KMP for tasks, journals, reminder definitions, and sync metadata
- DataStore KMP for theme, week start, defaults, and other small preferences
- DataStore KMP for persisted local runtime-config overrides used by the shared debug menu

Persistence rules:

- export Room schemas from day one
- add migration tests before any release-bound schema change
- use repository boundaries instead of exposing DAOs to features
- persist reminder intent, not just a computed timestamp

## Reminder Modeling

Model reminders with explicit time semantics:

- `LocalDate`
- `LocalTime`
- `TimeZone`
- repeat rule
- scheduling status
- derived next scheduled instant if useful

Do not treat `LocalDateTime` alone as enough. A reminder app lives or dies on DST and timezone behavior.

## Notifications And Scheduling

Use platform scheduling deliberately:

- Android `AlarmManager` for user-visible fire times
- Android `WorkManager` for sync, retries, cleanup, and deferred work
- iOS `UNUserNotificationCenter` for local notification delivery
- iOS `BGTaskScheduler` for refresh and maintenance, not exact reminder firing

## Navigation And Back Behavior

Key rules:

- validate all deep links as untrusted input
- keep a single deep-link parsing strategy in shared code
- define modal behavior early instead of letting screens improvise
- treat app-global stacks, modal presentation, and feature-local flows as separate navigation layers
- make back resolution first-class instead of equating back with a naive stack pop
- support predictive back on Android
- verify iOS swipe-back before introducing custom transitions
- use multiple back stacks only when tabs or root sections justify them

Detailed working agreement:

- [workstreams/navigation.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/navigation.md)

## Force Update Strategy

Use a backend-driven version policy:

- `latestVersion`
- `minimumSupportedVersion`

Platform behavior:

- Android: combine backend policy with Play in-app updates
- iOS: use backend policy plus App Store redirect for mandatory updates

## Testing Expectations

The starter should be testable by default.

Minimum test layers:

- reducer tests
- middleware tests
- repository tests
- migration tests
- reminder time model tests across timezone and DST changes
- Ktor client tests with mock engine
- Ktor server tests
- Compose UI tests if UI is shared

## Risks To Keep Visible

- AGP 9 migration can become expensive if delayed
- Metro native aggregation constraints need conservative usage
- Compose iOS behavior should be treated as a real product surface, not a free port
- exact-alarm and notification policy is platform-constrained
- persistence and reminder-time bugs become release blockers quickly
