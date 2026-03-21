package com.focus.kolo.store.impl

import com.focus.kolo.store.Middleware
import com.focus.kolo.store.Next
import com.focus.kolo.store.Reducer
import com.focus.kolo.store.Store
import com.focus.kolo.store.StoreFactory
import com.focus.kolo.store.StoreScope
import com.focus.kolo.store.UiAction
import com.focus.kolo.store.UiEffect
import com.focus.kolo.store.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class DefaultStore<S : UiState, A : UiAction, E : UiEffect>(
    initialState: S,
    scope: CoroutineScope,
    private val reducer: Reducer<S, A>,
    middlewares: List<Middleware<S, A, E>> = emptyList(),
    effectBufferCapacity: Int = StoreFactory.DEFAULT_EFFECT_BUFFER_CAPACITY,
) : Store<S, A, E>, StoreScope<S, A, E> {
    private val actions = Channel<A>(capacity = Channel.BUFFERED)
    private val mutableState = MutableStateFlow(initialState)
    private val mutableEffects = MutableSharedFlow<E>(extraBufferCapacity = effectBufferCapacity)

    override val state = mutableState.asStateFlow()
    override val effects = mutableEffects.asSharedFlow()

    private val pipeline: Next<A> = middlewares
        .asReversed()
        .fold(Next<A> { action ->
            mutableState.value = reducer.reduce(mutableState.value, action)
        }) { next, middleware ->
            Next { action ->
                middleware.intercept(action, this, next)
            }
        }

    init {
        scope.launch {
            actions.consumeEach { action ->
                pipeline.dispatch(action)
            }
        }
    }

    override suspend fun dispatch(action: A) {
        actions.send(action)
    }

    override suspend fun emit(effect: E) {
        mutableEffects.emit(effect)
    }
}
