## Why

The app needs one coherent place for runtime configuration before feature work starts to multiply flags, version policy, URLs, and environment-specific behavior across unrelated files. This is the right time to establish that boundary because the app is still small, the debug menu can become the first real consumer, and later remote config can plug into the same shape without redesigning feature code.

## What Changes

- Add a shared runtime-config domain that produces one effective typed app config from defaults, mocked remote config, and persisted local overrides.
- Add persisted on-device local overrides that can override every config field in scope for this change.
- Add typed feature flags, version policy, URLs/constants, and predefined environment selection (`dev`, `prod`) to the runtime-config model.
- Add a remote-config boundary now, with an initial mock implementation that returns no remote overrides.
- Add explicit use cases for updating overrides, clearing overrides, and resetting all overrides, with environment selection flowing through the same override-mutation path.
- Add platform-hosted debug menu surfaces that observe effective runtime config, dispatch override commands, and are reachable through internal URI entry paths.
- Persist local overrides in DataStore.
- Apply non-environment config edits immediately, keep URL/config values read-only in the first debug-menu UI, and trigger a clean app restart via Process Phoenix when the selected environment changes while preserving all other local overrides.

## Capabilities

### New Capabilities
- `runtime-config`: Shared runtime configuration with typed effective config, mocked remote overrides, persisted local overrides, and environment/version/flag resolution.
- `debug-menu`: platform-hosted internal tooling surfaces for viewing and editing effective runtime config and local overrides.

### Modified Capabilities
- None.

## Impact

- Affected code:
  - `shared/**` for runtime-config models, repo contracts, sources, and use cases
  - Android debug-only UI surface in `androidApp` or a dedicated Android-only debug module
  - Android app wiring for Process Phoenix restart on environment switch
- Affected systems:
  - local override persistence
  - future remote config integration boundary
  - debug-only internal entry path
  - release/update policy consumption
- New dependencies or integrations:
  - Process Phoenix for environment-switch restart behavior
