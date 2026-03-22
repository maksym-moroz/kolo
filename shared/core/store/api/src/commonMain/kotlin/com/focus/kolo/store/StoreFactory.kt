package com.focus.kolo.store

import kotlinx.coroutines.CoroutineScope

interface StoreFactory {
    fun <S : UiState, A : UiAction, E : UiEffect> create(
        scope: CoroutineScope,
        initialState: S,
        reducer: Reducer<S, A>,
        middlewares: List<Middleware<S, A, E>> = emptyList(),
        effectBufferCapacity: Int = DEFAULT_EFFECT_BUFFER_CAPACITY
    ): Store<S, A, E>

    companion object {
        const val DEFAULT_EFFECT_BUFFER_CAPACITY: Int = 16
    }
}
