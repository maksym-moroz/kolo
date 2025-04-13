package com.kolo.store

import com.kolo.action.Action
import com.kolo.action.AsEventAction
import com.kolo.action.SystemAction
import com.kolo.middleware.Dispatch
import com.kolo.middleware.builder.StoreMiddlewareBuilder
import com.kolo.reducer.Reducer
import com.kolo.state.State
import com.kolo.store.configuration.StoreConfiguration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.runningFold

// todo move out into impl gradle project (eventually)

class KoloStore<S : State>(
    configuration: StoreConfiguration,
    initialState: S,
    reducer: Reducer<S>,
    outerScope: CoroutineScope,
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate, // todo need immediate?
) : Store<S> {
    override val scope: CoroutineScope =
        CoroutineScope(dispatcher + SupervisorJob(outerScope.coroutineContext[Job]))

    override val states: StateFlow<S>
        private field: MutableStateFlow<S> = MutableStateFlow(initialState)

    override val events: SharedFlow<Action>
        private field: MutableSharedFlow<Action> = MutableSharedFlow()

    // todo make it distinct from Action somehow
    override val actions: SharedFlow<Action>
        private field: MutableSharedFlow<Action> = MutableSharedFlow(extraBufferCapacity = 64, replay = 1)

    private val reduce: MutableSharedFlow<Action> = MutableSharedFlow(replay = 1)

    private val middleware =
        StoreMiddlewareBuilder<S>()
            .withEventEffects(configuration.eventEffects)
            .withActionEffects(configuration.actionEffects)
            .withParentDispatch(configuration.parentDispatch)
            // todo create standard middleware like logging, add here
            .withAdditionalMiddleware(emptyList())
            .build()

    private val dispatch = Dispatch<Action> { action: Action -> reduce.emit(action) }
    private val interference = middleware.foldRight(dispatch) { acc, next -> acc.interfere(this, next) }

    // todo(maksym) components should subscribe to events

    init {
        reduce
            // todo check if works with states.value
            .runningFold(states.value) { state, action -> reducer.reduce(state, action) }
            .onEach(states::emit)
            .launchIn(scope)

        actions
            .onStart { emit(SystemAction.InitialAction) }
            .onEach { action ->
                when (action) {
                    // is separate pipeline for events a good idea? Seems like it
                    is AsEventAction -> events.emit(action)
                    else -> interference.perform(action)
                }
            }.launchIn(scope)
    }

    // todo items are being dropped
    override /*suspend*/ fun dispatch(action: Action) {
        actions.tryEmit(action)
    }
}
