## 1. Shared runtime-config domain

- [x] 1.1 Add typed shared models for `AppConfig`, `AppConfigOverride`, feature flags, version policy, URLs/constants, and predefined `dev` / `prod` environments
- [x] 1.2 Add a read-only `AppConfigRepo` contract that exposes reactive effective config to production consumers
- [x] 1.3 Add shared merge logic that resolves effective config from defaults, mocked remote overrides, and persisted local overrides with local overrides taking precedence
- [x] 1.4 Add a default config source that defines the complete starter-pack config values for proposal one
- [x] 1.5 Add a remote-config source boundary with an initial mock implementation that returns an empty override

## 2. Persisted overrides and use cases

- [x] 2.1 Add DataStore-backed local override storage for the full in-scope override model
- [x] 2.2 Add `UpdateAppConfigOverrideUseCase` for writing non-environment local overrides
- [x] 2.3 Add `ClearAppConfigOverrideUseCase` for clearing individual override fields
- [x] 2.4 Add `ResetAppConfigOverridesUseCase` for clearing all local overrides
- [x] 2.5 Persist the selected environment override through the same override-mutation path and let the Android boundary own the restart path

## 3. Android integration

- [x] 3.1 Wire the shared runtime-config domain into the Android graph so Android consumers can observe effective config and invoke use cases
- [x] 3.2 Add Android restart integration using Process Phoenix for environment-switch flows
- [x] 3.3 Ensure non-environment override changes update effective config immediately without app restart
- [x] 3.4 Ensure environment override changes restart the app and come back with the new effective environment

## 4. Debug menu surface

- [x] 4.1 Add the shared debug-menu feature location plus debug-only internal URI entry wiring at the app edge without exposing it as a normal production surface
- [x] 4.2 Implement a thin platform-hosted debug-menu presentation layer that observes effective config and dispatches override commands
- [x] 4.3 Render effective config in the debug menu with sections for environment, feature flags, version policy, and URLs/constants
- [x] 4.4 Support editing and persisting in-scope environment, feature-flag, and version-policy override fields from the debug menu while keeping URL/config fields visible and read-only
- [x] 4.5 Support clearing individual overrides and resetting all overrides from the debug menu

## 5. Validation and documentation

- [x] 5.1 Add or update tests that verify config precedence, persisted override restore, immediate non-environment updates, and environment-switch restart behavior
- [x] 5.2 Validate Android behavior end to end for debug-menu edits and environment switching
- [x] 5.3 Update planning and repo docs to map the new runtime-config and debug-menu boundaries after implementation lands
