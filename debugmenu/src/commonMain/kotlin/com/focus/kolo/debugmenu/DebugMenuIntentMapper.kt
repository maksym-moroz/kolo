package com.focus.kolo.debugmenu

import com.focus.kolo.store.IntentMapper

class DebugMenuIntentMapper : IntentMapper<DebugMenuIntent, DebugMenuAction> {
    override fun map(intent: DebugMenuIntent): DebugMenuAction = when (intent) {
        is DebugMenuIntent.ClearOverride -> {
            DebugMenuAction.IntentAction
                .ClearOverride(intent.field)
        }
        DebugMenuIntent.Close -> {
            DebugMenuAction.IntentAction.Close
        }
        DebugMenuIntent.ResetAllOverrides -> {
            DebugMenuAction.IntentAction.ResetAllOverrides
        }
        is DebugMenuIntent.SetEnvironment -> {
            DebugMenuAction.IntentAction
                .SetEnvironment(intent.environment)
        }
        is DebugMenuIntent.SetFeatureFlag -> {
            DebugMenuAction.IntentAction
                .SetFeatureFlag(intent.key, intent.enabled)
        }
        is DebugMenuIntent.SetLatestVersion -> {
            DebugMenuAction.IntentAction
                .SetLatestVersion(intent.value)
        }
        is DebugMenuIntent.SetMinimumSupportedVersion -> {
            DebugMenuAction.IntentAction
                .SetMinimumSupportedVersion(intent.value)
        }
    }
}
