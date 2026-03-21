# Planning Workspace

This folder is the working agreement and delivery board for the Kolo starter app.

It exists for two reasons:

1. Preserve architecture and product decisions outside transient chat history.
2. Give multiple agents a shared operating model so they can work in parallel without colliding.

## Source Of Truth

- [starter-architecture.md](/Users/maksymmoroz/startup/kolo/docs/planning/starter-architecture.md): current architectural baseline, stack choices, and major trade-offs
- [multi-agent-workflow.md](/Users/maksymmoroz/startup/kolo/docs/planning/multi-agent-workflow.md): how agents claim work, define write scope, hand off, and close tickets
- [backlog.md](/Users/maksymmoroz/startup/kolo/docs/planning/backlog.md): prioritized epics and tickets
- [foundation/README.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/README.md): entry point for the AGP 9 and version baseline artifacts
- [foundation/udf-store-contract.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/udf-store-contract.md): implemented shared UDF/store contract
- [workstreams/persistence.md](/Users/maksymmoroz/startup/kolo/docs/planning/workstreams/persistence.md): deep notes and ticket detail for local persistence

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

## Suggested Cadence

- Planning: pull from `Ready` into the current working set
- Execution: agents claim tickets and update status before editing
- Handoff: agents leave a short result, risk note, and validation note
- Integration: one coordinating agent closes the ticket and updates the backlog

## Current Focus

The current highest-value sequence is:

1. `PERS-001` through `PERS-004` to establish durable local persistence on top of the extracted store modules
2. `NAV-001` and `REM-001` so navigation and reminders do not get bolted on later
3. first feature slices for tasks and journaling
4. deeper shared module decomposition after the first feature boundaries are clearer
