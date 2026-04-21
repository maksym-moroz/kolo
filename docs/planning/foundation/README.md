# Foundation

This folder holds the first execution-ready planning artifacts for the starter app.

## Artifacts

- [agp9-migration.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/agp9-migration.md): target module tree, current-to-future mapping, migration phases, and cut lines
- [version-baseline.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/version-baseline.md): pinned stack baseline, compatibility notes, and locked versus flexible versions
- [update-automation-strategy.html](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/update-automation-strategy.html): owner-facing recommendation for update bots, dependency integrity, and release hardening order
- [quality-tooling-map.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/quality-tooling-map.md): source-of-truth map for repository quality tasks, module conventions, and CI workflows
- [udf-store-contract.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/udf-store-contract.md): shared UDF/store contract and the extracted `shared:core:store:*` module boundaries

## Working Order

1. Read [agp9-migration.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/agp9-migration.md)
2. Confirm [version-baseline.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/version-baseline.md)
3. Read [update-automation-strategy.html](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/update-automation-strategy.html) before introducing update bots, lockfiles, dependency verification, or release-integrity tooling
4. Read [quality-tooling-map.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/quality-tooling-map.md) before changing CI, formatting, lint, or dependency analysis
5. Read [udf-store-contract.md](/Users/maksymmoroz/startup/kolo/docs/planning/foundation/udf-store-contract.md) before building feature state layers
6. Use those docs to drive implementation tickets from [backlog.md](/Users/maksymmoroz/startup/kolo/docs/planning/backlog.md)
