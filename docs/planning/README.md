# Planning Workspace

This folder is the working agreement and delivery board for the Kolo starter app.

It exists for two reasons:

1. Preserve architecture and product decisions outside transient chat history.
2. Give multiple agents a shared operating model so they can work in parallel without colliding.

## Source Of Truth

- [index.html](/Users/maksymmoroz/startup/kolo/docs/planning/index.html): owner-facing HTML entry point for the forward-facing planning set
- [repo-overview.html](/Users/maksymmoroz/startup/kolo/docs/planning/repo-overview.html): high-level repo identity, module map, runtime path, and roadmap
- [starter-architecture.html](/Users/maksymmoroz/startup/kolo/docs/planning/starter-architecture.html): owner-facing HTML summary of the current architecture baseline
- [platform-charter.html](/Users/maksymmoroz/startup/kolo/docs/planning/platform-charter.html): owner-facing HTML charter for what the platform baseline owns
- [app-shell-contract.html](/Users/maksymmoroz/startup/kolo/docs/planning/app-shell-contract.html): owner-facing HTML contract for app shells
- [app-factory-workflow.html](/Users/maksymmoroz/startup/kolo/docs/planning/app-factory-workflow.html): owner-facing HTML launch workflow for new app shells
- [next-capabilities.html](/Users/maksymmoroz/startup/kolo/docs/planning/next-capabilities.html): owner-facing HTML list of the next shared core capabilities to build
- [ios-compose-navigation3-cut.html](/Users/maksymmoroz/startup/kolo/docs/planning/ios-compose-navigation3-cut.html): owner-facing implementation summary for the iOS host move to shared Compose and the first Navigation 3 root
- [starter-architecture.md](/Users/maksymmoroz/startup/kolo/docs/planning/starter-architecture.md): current architectural baseline, stack choices, and major trade-offs
- [platform-charter.md](/Users/maksymmoroz/startup/kolo/docs/planning/platform-charter.md): durable mission, ownership rules, and exit criteria for the reusable baseline
- [app-shell-contract.md](/Users/maksymmoroz/startup/kolo/docs/planning/app-shell-contract.md): what every app shell must own and what must stay in the platform
- [app-factory-workflow.md](/Users/maksymmoroz/startup/kolo/docs/planning/app-factory-workflow.md): first-cut workflow for creating and validating new app shells
- [multi-agent-workflow.md](/Users/maksymmoroz/startup/kolo/docs/planning/multi-agent-workflow.md): how agents claim work, define write scope, hand off, and close tickets
- [backlog.md](/Users/maksymmoroz/startup/kolo/docs/planning/backlog.md): prioritized epics and tickets
- [foundation/README.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/README.md): entry point for the AGP 9 and version baseline artifacts
- [foundation/update-automation-strategy.html](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/update-automation-strategy.html): owner-facing update automation, dependency integrity, and release hardening strategy
- [foundation/quality-tooling-map.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/quality-tooling-map.md): durable map for CI, formatting, static analysis, linting, and dependency health wiring
- [foundation/udf-store-contract.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/udf-store-contract.md): implemented shared UDF/store contract
- [workstreams/navigation.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/navigation.md): layered navigation program, route ownership, back behavior, modal policy, deep links, and migration path
- [workstreams/persistence.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/persistence.md): deep notes and ticket detail for local persistence

Documentation format preference:

- prefer HTML for forward-facing documents that are primarily meant to keep the repo owner in the loop
- this includes repo overviews, architecture maps, platform/source-of-truth summaries, operating models, and similar high-signal reading surfaces
- use Markdown freely for backlog, workstream detail, implementation notes, and other agent-facing documents where line diffs and lightweight editing matter more
- if an HTML source-of-truth document disagrees with code or build files, update the HTML document to match the implementation reality

## Status Model

Use these statuses consistently in backlog tickets:

- `Draft`: idea captured but not ready for implementation
- `Ready`: scoped, unblocked, and safe to assign
- `In Progress`: actively owned by one agent
- `Blocked`: cannot proceed without an answer or dependency
- `Review`: implementation done, awaiting verification or integration
- `Done`: merged or otherwise accepted

## Operating Rule

One ticket, one owner, one declared write scope.

If two agents need the same write scope, split the ticket or serialize the work.

## Current Repo Reality

- the AGP 9 first cut is already implemented with `:androidApp` as the real Android boundary, and the current reference app hosts now live under `apps/reference/`
- Android Baseline Profile generation now lives in a dedicated `baselineprofile` test module
- the app-shell identity and capability contract now lives in extracted `shared:core:appshell`
- shared runtime config now exists in extracted `shared:core:config:api` and `shared:core:config:impl` modules, with Android and iOS debug-menu tooling surfaces as its first internal consumers
- shared Gradle wiring now lives in the `build-logic` included build
- the shared store contract is already extracted into `shared:core:store:api` and `shared:core:store:impl`
- the next planning focus is persistence, navigation, reminders, and app-shell scaling on top of that baseline

## Suggested Cadence

- Planning: pull from `Ready` into the current working set
- Execution: agents claim tickets and update status before editing
- Handoff: agents leave a short result, risk note, and validation note
- Integration: one coordinating agent closes the ticket and updates the backlog

## Current Focus

The current highest-value sequence is:

1. `PERS-001` through `PERS-004` to establish durable local persistence on top of the extracted store modules
2. `NAV-001` through `NAV-004` plus `REM-001` so navigation and reminders do not get bolted on later
3. first feature slices for tasks and journaling
4. deeper shared module decomposition after the first feature boundaries are clearer
