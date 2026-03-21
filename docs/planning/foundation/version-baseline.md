# FND-002: Version Baseline

## Goal

Pin a boring, supportable dependency baseline for the starter app so the AGP 9 module split, shared KMP architecture, and later persistence work all build on the same assumptions.

## Recommended Baseline

- Kotlin: `2.3.20`
- AGP: `9.1.0`
- Gradle: `9.3.1`
- Compose Multiplatform: `1.10.3`
- Lifecycle / ViewModel: `2.10.0`
- Room: `2.8.4`
- SQLite bundled driver: `2.6.2`
- DataStore: `1.2.1`
- Ktor: `3.4.1`
- Navigation Compose: `2.9.2`
- Metro: release compatible with Kotlin `2.3.20`

## Locked Versus Flexible

### Locked

- Kotlin should stay on `2.3.20` for the initial starter baseline unless the build toolchain forces a move.
- AGP should stay on `9.1.0` because the repo already needs the new KMP-friendly module shape.
- Room, DataStore, Ktor, and Lifecycle should be pinned to the specified release lines so the persistence and shared architecture work against fixed APIs.

### Flexible

- Compose Multiplatform can move within the `1.10.x` patch line if the selected Kotlin version requires a compatible patch release.
- Metro can move to the release that is compatible with the chosen Kotlin version and target set.
- Navigation Compose can move within the minor line if a later patch is needed for a specific navigation or back-stack fix.

## Compatibility Notes

- AGP 9 is the forcing function for the module split, so module structure work should happen before feature work.
- Compose Multiplatform and Kotlin must be kept on compatible releases; do not upgrade them independently.
- Metro should be treated conservatively across native targets and multi-module boundaries until the module graph stabilizes.
- Room KMP should be introduced with schema export and migration testing from the start to avoid version churn later.
- DataStore should remain the settings store only; do not stretch it into a general database substitute.
- Ktor client and server should be kept on the same major/minor line to reduce contract drift.

## Version Policy

1. Lock the baseline in version catalog once.
2. Allow patch-only movement for Compose MP, Metro, and Navigation if needed for compatibility.
3. Treat Kotlin, AGP, Room, DataStore, Ktor, and Lifecycle as controlled upgrades that require explicit planning.

## Why This Baseline

This set is stable enough to support the new module structure, already aligns with the current KMP direction of the repo, and avoids forcing the starter onto newer bleeding-edge releases before the architecture is settled.

## Implemented Concrete Versions

The repo currently uses these concrete versions:

- Kotlin `2.3.20`
- AGP `9.1.0`
- Gradle `9.3.1`
- Compose Multiplatform `1.10.3`
- Lifecycle `2.10.0`
- Ktor `3.4.1`
- Metro `0.10.4`
