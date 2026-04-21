## Context

The app does not yet have a shared runtime configuration boundary. Feature flags, forced-update policy, URLs, and environment-specific behavior would otherwise be introduced ad hoc in unrelated files, which would couple feature code to debug tooling and make later remote config integration harder than it needs to be.

This change introduces a cross-cutting runtime-config pattern that will be consumed by an Android debug-menu tooling surface. It also establishes a controlled path for environment switching, including clean app restart on environment changes, while keeping production feature code dependent only on the effective config contract.

Constraints:

- Naming and layering should align with the repo's existing vocabulary: `Repo`, `UseCase`, and `ViewModel`, while keeping internal tooling responsibilities explicit instead of forcing a fake feature state machine.
- The runtime-config domain should live in shared Kotlin.
- The debug menu should stay platform-hosted internal tooling rather than product UI, with Android and iOS hosts free to present the shared surface differently at the app edge.
- The debug menu should be reachable through a debug-only internal URI entry path for the first iteration.
- Remote config support should exist as an architectural boundary now, but its initial implementation should return no overrides.
- Local overrides must persist on device and be able to override every field in scope for this proposal.

## Goals / Non-Goals

**Goals:**

- Provide one typed effective app config that production code can observe reactively.
- Model defaults, remote config, and local overrides truthfully instead of forcing every source to own a full config.
- Keep debug-only mutation capabilities out of the normal read-side repo contract.
- Introduce a meaningful starter pack of config categories: feature flags, version policy, URLs/constants, and environment selection.
- Provide an Android debug-menu surface that can inspect and edit local overrides.
- Use DataStore for persisted local override storage.
- Restart the app cleanly via Process Phoenix when the selected environment changes.

**Non-Goals:**

- Implement real remote config fetching or backend integration.
- Support server-driven UI, experimentation tooling, or dynamic key/value config maps.
- Add numeric/behavioral limits or broader debug tooling controls in this proposal.
- Treat the debug menu as a normal product route or release-facing feature.
- Guarantee that every future config concern belongs in this system.
- Make URL/config fields editable in the first debug-menu UI.

## Decisions

### 1. Use a full typed `AppConfig` plus a partial typed `AppConfigOverride`

The effective runtime configuration will be represented as a full typed `AppConfig`. Defaults will own a full `AppConfig`, while remote and local sources will own a partial typed `AppConfigOverride`.

Why:

- Defaults truthfully define the complete app behavior.
- Remote and local sources truthfully express only changed fields.
- Merge logic stays centralized and type-safe.
- Consumers only read one stable effective model.

Alternatives considered:

- Full `AppConfig` from every source: rejected because remote and local sources would need fake/default values for fields they do not own.
- Per-key string maps: rejected because they weaken type safety, spread parsing/validation, and invite config sprawl.

### 2. Expose a read-only `AppConfigRepo` and keep mutations behind use cases

Production-facing code will depend on a read-only `AppConfigRepo` that exposes the effective config reactively. Debug mutation capabilities will be expressed as use cases such as `UpdateAppConfigOverrideUseCase`, `ClearAppConfigOverrideUseCase`, and `ResetAppConfigOverridesUseCase`, with environment changes flowing through the same override-mutation path.

Why:

- Debug-only mutation APIs should not leak through the normal config contract.
- `Repo` and `UseCase` naming stays aligned with the rest of the app architecture.
- Feature code can depend on the minimum truthful contract it needs.

Alternatives considered:

- Expose override mutation methods directly from `AppConfigRepo`: rejected because it couples product code to debug/admin responsibilities.
- Introduce manager/controller/runtime naming: rejected because it drifts from the established naming scheme.

### 3. Keep the effective config reactive

`AppConfigRepo` will expose a reactive stream of the effective config so that non-environment override changes can apply immediately.

Why:

- The user explicitly wants local edits to be useful immediately.
- A reactive contract keeps the debug menu and any future consumers in sync with actual app behavior.
- Future remote config refreshes can plug into the same merge pipeline naturally.

Alternatives considered:

- Read-on-demand only: rejected because it makes immediate application awkward and encourages screens to re-query manually.

### 4. Treat environment switching as a special case that requires restart

The selected environment will be just another field in the config model, but changing it will persist the override and then trigger a clean restart via Process Phoenix while preserving all other local overrides.

Why:

- Environment changes affect networking and process-wide assumptions.
- Restarting is simpler and safer than trying to hot-swap all environment-dependent state in place.
- Preserving the rest of the override set makes environment switching operationally useful without forcing the user to rebuild local debug state after each restart.

Alternatives considered:

- Apply environment changes immediately without restart: rejected because it is more error-prone and harder to reason about.
- Lock `prod` selection: rejected because the agreed requirement is that `prod` must be selectable.

### 5. Keep the remote source real in architecture but empty in behavior

This proposal will define a real remote-config source boundary now, but the initial implementation will return an empty override.

Why:

- The architecture becomes future-ready without forcing backend work into this slice.
- The merge path is exercised from the start.
- It avoids retrofitting a third source later after features already depend on a simpler local-only shape.

Alternatives considered:

- Local-only architecture for now: rejected because the user explicitly wants the full layering in place.
- Real backend integration now: rejected because it is outside the intended scope.

### 6. Keep the debug menu as an observer-plus-command tooling surface

The shared debug menu should expose the effective runtime config truthfully, let the UI issue explicit override commands, and keep platform-specific behavior such as Android Process Phoenix restart at the platform boundary.

Why:

- It avoids inventing debug-menu-specific business state when the source of truth already lives in the shared runtime-config repo.
- It keeps transient UI concerns separate from persisted runtime-config state.
- It prevents the debug menu from becoming an unstructured dumping ground without forcing ceremonial reducer state that does not own truth.

Alternatives considered:

- Build a thin utility activity/fragment with direct repo calls: rejected because it bypasses the repo-plus-use-case boundary and makes Android own too much assembly logic.
- Build unrelated product-specific debug surfaces per platform: rejected because the internal tooling surface should stay shared even though platform hosts present it differently.

### 7. Use a debug-only internal URI entry path as the first access path

The first debug-menu entry path will be a debug-only internal URI entry path instead of a gesture, overflow action, or launcher-level entry.

Why:

- It avoids polluting product UI before the app has a settled shell.
- It is easy to invoke from adb, tests, and internal developer workflows.
- It does not require treating the debug menu as part of the product route model.

Alternatives considered:

- Hidden gesture: rejected because it is harder to discover and test.
- Debug-only visible menu action: rejected because the product shell is not stable enough yet to justify wiring one in.
- Dedicated launcher/debug activity: rejected because the debug menu is a feature surface, not a second app shell.

### 8. Use DataStore for persisted local overrides

Persisted local overrides will be stored in DataStore.

Why:

- The repo already points at DataStore KMP as the settings-store direction.
- The override payload is settings-like rather than relational.
- It keeps override persistence boring and easy to reason about.

Alternatives considered:

- Room: rejected because the override set is configuration data, not relational domain data.
- SharedPreferences: rejected because DataStore is the intended baseline direction.

### 9. Keep URL/config fields visible but read-only in the first debug-menu UI

URL/config fields in scope for this proposal will be part of the override model, but the first debug-menu UI will display them without allowing in-place editing.

Why:

- It keeps the first UI smaller while still making effective config visible.
- The architecture still supports those fields as overrides without forcing text-edit UX into the first implementation.

Alternatives considered:

- Full text editing for URLs now: rejected because it adds more UX and validation surface than needed for proposal one.

## Risks / Trade-offs

- [Config surface grows too quickly] → Keep proposal 1 limited to flags, version policy, URLs/constants, and environment selection; defer numeric limits and additional tooling to a second proposal.
- [Debug menu leaks into release behavior] → Keep the debug menu internal and debug-oriented, with product code reading only `AppConfigRepo` and platform-specific entry/restart behavior owned at the app edge.
- [Environment switching causes inconsistent state] → Persist the override first, then restart via Process Phoenix instead of hot-swapping process-wide dependencies.
- [Remote-ready architecture adds overhead before it is needed] → Use an empty remote override implementation initially so the boundary exists without adding backend complexity.
- [Overrides become hard to reason about] → Keep precedence explicit: defaults, then remote override, then local override.
- [Debug-menu access pattern changes later] → Start with a debug-only internal URI entry so future shell changes do not invalidate the config surface itself.
