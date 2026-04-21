# App Shell Contract

An app shell is a thin host around the shared Kolo platform.

## Required Surface

Each app shell should provide:

- app definition metadata
- Android host wiring
- iOS host wiring
- branding assets
- release identity
- enabled capability list
- app-specific integration bindings

## App Definition Fields

At minimum, each app definition should name:

- `id`
- `displayName`
- `packageName`
- `bundleId`
- enabled capabilities
- runtime-config profile
- observability configuration
- release environment list

## Ownership Boundary

Shells may own:

- branding and copy
- capability inclusion or exclusion
- app-specific adapters
- market-specific integration choices

Shells should not own:

- shared store primitives
- shared navigation contracts
- shared persistence abstractions
- duplicated platform infrastructure

## Composition Rule

Capabilities should be opt-in from the shell.

The shell decides what to mount.
The platform decides how shared contracts work.

## Reference Shell

`apps/reference/` is the current truth source for host wiring until additional app shells exist.
