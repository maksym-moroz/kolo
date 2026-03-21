# Multi-Agent Workflow

This repo should be worked as a coordinated multi-agent system, not as a pile of independent edits.

## Roles

Use these roles even if one human or one primary agent is coordinating them all:

- `Coordinator`: owns planning, sequencing, integration, and ticket state
- `Worker`: owns one ticket with one declared write scope
- `Reviewer`: validates behavior, tests, and risks before a ticket is marked `Done`

One person or agent can play multiple roles, but not on the same ticket at the same moment.

## Core Rule

Every ticket must declare:

- `Owner`
- `Status`
- `Write scope`
- `Dependencies`
- `Done when`

If write scope is not clear, the ticket is not ready.

## Ticket Lifecycle

1. `Draft`
2. `Ready`
3. `In Progress`
4. `Review`
5. `Done`

Only one owner should move a ticket into `In Progress`.

## Write Scope Rule

Parallel work is allowed only when write scopes do not overlap.

Good parallel split examples:

- one agent updates root build and module files
- another designs persistence schema docs only
- another works on server route contracts

Bad parallel split examples:

- two agents editing the same Gradle module
- two agents changing the same feature reducer files
- one agent refactoring persistence while another builds tasks on top of it

## Handoff Format

When an agent stops work, leave a handoff note with:

- what changed
- what did not change
- validation performed
- remaining risk
- next obvious step

## Planning Rules

- keep tickets small enough for one focused session
- separate architecture tickets from implementation tickets when possible
- split cross-cutting work by stable boundaries such as module, feature, or platform
- prefer one integration ticket over several agents touching the same shared boundary

## Definition Of Ready

A ticket is `Ready` when:

- the goal is explicit
- the dependency chain is known
- write scope is declared
- the owner will know when the ticket is done

## Definition Of Done

A ticket is `Done` when:

- the scoped change is complete
- tests or validation expected by the ticket were run or consciously deferred
- any relevant docs were updated
- known risks are recorded

## Working Set Recommendation

Keep the active working set small:

- at most 1 coordinator-owned integration ticket
- at most 2 to 4 implementation tickets in progress at once
- no hidden side work outside the backlog

## Ticket Template

Use this format for new tickets:

```md
### TICKET-ID: Short title

- Status: Ready
- Owner: Unassigned
- Write scope: `path/or/module`
- Dependencies: TICKET-123, none
- Goal: one clear outcome
- Done when:
  - outcome 1
  - outcome 2
- Notes:
  - risk or coordination note
```

## Document Update Rule

If a ticket changes architecture, platform assumptions, or sequencing, update:

- [starter-architecture.md](/Users/maksymmoroz/startup/kolo/docs/planning/starter-architecture.md)
- [backlog.md](/Users/maksymmoroz/startup/kolo/docs/planning/backlog.md)

If a ticket changes one deep domain, update its workstream file too.
