# FND-003: Shared UDF Store Contract

## Goal

Lock a shared, reusable contract for feature state management before feature modules, persistence, and navigation logic spread across the repo.

## Multi-Module Direction

The store contract is now extracted into dedicated modules:

- `shared:core:store:api`
- `shared:core:store:impl`

That means:

- feature code should depend on the contract, not on ad hoc local store types
- the runtime is generic and feature-agnostic
- later shared decomposition can treat store as an already-isolated unit

## Implemented Contract

Key types:

- `UiIntent`
- `UiAction`
- `UiState`
- `UiEffect`
- `IntentMapper`
- `Reducer`
- `Middleware`
- `StoreScope`
- `Store`
- `StoreFactory`
- `DefaultStore`

## Flow

The current contract is intentionally strict and boring:

1. UI emits `UiIntent`
2. a feature-level `IntentMapper` maps intent to `UiAction`
3. `Store.dispatch()` queues actions
4. actions are processed serially
5. middleware can:
   - inspect current state
   - dispatch follow-up actions
   - emit `UiEffect`
6. reducer returns the next immutable `UiState`

## Why This Shape

- action processing is serialized, so reducers do not race each other
- middleware is explicit and side effects stay out of reducers
- effects are a separate stream and are not folded into persistent UI state
- the factory is DI-friendly and already exposed through Metro
- the contract is generic enough to survive the future multi-module split

## ViewModel Boundary

The intended ViewModel role stays thin:

- own coroutine scope
- collect `state`
- collect `effects`
- convert UI intents into actions
- call `store.dispatch()`

The ViewModel should not contain reducer logic or long-lived side-effect orchestration.

## Current Code Boundaries

- public contract: `shared/core/store/api/src/commonMain/kotlin/com/focus/kolo/store/`
- runtime implementation: `shared/core/store/impl/src/commonMain/kotlin/com/focus/kolo/store/impl/`
- store tests and fixtures: `shared/core/store/impl/src/commonTest/kotlin/com/focus/kolo/store/`
- DI exposure: `shared/src/commonMain/kotlin/com/focus/kolo/AppGraph.kt`
- current DI implementation: platform graphs in `shared/src/*Main/kotlin/com/focus/kolo/*AppGraph.kt`

## Feature Usage Pattern

Each feature should define:

- a feature `UiState`
- a feature `UiAction`
- a feature `UiEffect`
- a feature `IntentMapper`
- one reducer
- zero or more middlewares

The store itself is generic and should not know feature details.

## File And Package Discipline

Use one top-level type per Kotlin file.

In practice:

- actions get their own files
- state gets its own file
- effects get their own file
- reducers and middleware get their own files
- runtime implementations stay separate from public contracts

This keeps future extraction into `shared:core:store` and feature modules mechanical instead of disruptive.

## Non-Goals

This contract does not yet define:

- navigation model
- persistence model
- feature-specific state restoration
- a shared ViewModel base class
- logging, tracing, or time-travel tooling

Those can be layered on later if they remain justified.

## Validation

The baseline is covered by shared tests for:

- ordered reduction
- middleware-triggered effects
- middleware-triggered follow-up actions
