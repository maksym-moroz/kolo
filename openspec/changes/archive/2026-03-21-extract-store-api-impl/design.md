## Context

The current store contract was intentionally cleaned up into an API/runtime package split inside the broad `shared` module. That is a useful intermediate step, but it still leaves feature-facing abstractions and concrete implementation coupled to the same Gradle module. The project direction already points toward a deeper multi-module `shared:core:*` layout, and store is the cleanest next candidate because it has a narrow dependency surface and clear API/implementation boundaries.

This change is cross-cutting because it touches Gradle structure, Metro wiring, shared code ownership, and feature dependency rules. It also sets a pattern that later extractions such as `model`, `database`, and `preferences` are likely to follow.

## Goals / Non-Goals

**Goals:**

- Extract the current store contract into `shared:core:store:api`.
- Extract the current default store implementation into `shared:core:store:impl`.
- Preserve current store behavior while changing module boundaries.
- Make future feature modules depend only on store abstractions.
- Keep the extraction mechanical so the later `shared` decomposition is easier.

**Non-Goals:**

- Redesigning the store contract itself.
- Changing navigation, persistence, or feature state semantics.
- Splitting the entire `shared` module in one ticket.
- Adding alternate store implementations, logging, or tracing infrastructure.

## Decisions

### 1. Use exactly two modules: `api` and `impl`

The extraction will create:

- `:shared:core:store:api`
- `:shared:core:store:impl`

Why:

- the contract/implementation boundary is already explicit in code
- two modules are enough to isolate feature dependencies from concrete runtime code
- adding more modules now would create ceremony without immediate benefit

Alternatives considered:

- keep everything in `shared`: too much coupling for future feature modules
- split into more than two modules now: premature and harder to land safely

### 2. Keep Metro wiring at the app/shared graph edge

The API module will remain DI-agnostic except for the exposed contract types. The implementation module will own `DefaultStoreFactory`, and the existing platform graphs will bind the implementation to the API-facing `StoreFactory`.

Why:

- features should not know which implementation is chosen
- Metro bindings already exist at the platform graph edge
- this preserves the current DI pattern and avoids graph churn in the same change

Concrete decision:

- keep Metro binding simple in platform graphs
- bind `StoreFactory <- DefaultStoreFactory`
- do not introduce a separate store-specific binding surface in this change

### 3. Move package structure with the module split

The API module will own the public `com.focus.kolo.store` package. The implementation module will own `com.focus.kolo.store.impl`.

Why:

- preserves the “one top-level type per file” cleanup already completed
- keeps extraction aligned with package meaning
- minimizes future import churn

Concrete decision:

- keep public package names stable
- do not rename API packages to `.api`
- rename the current implementation-only `runtime` package to `impl` during extraction

### 4. Rewire consumers directly to the new modules

This extraction will not use the broad `shared` module as a compatibility shell. Current consumers and app wiring will be updated directly to `:shared:core:store:api` and `:shared:core:store:impl`.

Why:

- the user explicitly wants the dependency correction, not a temporary bridge
- the store surface is narrow enough to rewire directly
- this produces the clean dependency shape immediately

### 5. Keep implementation exposure narrow

`DefaultStoreFactory` will stay public in `impl` because platform graphs bind to it. `DefaultStore` should be internal in `impl` wherever Kotlin visibility allows cleanly.

Why:

- `StoreFactory` is the public abstraction
- `DefaultStoreFactory` is the concrete DI entry point
- `DefaultStore` itself should remain an implementation detail

### 6. Move store-specific tests into impl

Store-specific tests and fixtures will move to `:shared:core:store:impl`.

Why:

- the API module should remain contract-focused
- `DefaultStore` behavior belongs to the implementation module
- this keeps module responsibilities easier to reason about

## Risks / Trade-offs

- [Gradle/module churn introduces build breakage] → Keep the extraction limited to store and validate Android, JVM, and iOS compilation after each cut
- [Metro bindings drift during module move] → Keep the `StoreFactory` binding at the existing app-graph edge and bind `DefaultStoreFactory` directly without additional abstraction
- [Package/import churn leaks into unrelated code] → Preserve the public `com.focus.kolo.store` package and move implementation classes into `com.focus.kolo.store.impl`
- [Extraction sets a bad pattern for later modules] → Keep the split simple and use it as the reference pattern for future `shared:core:*` moves

## Migration Plan

1. Create `shared/core/store/api` and `shared/core/store/impl` modules in Gradle.
2. Move the public store contract files into the API module.
3. Move `DefaultStore` and `DefaultStoreFactory` into the implementation module and rename implementation packages to `impl`.
4. Make `impl` depend on `api`, and rewire current consumers directly to the new modules.
5. Move store-specific tests and fixtures into `impl`.
6. Remove the old in-module store code from the broad `shared` module.
7. Validate with shared tests plus Android, JVM, and iOS Kotlin compilation.

Rollback strategy:

- revert the module additions and move the store files back into the broad `shared` module
- preserve package names during implementation so rollback is mechanical if needed

## Open Questions

- Should `AppGraph` remain in the broad `shared` module for now, or move when the broader `shared:core:*` decomposition starts?
