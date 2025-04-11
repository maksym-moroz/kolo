package com.kolo.store

import com.kolo.action.Action
import com.kolo.action.FeatureAction
import com.kolo.action.OneShotAction
import com.kolo.action.SystemAction
import com.kolo.middleware.Middleware
import com.kolo.middleware.dispatch.Dispatch
import com.kolo.state.State
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
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.launch

// todo move out into impl gradle project (eventually)

class KoloStore<S : State>(
    initialState: S,
    middleware: List<Middleware<Action, S>>,
    reducer: S.(action: Action) -> S,
    outerScope: CoroutineScope,
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate, // todo need immediate?
) : Store<S> {
    private val storeScope =
        CoroutineScope(dispatcher + SupervisorJob(outerScope.coroutineContext[Job]))

    override val states: StateFlow<S>
        private field: MutableStateFlow<S> = MutableStateFlow(initialState)

    override val events: SharedFlow<Action>
        private field: MutableSharedFlow<Action> = MutableSharedFlow()

    private val actions: MutableSharedFlow<Action> =
        MutableSharedFlow(replay = 1) // todo need replay?
    private val reduce: MutableSharedFlow<Action> =
        MutableSharedFlow(replay = 1) // todo need replay?

    private val dispatch = Dispatch<Action> { action: Action -> reduce.emit(action) }
    private val interference =
        middleware.foldRight(dispatch) { acc, next -> acc.interfere(this, next) }

    init {
        reduce
            .runningFold(initialState) { state, action -> state.reducer(action) }
            .onEach(states::emit)
            .launchIn(storeScope)

        actions
            .onEach { action ->
                when (action) {
                    is OneShotAction -> {
                        events.emit(action)
                    }

                    is SystemAction -> {
                        // todo separate pipeline for system events
                        interference.perform(action)
                    }

                    is FeatureAction -> {
                        // can we make this better if we know it's a feature action?
                        interference.perform(action)
                    }
                }
            }.launchIn(storeScope)

        storeScope.launch {
            actions.emit(SystemAction.InitialAction)
        }
    }

    override suspend fun dispatch(action: Action) {
        actions.emit(action)
    }
}

// middleware -> internal layers
// action -> states.value!!!

// -> Initial Action -> i > states -> states change ->

// state A -> state A -> state A -> x
// if deterministic no point in replaying the same state
// in that case we can conflate 3A -> A

// each action should be propagated into the middleware layer
// then it should be reduced to a state and emitted
