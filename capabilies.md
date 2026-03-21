# Evaluated App Capabilities

This file evaluates the 50-capability list you shared and marks each item for the current Kolo direction.

## Overall Take

The list is strong. It covers the real surfaces that separate a prototype from a launchable app.

The main adjustment for this repo is sequencing:

- Kolo is explicitly local-first before auth, sync, or cloud backup exist.
- reminders, deep links, notification taps, back behavior, migrations, and accessibility are already called out in planning as release-critical
- auth-heavy and growth-heavy items make sense, but several should wait until accounts, sync, and backend config are actually in scope

## Tag Legend

- `must have`: should shape architecture from the start
- `before release`: can be deferred during prototype, but should exist before public launch
- `low hanging fruit`: useful and often valuable, but not a core v1 gate

## Repo-Specific Priorities

If I were tightening this list for Kolo, I would push these to the front:

1. deep links, app links, and universal links
2. local notifications, notification tap routing, and reminder-safe timezone handling
3. offline-first storage, migrations, and state restoration
4. loading/error/empty/retry states
5. crash reporting, release-health monitoring, and performance telemetry
6. accessibility, localization readiness, and adaptive UI
7. feature flags, remote config, kill switches, and forced update policy

I would deliberately defer these until the product actually grows into them:

1. passkeys
2. biometric re-auth
3. audience segmentation
4. experimentation at scale
5. widgets and live activities
6. app integrity / attestation
7. admin tooling beyond the minimum support surface

## Release Control And Rollout

| # | Capability | Tag | Recommendation | Highlight |
| --- | --- | --- | --- | --- |
| 1 | Feature flags | `must have` | Add a shared flag abstraction now, even if the first implementation is local or static. | This aligns cleanly with the planned app-config direction and prevents risky hardcoded rollouts. |
| 2 | Remote config | `before release` | Introduce once a backend config endpoint exists; do not scatter ad hoc remote booleans through features. | Useful for reminder defaults, copy, and release mitigation without a full app update. |
| 3 | Kill switches | `must have` | Treat kill switches as a first-class subset of flags for notifications, sync, and risky integrations. | Exact reminder or sync bugs need a fast shutoff path. |
| 4 | Experimentation / A/B testing | `low hanging fruit` | Layer this on top of clean flags plus analytics, not as a separate system. | Valuable later, but not a starter-app blocker. |
| 5 | Audience segmentation | `low hanging fruit` | Defer until remote config and analytics are stable. | Personalization is useful, but premature segmentation adds noise fast. |
| 6 | Soft update prompts | `before release` | Add a non-blocking update path before broad public launch. | This gives you room between “nice to update” and “must update now.” |
| 7 | Forced update / minimum supported version | `before release` | Implement backend-driven version policy before release. | This is already called out in the architecture baseline and should not be improvised later. |
| 8 | Staged rollouts / phased releases | `before release` | Use store rollout controls as part of release process, even if they are not app code. | This is one of the cheapest ways to reduce blast radius. |
| 9 | In-app review prompts | `low hanging fruit` | Add only after the core experience is stable and the prompt timing is intentional. | Poorly timed review prompts hurt more than they help. |
| 10 | Release-health monitoring | `must have` | Put crash, ANR, and performance monitoring in place before inviting real users. | Shipping without release health is operating blind. |

## Entry Points And Navigation

| # | Capability | Tag | Recommendation | Highlight |
| --- | --- | --- | --- | --- |
| 11 | Deep links | `must have` | Define typed routes and shared parsing early in shared code. | This is explicitly a release-readiness item in the repo planning docs. |
| 12 | Android App Links | `before release` | Add verified web-to-app routing once public URLs exist. | Better trust and fewer chooser interruptions than plain custom schemes. |
| 13 | iOS Universal Links | `before release` | Mirror the same route registry used by Android and web fallback pages. | Universal links are the clean iOS path for durable shared content links. |
| 14 | Notification tap routing | `must have` | Treat reminder tap payloads as typed navigation input, not loose extras. | Reminder apps feel broken instantly when taps land on the wrong screen. |
| 15 | Navigation state restoration | `must have` | Persist enough route and screen state to survive process death and relaunch. | This matters more in a local-first app because users expect continuity. |
| 16 | Platform-native back behavior | `must have` | Honor predictive back on Android and standard hierarchy/close patterns on iOS. | The planning docs already call this out as a baseline expectation. |
| 17 | Modal, sheet, and overlay discipline | `must have` | Define modal rules before screens start inventing their own navigation semantics. | This avoids restoration, accessibility, and back-stack chaos later. |
| 18 | Multiple back stacks / tab memory | `low hanging fruit` | Add only if top-level tabs or sections justify it. | Useful, but not necessary before the IA actually needs it. |
| 19 | Search and suggestions | `low hanging fruit` | Defer until tasks or journals are dense enough that browsing stops being enough. | Search is valuable, but not the first constraint in a starter app. |
| 20 | Share sheet / open-in / import-export | `low hanging fruit` | Add when the product starts benefiting from content ingress or egress. | Strong quality-of-life feature, especially for notes and exports. |

## Engagement And Product Surface

| # | Capability | Tag | Recommendation | Highlight |
| --- | --- | --- | --- | --- |
| 21 | Push notifications | `before release` | Add once server-backed sync or remote events exist; do not confuse this with core reminder delivery. | Useful later, but local notifications matter earlier for Kolo. |
| 22 | Local notifications and reminders | `must have` | Model reminder intent safely and schedule notifications with timezone-aware data. | This is central to the app shape, not an optional enhancement. |
| 23 | Notification channels, categories, and actions | `before release` | Add structured notification controls before broad launch. | Users need control, and reminder actions can materially improve completion flows. |
| 24 | In-app messaging / announcements | `low hanging fruit` | Add only after you have something meaningful to announce in-app. | Helpful for maintenance or feature discovery, but not foundational. |
| 25 | Widgets / Live Activities / glanceable surfaces | `low hanging fruit` | Defer until the core reminder/task loop is stable. | Great surface area for habit products, but not where the architecture risk sits. |
| 26 | App shortcuts / quick actions | `low hanging fruit` | Add once the top 2 to 3 common actions are obvious from actual usage. | Fast capture flows benefit from this, especially for adding tasks quickly. |
| 27 | Personalization | `low hanging fruit` | Start small with defaults and settings before chasing behavioral personalization. | Avoid inventing personalization before the data model is mature. |
| 28 | Onboarding and progressive disclosure | `before release` | Keep first-run focused on first value, permissions, and reminder setup. | The right onboarding matters more than a long tour. |
| 29 | Analytics event taxonomy | `must have` | Define event names and properties before instrumentation spreads. | This supports release decisions, funnels, and later experiments. |
| 30 | Product and store analytics reports | `low hanging fruit` | Review once distribution channels and acquisition matter. | Useful for growth loops, but not a core app architecture concern. |

## Identity, Security, And Privacy

| # | Capability | Tag | Recommendation | Highlight |
| --- | --- | --- | --- | --- |
| 31 | A first-class authentication foundation | `before release` | Plan the auth boundary early, but do not force it ahead of the local-first MVP. | The architecture explicitly says the app should be useful before auth exists. |
| 32 | Passkeys | `before release` | Add when account auth becomes real; do not build password-only auth first if you can avoid it. | Strong long-term choice, but not a day-one requirement for the current product shape. |
| 33 | Biometric re-auth for sensitive actions | `low hanging fruit` | Add only if the app starts holding sensitive account, payment, or private-content operations. | Nice trust layer, but not central to the current starter scope. |
| 34 | Credential management, recovery, and revocation | `before release` | Treat recovery and revocation as part of auth, not post-launch cleanup. | Account systems fail in the edges, not the happy path. |
| 35 | Permission orchestration | `before release` | Request notifications and any future permissions in context, not on first launch. | Permission UX directly affects opt-in and perceived quality. |
| 36 | Secure storage | `must have` | Use Keychain / Keystore equivalents for session and secret material from the start. | This is foundational hygiene, not optional hardening. |
| 37 | Secure transport policy | `before release` | Lock down HTTPS/TLS policy before any real backend traffic becomes important. | Good transport defaults prevent subtle security drift. |
| 38 | App integrity / attestation | `low hanging fruit` | Defer unless fraud, abuse, or monetization risk makes it necessary. | Useful in the right threat model, but easy to over-apply too early. |
| 39 | Privacy center, data choices, and account deletion | `before release` | Build this once accounts or remote user data are public-facing. | Privacy obligations accelerate quickly once user data leaves the device. |
| 40 | SDK governance and privacy disclosures | `before release` | Audit every third-party SDK and keep disclosures accurate before release. | Third-party SDK debt becomes store-review and trust debt fast. |

## Reliability, Inclusivity, And Shipping Quality

| # | Capability | Tag | Recommendation | Highlight |
| --- | --- | --- | --- | --- |
| 41 | Offline-first local source of truth | `must have` | Keep shared persistence as the real source of truth for core features. | This is already the central architectural direction for the repo. |
| 42 | Background sync and retries | `before release` | Add durable schedulers and retry semantics when sync actually arrives. | Important for future cloud behavior, but can follow the local-first baseline. |
| 43 | Conflict resolution | `before release` | Define merge policy before multi-device sync leaves prototype stage. | Conflicts are where “sync works” claims usually break down. |
| 44 | Loading, error, empty, and retry states | `must have` | Create reusable patterns in shared UI instead of one-off screen logic. | This is low-cost polish with very high perceived quality impact. |
| 45 | Crash reporting | `must have` | Add release-aware crash capture before external testing. | There is no serious launch story without this. |
| 46 | Performance monitoring | `before release` | Capture startup, render, and network performance before public launch. | Regressions compound quietly in cross-platform apps. |
| 47 | Accessibility | `before release` | Treat semantics, dynamic type, contrast, and focus order as baseline QA. | The architecture docs already name this as a release-readiness concern. |
| 48 | Localization and per-app language | `before release` | Externalize strings early even if v1 launches in one language. | This is cheaper before strings spread through the UI. |
| 49 | Adaptive UI: dark mode, large text, and large screens | `before release` | Build layout and typography systems that survive font scaling and larger form factors. | Adaptive behavior is part of “modern app quality,” not a separate premium feature. |
| 50 | Automated UI tests, beta distribution, and pre-launch scanning | `before release` | Put repeatable release validation in place before broad rollout. | This is what turns release confidence into a process instead of a feeling. |

## Bottom Line

Yes, the list makes sense.

For this repo specifically, I would make sure the team does not dilute focus. The highest-value launch path is:

1. offline-first persistence and migration safety
2. reminder correctness and notification tap routing
3. deep links, route discipline, and back behavior
4. crash and release-health visibility
5. accessibility, localization readiness, and adaptive UI
6. release control with flags, kill switches, and update policy

I would not let passkeys, A/B testing, widgets, or attestation get ahead of those.
