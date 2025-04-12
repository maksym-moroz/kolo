package com.kolo.store

import com.kolo.action.Action
import com.kolo.action.AsEventAction
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

// todo move out into impl gradle project (eventually)

class KoloStore<S : State>(
    initialState: S,
    middleware: List<Middleware<S>>, // [1, 2, 3] -> (dispatch) -> (3, dispatch) -> ... -> (1, 2, 3, dispatch)
    reducer: (S, Action) -> S,
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
    private val interference = middleware.foldRight(dispatch) { acc, next -> acc.interfere(this, next) }

    // todo(maksym) components should subscribe to events

    init {
        reduce
            .runningFold(initialState) { state, action -> reducer(state, action) }
            // .onEach { println("state: $it") }
            .onEach(states::emit)
            .launchIn(scope)

        actions
            // .onEach { println("action: $it") }
            // todo fix
            // .onStart { emit(SystemAction.InitialAction) }
            .onEach { action ->
                when (action) {
                    // is separate pipeline for events a good idea? Seems like it
                    is AsEventAction -> events.emit(action)
                    else -> interference.perform(action)
                }
            }.launchIn(scope)

        // todo check if the fact it's in init block will make it fail
        // first make sure everything fired through, then launch initial action
//        scope.launch {
//            actions.emit(SystemAction.InitialAction)
//        }
    }

    override /*suspend*/ fun dispatch(action: Action) {
        // actions.emit(action)
        actions.tryEmit(action)
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
