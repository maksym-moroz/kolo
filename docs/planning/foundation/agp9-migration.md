# FND-001: AGP 9 Migration Target Structure

## Goal

Document and continue the move from the original template-shaped KMP layout to an AGP 9-friendly module structure that keeps Android app code, shared KMP libraries, iOS host code, and server code cleanly separated.

## Status Snapshot

The first migration cut is already implemented:

- `androidApp` is now the real Android app module
- `composeApp` is now a library module instead of the Android app boundary
- `build-logic` is the source of truth for shared plugin wiring
- `shared:core:store:api` and `shared:core:store:impl` are already extracted

The remaining work is deeper decomposition of `shared` plus feature extraction, not recreating the Android app boundary.

## Exact Target Module Tree

```text
root
├── androidApp
│   ├── Android application entry point
│   ├── Android-only UI shell
│   ├── platform integrations
│   └── dependency on shared feature modules
├── iosApp
│   ├── iOS host app
│   ├── SwiftUI shell or Compose host wrapper
│   └── framework consumption of shared modules
├── server
│   ├── Ktor entry point
│   ├── typed routes
│   └── server persistence and API contracts
├── shared
│   ├── core
│   │   ├── model
│   │   ├── common
│   │   ├── store
│   │   ├── domain
│   │   ├── network
│   │   ├── database
│   │   └── preferences
│   └── feature
│       ├── tasks
│       ├── journal
│       ├── reminders
│       └── settings
└── docs
    └── planning
```

## Migration Mapping

Original `composeApp` role:

- supplied the original Android entry points before `androidApp` existed
- now keeps shared Compose-facing UI concerns instead of acting as the Android app boundary
- should not regain shared domain, data, or store responsibilities that belong elsewhere

Current `shared`:

- becomes the source for reusable KMP code
- is split into `shared/core/*` and `shared/feature/*`
- should not remain a catch-all module after migration

Current `iosApp`:

- stays as the iOS host app
- consumes shared frameworks from `shared`
- may host SwiftUI or a Compose wrapper depending on the UI decision

Current `server`:

- remains the server module
- is isolated from mobile app persistence concerns
- owns API contracts, validation, and server-specific storage

## Migration Phases

### Phase 1: Structural split

- Status: first cut completed.
- `androidApp` exists and is the app boundary.
- `composeApp` is no longer treated as the final Android app boundary.
- `shared` is prepared for further conversion into narrower library-focused submodules.

### Phase 2: Shared foundation

- Move IDs, DTOs, clocks, dispatchers, and store primitives into `shared/core/*`.
- Make shared code the source of truth for business logic and data contracts.
- Keep UI entry points thin.

### Phase 3: Data and persistence

- Add `shared/core/database` and `shared/core/preferences`.
- Introduce repository boundaries in `shared/core/domain`.
- Keep database and preference ownership out of feature modules.

### Phase 4: Feature extraction

- Move tasks, journaling, reminders, and settings into `shared/feature/*`.
- Keep each feature behind its own state/action/store surface.

### Phase 5: Cleanup and lock-in

- Remove duplicated logic from `composeApp`.
- Trim dead template code.
- Recheck the module graph for cyclic dependencies.

## Risks And Cut Lines

### Risks

- AGP 9 compatibility issues if the old `com.android.application` plus KMP pairing is left in place too long.
- Module explosion if the split happens before the domain boundaries are clear.
- Shared code becoming a second catch-all if feature boundaries are not enforced.
- iOS UI work being blocked if the shared layer tries to solve every platform-specific detail up front.

### Cut Lines

- Do not build persistence features before the module split is in place.
- Do not add sync, auth, or notification delivery to the migration ticket.
- Do not collapse Android and shared concerns into one module just to shorten the migration.
- Do not finalize iOS UI implementation in the module-split ticket; preserve the host boundary only.

## Historical First Implementation Step

Create the `androidApp` module and make `composeApp` clearly non-final in the planning docs, then move the existing Android entry point and build configuration to the new app boundary.

## Implemented First Cut

The current implemented cut keeps the structure intentionally small:

- `androidApp` is now the real Android application module
- `composeApp` is now a KMP library module instead of the Android application boundary
- `shared` remains a KMP library module and uses the Android-KMP library plugin
- `server` remains isolated

Intentional follow-up:

- split `shared` into narrower `shared/core/*` and `shared/feature/*` modules later
- keep `composeApp` as the shared Compose UI module now that it already builds Android and iOS surfaces, and continue moving app-specific host concerns out to `apps/reference/*`
