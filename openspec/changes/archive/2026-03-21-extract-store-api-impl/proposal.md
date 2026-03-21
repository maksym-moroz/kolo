## Why

The shared store contract now exists in code, but it still lives inside the broad `shared` module. Extracting it into `shared:core:store:api` and `shared:core:store:impl` is the next structural step because feature modules should depend only on store abstractions while the concrete implementation stays replaceable and isolated.

## What Changes

- Create a dedicated `shared:core:store:api` module for the public store contract.
- Create a dedicated `shared:core:store:impl` module for the default store implementation.
- Move the current store API types out of the broad `shared` module into the new API module.
- Move the current default store runtime into the new implementation module.
- Rewire the app graph and current consumers directly to the new modules instead of routing through the broad `shared` module.
- Update planning and agent-facing documentation so future work builds on the extracted module boundaries.

## Capabilities

### New Capabilities

- `shared-store-modules`: Separate store API and implementation modules for shared feature-state infrastructure.

### Modified Capabilities

- None.

## Impact

- Affects Gradle module structure and settings inclusion.
- Affects shared DI wiring and Metro bindings.
- Affects current shared store code location and package ownership.
- Affects future feature-module dependency rules and extraction path.
