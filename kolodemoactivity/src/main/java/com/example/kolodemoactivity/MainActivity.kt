package com.example.kolodemoactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.kolodemoactivity.generate.component.configuration.getComponentConfiguration
import com.example.kolodemoactivity.generate.component.initialiseComponent
import com.example.kolodemoactivity.generate.context.reduce.initialiseReduceContext
import com.example.kolodemoactivity.generate.store.getStoreConfiguration
import com.example.kolodemoactivity.ui.theme.KoloTheme
import com.kolo.component.common.KoloComponent
import com.kolo.component.composition.container.EffectContainer
import com.kolo.component.composition.context.reduce.ReduceContext
import com.kolo.component.composition.context.store.StoreContext
import com.kolo.component.composition.context.store.StoreContextDelegate
import com.kolo.component.configuration.ComponentConfiguration
import com.kolo.effect.Effect
import com.kolo.example.container.RootEffectContainer
import com.kolo.example.state.RootState
import com.kolo.middleware.communication.ParentDispatch
import com.kolo.middleware.communication.ParentDispatchImpl
import com.kolo.middleware.communication.ParentDispatchNoop
import com.kolo.reducer.Reducer
import com.kolo.reducer.ReducerImpl
import com.kolo.store.KoloStore
import com.kolo.store.configuration.StoreConfiguration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val state = RootState(counter = -1)

        val container: EffectContainer = RootEffectContainer()
        val component: KoloComponent<*, RootState> = initialiseComponent(state, container)

        val reduceContext: ReduceContext = initialiseReduceContext()

        val reducer: Reducer<RootState> = ReducerImpl(component, reduceContext)

        // todo split action and event effects into containers
        val effects: List<Effect> = container.effects()

        val componentConfiguration: ComponentConfiguration = getComponentConfiguration(component)

        // looks like a circular dependency but not really, since either components don't have a parent
        // in that there is no mismatch, or the component have a parent, which means store will be available
        // but this definitely requires a refactor

        // [component, store, etc] as a single entity, if hasContracts store should be accessible
        val storeContext: StoreContext? =
            if (componentConfiguration.hasContracts) {
                StoreContextDelegate<RootState>(TODO())
            } else {
                null
            }

        val parentDispatch: ParentDispatch =
            if (storeContext != null) {
                ParentDispatchImpl(storeContext)
            } else {
                ParentDispatchNoop
            }

        val storeConfiguration: StoreConfiguration = getStoreConfiguration(effects, parentDispatch)

        val store =
            KoloStore<RootState>(
                configuration = storeConfiguration,
                initialState = state,
                reducer = reducer,
                outerScope = lifecycleScope,
            )

        val componentDispatch: StoreContext = StoreContextDelegate(store)

        setContent {
            KoloTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    component.content.android(componentDispatch, store.states.collectAsState().value)
                }
            }
        }
    }
}

//            [parent (initial) --> child, parent (eventual) --> child]
//            child declares contract, it's on parent to fulfill it, otherwise can't create
//            reduce signature includes both State and Contract to react accordingly
