## 1. Module Setup

- [x] 1.1 Create `:shared:core:store:api` and `:shared:core:store:impl` modules and include them in Gradle settings
- [x] 1.2 Add the required Kotlin/Android-KMP/Metro/coroutines configuration to both new modules
- [x] 1.3 Wire `:shared:core:store:impl` to depend on `:shared:core:store:api`

## 2. API Extraction

- [x] 2.1 Move the public store contract types from the broad `shared` module into `:shared:core:store:api`
- [x] 2.2 Preserve the public `com.focus.kolo.store` package and imports during the move
- [x] 2.3 Update consumers so shared feature-facing code depends on `:shared:core:store:api`

## 3. Implementation Extraction

- [x] 3.1 Move `DefaultStore` and `DefaultStoreFactory` into `:shared:core:store:impl`
- [x] 3.2 Rename implementation-only packages from `runtime` to `impl`
- [x] 3.3 Keep `DefaultStoreFactory` public and reduce `DefaultStore` visibility where possible
- [x] 3.4 Rewire Metro bindings so platform graphs bind `StoreFactory` to `DefaultStoreFactory`

## 4. Direct Rewiring

- [x] 4.1 Update current consumers to depend directly on `:shared:core:store:api`
- [x] 4.2 Update current app wiring to depend directly on `:shared:core:store:impl`
- [x] 4.3 Remove the old in-module store sources from `shared` without leaving a compatibility bridge
- [x] 4.4 Move store-specific tests and fixtures into `:shared:core:store:impl`

## 5. Validation And Docs

- [x] 5.1 Validate with store tests plus Android, JVM, and iOS Kotlin compilation
- [x] 5.2 Update planning and agent-facing docs to point at `shared:core:store:api` and `shared:core:store:impl`
- [x] 5.3 Record any follow-up cleanup left after the first extraction cut
