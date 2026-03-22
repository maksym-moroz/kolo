package com.focus.kolo.debugmenu

import com.focus.kolo.config.impl.override.AppConfigOverrideEffect
import com.focus.kolo.config.impl.usecase.ClearAppConfigOverrideUseCase
import com.focus.kolo.config.impl.usecase.ResetAppConfigOverridesUseCase
import com.focus.kolo.config.impl.usecase.UpdateAppConfigOverrideUseCase
import com.focus.kolo.store.Middleware
import com.focus.kolo.store.Next
import com.focus.kolo.store.StoreScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class DebugMenuCommandMiddleware(
    private val updateAppConfigOverrideUseCase: UpdateAppConfigOverrideUseCase,
    private val clearAppConfigOverrideUseCase: ClearAppConfigOverrideUseCase,
    private val resetAppConfigOverridesUseCase: ResetAppConfigOverridesUseCase
) : Middleware<DebugMenuState, DebugMenuAction, DebugMenuEffect> {
    override fun interfere(
        store: StoreScope<DebugMenuState, DebugMenuAction, DebugMenuEffect>,
        next: Next<DebugMenuAction>
    ): Next<DebugMenuAction> = Next { action ->
        next.dispatch(action)

        when (action) {
            DebugMenuAction.IntentAction.Close -> launchClose(store)
            is DebugMenuAction.IntentAction.ClearOverride -> launchClearOverride(store, action)
            DebugMenuAction.IntentAction.ResetAllOverrides -> launchResetAllOverrides(store)
            is DebugMenuAction.IntentAction.SetEnvironment -> launchSetEnvironment(store, action)
            is DebugMenuAction.IntentAction.SetFeatureFlag -> launchSetFeatureFlag(store, action)
            is DebugMenuAction.IntentAction.SetLatestVersion -> launchSetLatestVersion(store, action)
            is DebugMenuAction.IntentAction.SetMinimumSupportedVersion -> launchSetMinimumSupportedVersion(store, action)
            is DebugMenuAction.SystemAction -> Unit
        }
    }

    private fun launchClose(store: StoreScope<DebugMenuState, DebugMenuAction, DebugMenuEffect>) {
        store.scope.launch {
            executeCommand(store, "Close debug menu") {
                store.emit(DebugMenuEffect.Close)
            }
        }
    }

    private fun launchClearOverride(
        store: StoreScope<DebugMenuState, DebugMenuAction, DebugMenuEffect>,
        action: DebugMenuAction.IntentAction.ClearOverride
    ) {
        store.scope.launch {
            executeCommand(store, "Clear override") {
                clearAppConfigOverrideUseCase(action.field)
            }
        }
    }

    private fun launchResetAllOverrides(store: StoreScope<DebugMenuState, DebugMenuAction, DebugMenuEffect>) {
        store.scope.launch {
            executeCommand(store, "Reset all overrides") {
                resetAppConfigOverridesUseCase()
            }
        }
    }

    private fun launchSetEnvironment(
        store: StoreScope<DebugMenuState, DebugMenuAction, DebugMenuEffect>,
        action: DebugMenuAction.IntentAction.SetEnvironment
    ) {
        store.scope.launch {
            executeCommand(store, "Set environment") {
                val currentEnvironment = store.currentState.snapshot
                    ?.config
                    ?.environment
                if (currentEnvironment == action.environment) {
                    return@executeCommand
                }

                updateAppConfigOverrideUseCase(
                    AppConfigOverrideEffect.SetEnvironment(action.environment)
                )
                store.emit(DebugMenuEffect.RestartRequired)
            }
        }
    }

    private fun launchSetFeatureFlag(
        store: StoreScope<DebugMenuState, DebugMenuAction, DebugMenuEffect>,
        action: DebugMenuAction.IntentAction.SetFeatureFlag
    ) {
        store.scope.launch {
            executeCommand(store, "Set feature flag") {
                updateAppConfigOverrideUseCase(
                    AppConfigOverrideEffect.SetFeatureFlag(
                        key = action.key,
                        enabled = action.enabled
                    )
                )
            }
        }
    }

    private fun launchSetLatestVersion(
        store: StoreScope<DebugMenuState, DebugMenuAction, DebugMenuEffect>,
        action: DebugMenuAction.IntentAction.SetLatestVersion
    ) {
        store.scope.launch {
            executeCommand(store, "Set latest version") {
                updateAppConfigOverrideUseCase(
                    AppConfigOverrideEffect.SetLatestVersion(action.value)
                )
            }
        }
    }

    private fun launchSetMinimumSupportedVersion(
        store: StoreScope<DebugMenuState, DebugMenuAction, DebugMenuEffect>,
        action: DebugMenuAction.IntentAction.SetMinimumSupportedVersion
    ) {
        store.scope.launch {
            executeCommand(store, "Set minimum supported version") {
                updateAppConfigOverrideUseCase(
                    AppConfigOverrideEffect.SetMinimumSupportedVersion(action.value)
                )
            }
        }
    }

    private suspend fun executeCommand(
        store: StoreScope<DebugMenuState, DebugMenuAction, DebugMenuEffect>,
        commandName: String,
        block: suspend () -> Unit
    ) {
        val failure = runCatching {
            block()
        }.exceptionOrNull()

        when (failure) {
            null -> {
                store.dispatch(DebugMenuAction.SystemAction.CommandSucceeded)
            }
            is CancellationException -> {
                throw failure
            }
            else -> {
                val detail = failure.message ?: failure::class.simpleName ?: "Unknown failure"
                store.dispatch(
                    DebugMenuAction.SystemAction.CommandFailed(
                        message = "$commandName failed: $detail"
                    )
                )
            }
        }
    }
}
