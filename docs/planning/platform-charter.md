# Platform Charter

`kolo` exists to launch new mobile apps quickly with consistent quality.

This repo is a platform baseline, not a single app codebase with reusable leftovers.

## Mission

- make new app launches cheap and repeatable
- keep quality, testability, and observability part of the baseline
- let multiple agents work in parallel with small, explicit write scopes

## What Kolo Owns

- build logic and repo-wide quality rails
- shared DI, store, persistence, navigation, and runtime-config contracts
- reusable capability modules
- observability and test infrastructure
- shell conventions and app-definition contracts

## What App Shells Own

- app id and bundle id
- branding, naming, icons, and market metadata
- enabled capabilities
- app-specific integrations and monetization
- market- or client-specific composition

## What Must Stay Out Of Kolo

- app-specific copy with no reuse value
- partner-specific or one-off integrations in shared modules
- shell-specific hacks hidden as baseline abstractions
- broad feature logic added to `shared` without a named destination

## Extension Rule

If a behavior varies across apps, prefer one of these:

1. app-definition config
2. capability composition
3. adapter interface at the app shell edge

Do not scatter app conditionals through shared platform code.

## Quality Contract

Every app launched from this baseline should inherit:

- deterministic module boundaries
- testable storage and domain contracts
- observability hooks
- smoke validation paths
- predictable CI and release checks

## Graduation Rule

If an app diverges enough that it keeps bending shared contracts toward app-specific behavior, it should stop stretching the baseline and graduate to its own repo or a much thinner shell relationship.
