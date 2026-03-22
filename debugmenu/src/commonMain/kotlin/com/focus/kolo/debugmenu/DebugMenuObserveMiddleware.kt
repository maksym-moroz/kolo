package com.focus.kolo.debugmenu

import com.focus.kolo.config.AppConfigRepository
import com.focus.kolo.store.Middleware
import com.focus.kolo.store.Next
import com.focus.kolo.store.StoreScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class DebugMenuObserveMiddleware(
    private val appConfigRepository: AppConfigRepository
) : Middleware<DebugMenuState, DebugMenuAction, DebugMenuEffect> {
    override fun interfere(
        store: StoreScope<DebugMenuState, DebugMenuAction, DebugMenuEffect>,
        next: Next<DebugMenuAction>
    ): Next<DebugMenuAction> {
        appConfigRepository.snapshot
            .map(DebugMenuAction.SystemAction::ConfigObserved)
            .onEach(store::dispatch)
            .launchIn(store.scope)

        return Next(next::dispatch)
    }
}
