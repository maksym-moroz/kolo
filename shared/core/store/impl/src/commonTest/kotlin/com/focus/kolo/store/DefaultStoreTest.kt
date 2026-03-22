package com.focus.kolo.store

import com.focus.kolo.store.fixture.CounterAction
import com.focus.kolo.store.fixture.CounterEffect
import com.focus.kolo.store.fixture.CounterIncrementAction
import com.focus.kolo.store.fixture.CounterLoadFinishedAction
import com.focus.kolo.store.fixture.CounterLoadRequestedAction
import com.focus.kolo.store.fixture.CounterState
import com.focus.kolo.store.impl.DefaultStoreFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultStoreTest {
    @Test
    fun dispatch_reduces_actions_in_order() = runTest {
        val storeScope = CoroutineScope(StandardTestDispatcher(testScheduler) + SupervisorJob())
        val store = DefaultStoreFactory().create<CounterState, CounterAction, CounterEffect>(
            initialState = CounterState(),
            scope = storeScope,
            reducer =
                Reducer<CounterState, CounterAction> { current, action ->
                    when (action) {
                        CounterIncrementAction ->
                            current
                                .copy(count = current.count + 1)
                        CounterLoadRequestedAction ->
                            current
                                .copy(loading = true)
                        CounterLoadFinishedAction ->
                            current
                                .copy(loading = false)
                    }
                }
        )

        store
            .dispatch(CounterIncrementAction)
        store
            .dispatch(CounterIncrementAction)
        advanceUntilIdle()

        assertEquals(2, store.state.value.count)
        storeScope
            .cancel()
    }

    @Test
    fun middleware_can_emit_effects_and_follow_up_actions() = runTest {
        val storeScope = CoroutineScope(StandardTestDispatcher(testScheduler) + SupervisorJob())
        val store = DefaultStoreFactory().create<CounterState, CounterAction, CounterEffect>(
            initialState = CounterState(),
            scope = storeScope,
            reducer =
                Reducer<CounterState, CounterAction> { current, action ->
                    when (action) {
                        CounterIncrementAction ->
                            current
                                .copy(count = current.count + 1)
                        CounterLoadRequestedAction ->
                            current
                                .copy(loading = true)
                        CounterLoadFinishedAction ->
                            current
                                .copy(loading = false)
                    }
                },
            middlewares = listOf(
                Middleware<CounterState, CounterAction, CounterEffect> { store, next ->
                    Next { action ->
                        next
                            .dispatch(action)
                        if (action == CounterLoadRequestedAction) {
                            store.emit(CounterEffect("loading"))
                            store.dispatch(CounterLoadFinishedAction)
                        }
                    }
                }
            )
        )

        val effect = async(start = CoroutineStart.UNDISPATCHED) {
            store.effects
                .first()
        }

        store.dispatch(CounterLoadRequestedAction)
        advanceUntilIdle()

        assertEquals(
            "loading",
            effect
                .await()
                .message
        )
        assertFalse(store.state.value.loading)
        storeScope
            .cancel()
    }
}
