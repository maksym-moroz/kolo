# Apps

This directory is the home for app shells built on top of the Kolo platform baseline.

Rules:

- each app shell is thin and composes shared platform and capability modules
- app shells own branding, release identity, enabled capabilities, and app-specific integrations
- shared product and infrastructure code should not grow inside an app shell unless it is intentionally app-only

The current shell is:

- `reference/`: the canonical proof app for validating platform changes end to end
