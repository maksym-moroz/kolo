package com.focus.kolo.store.impl

import com.focus.kolo.store.Middleware
import com.focus.kolo.store.Reducer
import com.focus.kolo.store.Store
import com.focus.kolo.store.StoreFactory
import com.focus.kolo.store.UiAction
import com.focus.kolo.store.UiEffect
import com.focus.kolo.store.UiState
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.CoroutineScope

@Inject
class DefaultStoreFactory : StoreFactory {
    override fun <S : UiState, A : UiAction, E : UiEffect> create(
        scope: CoroutineScope,
        initialState: S,
        reducer: Reducer<S, A>,
        middlewares: List<Middleware<S, A, E>>,
        effectBufferCapacity: Int
    ): Store<S, A, E> = DefaultStore(
        initialState = initialState,
        scope = scope,
        reducer = reducer,
        middlewares = middlewares,
        effectBufferCapacity = effectBufferCapacity
    )
}
