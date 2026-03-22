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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class DefaultStore<S : UiState, A : UiAction, E : UiEffect>(
    initialState: S,
    override val scope: CoroutineScope,
    private val reducer: Reducer<S, A>,
    middlewares: List<Middleware<S, A, E>> = emptyList(),
    effectBufferCapacity: Int = StoreFactory.DEFAULT_EFFECT_BUFFER_CAPACITY
) : Store<S, A, E>,
    StoreScope<S, A, E> {
    override val actions = MutableSharedFlow<A>(extraBufferCapacity = ACTION_BUFFER_CAPACITY)
    override val state = MutableStateFlow(initialState)
    override val effects = MutableSharedFlow<E>(extraBufferCapacity = effectBufferCapacity)
    private val actionQueue = Channel<A>(capacity = Channel.BUFFERED)

    private val reducerDispatch =
        Next<A> { action ->
            state.value =
                reducer
                    .reduce(state.value, action)
        }
    private var pipeline: Next<A> = reducerDispatch

    init {
        pipeline =
            middlewares.foldRight(reducerDispatch) { middleware, next ->
                middleware
                    .interfere(this, next)
            }

        scope.launch {
            for (action in actionQueue) {
                actions.emit(action)
                pipeline.dispatch(action)
            }
        }
    }

    override suspend fun dispatch(action: A) {
        actionQueue.send(action)
    }

    override suspend fun emit(effect: E) {
        effects.emit(effect)
    }

    private companion object {
        const val ACTION_BUFFER_CAPACITY = 64
    }
}
