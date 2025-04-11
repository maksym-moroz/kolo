package com.kolo.store

import com.kolo.action.Action
import com.kolo.action.EventAction
import com.kolo.action.FeatureAction
import com.kolo.action.ResultAction
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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.runningFold

// todo move out into impl gradle project (eventually)

class KoloStore<S : State>(
    initialState: S,
    middleware: List<Middleware<Action, S>>,
    reducer: S.(action: Action) -> S,
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
        private field: MutableSharedFlow<Action> = MutableSharedFlow(replay = 1)

    private val reduce: MutableSharedFlow<Action> = MutableSharedFlow(replay = 1)

    private val dispatch = Dispatch<Action> { action: Action -> reduce.emit(action) }
    private val interference =
        middleware.foldRight(dispatch) { acc, next -> acc.interfere(this, next) }

    init {
        reduce
            .runningFold(initialState) { state, action -> state.reducer(action) }
            .onEach(states::emit)
            .launchIn(scope)

        actions
            .onStart { emit(SystemAction.InitialAction) }
            .onEach(::processAction)
            .launchIn(scope)

        // todo check if the fact it's in init block will make it fail
        // first make sure everything fired through, then launch initial action
//        scope.launch {
//            actions.emit(SystemAction.InitialAction)
//        }
    }

    private suspend fun processAction(action: Action) {
        when (action) {
            is EventAction -> {
                // good enough?
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

            is ResultAction -> {
                // how to do this with no middleware?
                interference.perform(action)
            }
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
