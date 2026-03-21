# App Capabilities

This is a ranked capability baseline for a new app, assuming a mobile-first product with backend services and a public launch path.

## Tag Legend

- `must have`: foundation work that should be designed in from the start
- `before release`: can be rough during prototype, but should be in place before public launch
- `low hanging fruit`: relatively cheap, high-leverage additions once the core loop works

## Recommendations

- Build the routing, auth, observability, and flagging foundation early. These are painful to retrofit.
- Treat privacy, accessibility, update control, and migration safety as release gates, not polish.
- Use the `low hanging fruit` items to improve activation, support load, and retention after the core product flow is stable.

## Ranked Capabilities

| Rank | Capability | Tag | Recommendation | Highlight |
| --- | --- | --- | --- | --- |
| 1 | Authentication, session restore, and account recovery | `must have` | Support durable sessions, logout everywhere, and passwordless or recovery-safe flows from day one. | Broken auth poisons trust faster than almost any other failure. |
| 2 | Deep links, universal links, and route contracts | `must have` | Define one canonical route model shared across app, web fallback, notifications, and marketing links. | Shared links should always open the right screen, not the home screen. |
| 3 | Crash reporting and production error alerting | `must have` | Add crash capture, structured error context, and alert thresholds before real users touch the app. | If production breaks and nobody sees it, shipping speed becomes guesswork. |
| 4 | Analytics with a clean event taxonomy | `must have` | Track activation, retention, conversion, and drop-off events with stable names and documented meanings. | You need to know which behavior matters before optimizing anything. |
| 5 | Feature flags, remote config, and kill switches | `must have` | Put risky features behind server-controlled flags and keep a global emergency disable path. | This is the fastest way to reduce blast radius without waiting for app review. |
| 6 | Secure storage and secret handling | `must have` | Store tokens, keys, and sensitive preferences in platform-secure storage with short exposure windows. | Most security failures start as convenience shortcuts around secrets. |
| 7 | Offline cache and state restoration | `must have` | Cache last known data and restore user context after app restarts, drops, or transient network failures. | Users expect the app to feel alive even when the network is not. |
| 8 | Background sync, retry queues, and idempotent writes | `must have` | Queue writes safely, retry failures, and make server operations idempotent so retries do not duplicate state. | Reliable sync is what makes mobile apps feel trustworthy. |
| 9 | Observability logs with request and user correlation IDs | `must have` | Standardize logs so support, backend, and client telemetry can be stitched together quickly. | Debugging cross-platform issues is dramatically cheaper with correlation IDs. |
| 10 | Minimum supported version checks and forced update paths | `before release` | Add version gating for incompatible APIs, security fixes, and hard deprecations before wide rollout. | A public app needs a way to retire broken clients safely. |
| 11 | Loading, empty, offline, and error state patterns | `must have` | Create reusable UI patterns instead of inventing edge-state handling screen by screen. | Polished state handling makes the product feel finished long before extra features do. |
| 12 | Push notifications with granular preferences | `before release` | Support useful notifications, notification categories, and per-topic opt-ins instead of one global switch. | Notifications are either a retention lever or an uninstall trigger. |
| 13 | Accessibility baseline | `before release` | Meet baseline screen-reader, contrast, touch-target, and dynamic type support before public launch. | Accessibility fixes are cheapest when the component system is still small. |
| 14 | Permission prompt orchestration | `before release` | Ask late, explain why, and only request permissions in the exact context that needs them. | Premature permission prompts reduce opt-in rates and credibility. |
| 15 | Privacy controls, consent, and account deletion | `before release` | Expose privacy settings, consent capture where needed, and a real delete/export path. | Privacy debt turns into legal and trust debt quickly. |
| 16 | Localization, locale, and timezone correctness | `before release` | Externalize strings early and make dates, currencies, and reminder times locale-safe. | Timezone bugs are silent data corruption for scheduling features. |
| 17 | Cross-device sync and conflict resolution | `before release` | Decide merge rules, last-write-wins exceptions, and visible recovery paths for conflicting edits. | Multi-device products feel broken when edits disappear or duplicate. |
| 18 | Data migrations and rollback safety | `before release` | Version schemas deliberately, test migrations, and define recovery behavior for partial failures. | Release quality depends on what happens to old data, not just new installs. |
| 19 | Performance telemetry for startup, rendering, and network | `before release` | Measure cold start, screen latency, jank, and slow requests before optimizing based on anecdotes. | Performance regressions compound quietly as features accumulate. |
| 20 | Settings and preference center | `low hanging fruit` | Centralize notification, theme, language, privacy, and account controls in one predictable place. | A clear settings surface reduces support tickets and hidden state. |
| 21 | In-app feedback, bug report, and support entry points | `low hanging fruit` | Add a simple way to contact support, report bugs, and optionally attach diagnostics. | Users will tell you what is broken if you make it easy. |
| 22 | Onboarding and activation checklist | `low hanging fruit` | Keep onboarding short, contextual, and focused on reaching the first meaningful success moment fast. | Activation lifts often outperform feature expansion early on. |
| 23 | Share sheets, import, export, and open-in workflows | `low hanging fruit` | Let users bring content in and send content out through native platform affordances. | Interop makes the app feel connected instead of siloed. |
| 24 | Search and quick find | `low hanging fruit` | Add fast, forgiving search once there is enough user data or content to justify it. | Search becomes table stakes sooner than most teams expect. |
| 25 | Web fallback pages for shared links | `low hanging fruit` | Make every important deep link degrade cleanly to web or install prompts when the app is missing. | Shared content should remain useful outside the app install base. |
| 26 | Experimentation and A/B testing on top of flags | `low hanging fruit` | Reuse flag infrastructure to validate onboarding, pricing, prompts, and growth ideas safely. | Good experiments depend on clean events and reversible rollouts. |
| 27 | Design tokens, theming, and dark mode readiness | `low hanging fruit` | Normalize typography, spacing, color tokens, and theme switching before UI sprawl sets in. | A tokenized UI is cheaper to evolve than a pile of screen-specific styling. |
| 28 | Role, permission, and entitlement model | `before release` | Model roles and access rules explicitly if there is any chance of teams, admins, or paid tiers. | Access logic spreads fast if it is not centralized early. |
| 29 | Admin and support tooling | `before release` | Give internal teams safe ways to inspect accounts, replay events, and toggle features without database edits. | Support quality depends on operator tools, not just user-facing polish. |
| 30 | Rate limiting, abuse controls, and suspicious activity detection | `before release` | Protect APIs, auth flows, invites, and uploads before traffic or growth loops invite abuse. | Abuse prevention is much easier before bad actors find the edges. |

## Suggested Rollout Order

1. Build items `1` through `11` as foundation work.
2. Treat items `12` through `19` plus `28` through `30` as release blockers for a public launch.
3. Use items `20` through `27` to improve activation, support, and retention once the core product loop is stable.
